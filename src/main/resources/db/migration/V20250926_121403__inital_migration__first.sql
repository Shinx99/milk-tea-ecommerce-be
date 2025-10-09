-- Migration: inital_migration - testt
-- Created: Fri Sep 26 12:14:03 PM +07 2025
-- Author: mango

-- Add your SQL statements below:
-- Enable once
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1) ROLES trước USERS
create table if not exists roles (
  id uuid primary key default gen_random_uuid(),
  role varchar(30) not null unique,
  description text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  is_active boolean not null default true
);

create table if not exists users (
  id uuid primary key default gen_random_uuid(),
  email varchar(255) not null unique,
  password_hash varchar(255) not null,
  is_active boolean not null default true,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  role_id uuid not null references roles(id),
  customer_id uuid not null references customers(id)
);

-- 1b) CUSTOMERS & ADDRESSES
create table if not exists customers(
  id uuid primary key default gen_random_uuid(),
  email varchar(255) not null unique,
  password_hash varchar(255) not null,
  phone varchar(40) not null,
  fullname varchar(255) not null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  is_active boolean not null default true
);

create table if not exists addresses (
  id uuid primary key default gen_random_uuid(),
  customer_id uuid not null references customers(id),
  number varchar(50),
  street varchar(100),
  ward varchar(100),
  district varchar(100),
  city varchar(100),
  province varchar(100),
  country varchar(100) default 'VN',
  is_default boolean not null default true,
  is_active boolean not null default true
);
create unique index if not exists ux_addr_default_per_user
  on addresses(customer_id) where is_default = true;

-- 2) CATEGORY
create table if not exists categories (
  id uuid primary key default gen_random_uuid(),
  parent_id uuid references categories(id),
  category_name varchar(255) not null,
  slug varchar(255) unique,
  sort_order int not null default 0,
  is_active boolean not null default true,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint ck_cat_parent_self check (parent_id is null or parent_id <> id)
);
create unique index if not exists ux_cat_parent_name
  on categories(parent_id, lower(category_name));

-- 3) PRODUCT
create table if not exists products(
  id uuid primary key default gen_random_uuid(),
  category_id uuid references categories(id),
  name varchar(255) not null,
  slug varchar(255) unique,
  description text,
  quantity int not null default 0 check (quantity >= 0),
  price numeric(10,2) not null default 0 check (price >= 0),
  is_active boolean not null default true,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index if not exists idx_prod_category on products(category_id);
create index if not exists idx_prod_active   on products(is_active);

create table if not exists images (
  id uuid primary key default gen_random_uuid(),
  product_id uuid not null references products(id),
  sort_order int not null default 0,
  is_primary boolean not null default false,
  public_id varchar(255) not null,
  secure_url varchar(1000) not null,
  resource_type varchar(20),
  format varchar(16),
  width int,
  height int,
  bytes bigint,
  alt_text varchar(255),
  created_at timestamptz not null default now()
);
create index if not exists idx_images_product on images(product_id);
create unique index if not exists ux_images_pubid on images(public_id);
create unique index if not exists ux_img_sort_per_product
  on images(product_id, sort_order);
create unique index if not exists ux_img_primary_per_product
  on images(product_id) where is_primary = true;

-- 4) CARTS
do $$
begin
  if not exists (select 1 from pg_type where typname = 'carts_status_enum') then
    create type carts_status_enum as enum (
      'active',
      'merged',
      'abandoned',
      'checked_out',
      'expired'
    );
  end if;
end$$;

-- 4a) CARTS (trung gian giữa customers và products)
create table if not exists carts (
  id uuid primary key default gen_random_uuid(),
  customer_id uuid not null references customers(id),
  product_id uuid not null references products(id),
  quantity int not null check (quantity > 0),
  price numeric(12,2) not null check (price >= 0),
  status carts_status_enum not null default 'active',
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  expires_at timestamptz
);

create index if not exists idx_carts_customer on carts(customer_id);
create index if not exists idx_carts_product on carts(product_id);
create index if not exists idx_carts_status on carts(status);

-- Mỗi customer chỉ có 1 cart đang active
create unique index if not exists ux_one_active_cart_per_customer
  on carts(customer_id)
  where status = 'active';

-- 5) VOUCHERS + trigger function
create table if not exists vouchers (
  id uuid primary key default gen_random_uuid(),
  code varchar(80) not null unique,
  discount_type varchar(100) not null,
  number numeric(12,2) not null check (number >= 0),
  start_at timestamptz,
  expired_at timestamptz,
  is_active boolean not null default false,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create or replace function set_updated_at() returns trigger as $$
begin
  new.updated_at := now();
  return new;
end;
$$ language plpgsql; --for logic updated_at

create trigger trg_vouchers_updated_at
before update on vouchers
for each row execute function set_updated_at(); --for logic updated_at

-- Quan hệ voucher ↔ sản phẩm (áp theo item)
CREATE TABLE IF NOT EXISTS voucher_products (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  voucher_id uuid NOT NULL REFERENCES vouchers(id) ON DELETE CASCADE,
  product_id uuid NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  created_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (voucher_id, product_id)
);
CREATE INDEX IF NOT EXISTS idx_voucher_products_voucher ON voucher_products(voucher_id);
CREATE INDEX IF NOT EXISTS idx_voucher_products_product ON voucher_products(product_id);


-- Quan hệ voucher ↔ khách hàng
CREATE TABLE IF NOT EXISTS voucher_customers (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  voucher_id uuid NOT NULL REFERENCES vouchers(id) ON DELETE CASCADE,
  customer_id uuid NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
  created_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (voucher_id, customer_id)
);
CREATE INDEX IF NOT EXISTS idx_voucher_customers_voucher ON voucher_customers(voucher_id);
CREATE INDEX IF NOT EXISTS idx_voucher_customers_customer ON voucher_customers(customer_id);


-- 6) ORDERS
do $$
begin
  if not exists (select 1 from pg_type where typname = 'order_status_enum') then
    create type order_status_enum as enum (
      'pending',
      'confirmed',
      'shipped',
      'completed',
      'cancelled',
      'refunded'
    );
  end if;
end$$;

create table if not exists orders (
  id uuid primary key default gen_random_uuid(),
  customer_id uuid references customers(id),
  status order_status_enum not null default 'pending',
  placed_at timestamptz not null default now(),
  confirmed_at timestamptz,
  completed_at timestamptz,
  cancelled_at timestamptz,
  description text,
  subtotal numeric(12,2) not null default 0 check (subtotal >= 0),
  discount_total numeric(12,2) not null default 0 check (discount_total >= 0),
  tax_total numeric(12,2) not null default 0 check (tax_total >= 0),
  shipping_fee numeric(12,2) not null default 0 check (shipping_fee >= 0),
  total numeric(12,2) not null default 0 check (total >= 0)
);
create index if not exists idx_orders_customer on orders(customer_id);
create index if not exists idx_orders_status on orders(status);

create table if not exists order_items (
  id uuid primary key default gen_random_uuid(),
  order_id uuid not null references orders(id),
  product_id uuid not null references products(id),
  quantity int not null check (quantity > 0),
  price numeric(12,2) not null check (price >= 0),
  line_total numeric(12,2) generated always as (quantity * price) stored
);
create index if not exists idx_order_items_order on order_items(order_id);
create index if not exists idx_order_items_product on order_items(product_id);


-- VOUCHER REDEMPTION - Ghi nhận redemption để đối soát & giới hạn
CREATE TABLE IF NOT EXISTS voucher_redemptions (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  voucher_id uuid NOT NULL REFERENCES vouchers(id) ON DELETE CASCADE,
  order_id uuid NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
  customer_id uuid REFERENCES customers(id),
  redeemed_at timestamptz NOT NULL DEFAULT now(),
  discount_amount numeric(12,2) CHECK (discount_amount IS NULL OR discount_amount >= 0),
  -- 1 voucher/1 order chỉ ghi 1 lần
  UNIQUE (voucher_id, order_id)
);

CREATE INDEX IF NOT EXISTS idx_redemptions_voucher ON voucher_redemptions(voucher_id);
CREATE INDEX IF NOT EXISTS idx_redemptions_customer ON voucher_redemptions(customer_id);
CREATE INDEX IF NOT EXISTS idx_redemptions_order ON voucher_redemptions(order_id);
-- 7) PAYMENTS
do $$
begin
  if not exists (select 1 from pg_type where typname = 'payment_status_enum') then
    create type payment_status_enum as enum (
      'pending',
      'requires_action',
      'authorized',
      'paid',
      'processing',
      'failed',
      'voided',
      'partially_refunded',
      'refunded',
      'cancelled'
    );
  end if;
end$$;

create table if not exists payments (
  id uuid primary key default gen_random_uuid(),
  order_id uuid not null unique references orders(id),
  provider varchar(80) not null,
  payload jsonb,
  transaction_ref varchar(160),
  status payment_status_enum not null default 'pending',
  amount numeric(12,2) not null check (amount >= 0),
  created_at timestamptz not null default now(),
  paid_at timestamptz,
  refunded_at timestamptz
);
create index if not exists idx_payments_status on payments(status);
create index if not exists idx_payments_txref on payments(transaction_ref);


