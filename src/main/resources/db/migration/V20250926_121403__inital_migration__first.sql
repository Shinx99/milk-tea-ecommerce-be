-- Migration: inital_migration - first
-- Created: Fri Sep 26 12:14:03 PM +07 2025
-- Author: Vuong

-- Add your SQL statements below:
-- Enable once
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1) ROLES trước (không phụ thuộc ai)
CREATE TABLE IF NOT EXISTS roles (
                                     id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    role varchar(30) NOT NULL UNIQUE,
    description text,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true
    );

-- 2) USERS
CREATE TABLE IF NOT EXISTS users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email varchar(255) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    role_id uuid NOT NULL REFERENCES roles(id) ON DELETE RESTRICT
    );
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role_id);


-- 2) CUSTOMERS trước USERS (vì users references customers)
CREATE TABLE IF NOT EXISTS customers(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    phone varchar(40) NOT NULL UNIQUE,
    fullname varchar(255) NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true,
    user_id uuid references users(id) not null
    );
CREATE INDEX IF NOT EXISTS idx_customers_users ON customers(user_id);

-- 3) ADDRESSES (sau customers)
CREATE TABLE IF NOT EXISTS addresses (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id uuid NOT NULL REFERENCES customers(id),
    number varchar(50),
    street varchar(100),
    ward varchar(100),
    district varchar(100),
    city varchar(100),
    province varchar(100),
    country varchar(100) DEFAULT 'VN',
    is_default boolean NOT NULL DEFAULT true,
    is_active boolean NOT NULL DEFAULT true
    );
CREATE UNIQUE INDEX IF NOT EXISTS ux_addr_default_per_user
    ON addresses(customer_id) WHERE is_default = true;

-- 5) CATEGORIES (tự tham chiếu)
CREATE TABLE IF NOT EXISTS categories (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    parent_id uuid REFERENCES categories(id),
    category_name varchar(255) NOT NULL,
    sort_order int NOT NULL DEFAULT 0,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT ck_cat_parent_self CHECK (parent_id IS NULL OR parent_id <> id)
    );
CREATE UNIQUE INDEX IF NOT EXISTS ux_cat_parent_name
    ON categories(parent_id, lower(category_name));

-- 6) PRODUCTS
CREATE TABLE IF NOT EXISTS products(
                                       id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id uuid REFERENCES categories(id),
    name varchar(255) NOT NULL,
    description text,
    quantity int NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    price numeric(10,2) NOT NULL DEFAULT 0 CHECK (price >= 0),
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
    );
CREATE INDEX IF NOT EXISTS idx_prod_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_prod_active ON products(is_active);

-- 7) IMAGES
-- Bảng IMAGES tối ưu cho Cloudinary (không dùng bảng variants)
CREATE TABLE IF NOT EXISTS images (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id uuid NOT NULL REFERENCES products(id),
    sort_order int NOT NULL DEFAULT 0,
    is_primary boolean NOT NULL DEFAULT false,

    -- Cloudinary keys
    asset_id varchar(64),                -- bất biến
    public_id varchar(255) NOT NULL,     -- để build URL/API
    resource_type varchar(20) NOT NULL DEFAULT 'image',  -- image|video|raw
    delivery_type varchar(32) NOT NULL DEFAULT 'upload', -- upload|authenticated|private|fetch...
    version bigint,                      -- cache-busting khi overwrite

-- Thông tin hiển thị/quản trị
    format varchar(16),
    width int,
    height int,
    bytes bigint,
    secure_url varchar(1000),
    alt_text varchar(255),

    created_at timestamptz NOT NULL DEFAULT now()
    );

-- Chỉ mục/Unique theo đúng không gian ID của Cloudinary
CREATE INDEX IF NOT EXISTS idx_images_product ON images(product_id);
CREATE INDEX IF NOT EXISTS idx_images_assetid ON images(asset_id);
DROP INDEX IF EXISTS ux_images_pubid;
CREATE UNIQUE INDEX IF NOT EXISTS ux_images_pubid_scoped
    ON images(public_id, resource_type, delivery_type);

-- Thứ tự ảnh trong sản phẩm
CREATE UNIQUE INDEX IF NOT EXISTS ux_img_sort_per_product
    ON images(product_id, sort_order);

-- 8) CARTS ENUM
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'carts_status_enum') THEN
CREATE TYPE carts_status_enum AS ENUM (
      'active',
      'merged',
      'abandoned',
      'checked_out',
      'expired'
    );
END IF;
END$$;

-- 9) CARTS
CREATE TABLE IF NOT EXISTS carts (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id uuid NOT NULL REFERENCES customers(id),
    product_id uuid NOT NULL REFERENCES products(id),
    quantity int NOT NULL CHECK (quantity > 0),
    price numeric(12,2) NOT NULL CHECK (price >= 0),
    status carts_status_enum NOT NULL DEFAULT 'active',
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    expires_at timestamptz
    );
CREATE INDEX IF NOT EXISTS idx_carts_customer ON carts(customer_id);
CREATE INDEX IF NOT EXISTS idx_carts_product ON carts(product_id);
CREATE INDEX IF NOT EXISTS idx_carts_status ON carts(status);
CREATE UNIQUE INDEX IF NOT EXISTS ux_one_active_cart_per_customer
    ON carts(customer_id) WHERE status = 'active';

-- 10) VOUCHERS
CREATE TABLE IF NOT EXISTS vouchers (
                                        id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code varchar(80) NOT NULL UNIQUE,
    discount_type varchar(100) NOT NULL,
    number numeric(12,2) NOT NULL CHECK (number >= 0),
    start_at timestamptz,
    expired_at timestamptz,
    is_active boolean NOT NULL DEFAULT false,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
    );

CREATE OR REPLACE FUNCTION set_updated_at() RETURNS trigger AS $$
BEGIN
  NEW.updated_at := now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_vouchers_updated_at
    BEFORE UPDATE ON vouchers
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 11) VOUCHER_PRODUCTS
CREATE TABLE IF NOT EXISTS voucher_products (
                                                id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    voucher_id uuid NOT NULL REFERENCES vouchers(id) ON DELETE CASCADE,
    product_id uuid NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    created_at timestamptz NOT NULL DEFAULT now(),
    UNIQUE (voucher_id, product_id)
    );
CREATE INDEX IF NOT EXISTS idx_voucher_products_voucher ON voucher_products(voucher_id);
CREATE INDEX IF NOT EXISTS idx_voucher_products_product ON voucher_products(product_id);

-- 12) VOUCHER_CUSTOMERS
CREATE TABLE IF NOT EXISTS voucher_customers (
                                                 id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    voucher_id uuid NOT NULL REFERENCES vouchers(id) ON DELETE CASCADE,
    customer_id uuid NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    created_at timestamptz NOT NULL DEFAULT now(),
    UNIQUE (voucher_id, customer_id)
    );
CREATE INDEX IF NOT EXISTS idx_voucher_customers_voucher ON voucher_customers(voucher_id);
CREATE INDEX IF NOT EXISTS idx_voucher_customers_customer ON voucher_customers(customer_id);

-- 13) ORDERS ENUM
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'order_status_enum') THEN
CREATE TYPE order_status_enum AS ENUM (
      'pending',
      'confirmed',
      'shipped',
      'completed',
      'cancelled',
      'refunded'
    );
END IF;
END$$;

-- 14) ORDERS
CREATE TABLE IF NOT EXISTS orders (
                                      id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id uuid REFERENCES customers(id),
    status order_status_enum NOT NULL DEFAULT 'pending',
    placed_at timestamptz NOT NULL DEFAULT now(),
    confirmed_at timestamptz,
    completed_at timestamptz,
    cancelled_at timestamptz,
    description text,
    subtotal numeric(12,2) NOT NULL DEFAULT 0 CHECK (subtotal >= 0),
    discount_total numeric(12,2) NOT NULL DEFAULT 0 CHECK (discount_total >= 0),
    tax_total numeric(12,2) NOT NULL DEFAULT 0 CHECK (tax_total >= 0),
    shipping_fee numeric(12,2) NOT NULL DEFAULT 0 CHECK (shipping_fee >= 0),
    total numeric(12,2) NOT NULL DEFAULT 0 CHECK (total >= 0)
    );
CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);

-- 15) ORDER_ITEMS
CREATE TABLE IF NOT EXISTS order_items (
                                           id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id uuid NOT NULL REFERENCES orders(id),
    product_id uuid NOT NULL REFERENCES products(id),
    quantity int NOT NULL CHECK (quantity > 0),
    price numeric(12,2) NOT NULL CHECK (price >= 0),
    line_total numeric(12,2) GENERATED ALWAYS AS (quantity * price) STORED
    );
CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product ON order_items(product_id);

-- 16) VOUCHER_REDEMPTIONS
CREATE TABLE IF NOT EXISTS voucher_redemptions (
                                                   id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    voucher_id uuid NOT NULL REFERENCES vouchers(id) ON DELETE CASCADE,
    order_id uuid NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    customer_id uuid REFERENCES customers(id),
    redeemed_at timestamptz NOT NULL DEFAULT now(),
    discount_amount numeric(12,2) CHECK (discount_amount IS NULL OR discount_amount >= 0),
    UNIQUE (voucher_id, order_id)
    );
CREATE INDEX IF NOT EXISTS idx_redemptions_voucher ON voucher_redemptions(voucher_id);
CREATE INDEX IF NOT EXISTS idx_redemptions_customer ON voucher_redemptions(customer_id);
CREATE INDEX IF NOT EXISTS idx_redemptions_order ON voucher_redemptions(order_id);

-- 17) PAYMENTS ENUM
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'payment_status_enum') THEN
CREATE TYPE payment_status_enum AS ENUM (
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
END IF;
END$$;

-- 18) PAYMENTS
CREATE TABLE IF NOT EXISTS payments (
                                        id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id uuid NOT NULL UNIQUE REFERENCES orders(id),
    provider varchar(80) NOT NULL,
    payload jsonb,
    transaction_ref varchar(160),
    status payment_status_enum NOT NULL DEFAULT 'pending',
    amount numeric(12,2) NOT NULL CHECK (amount >= 0),
    created_at timestamptz NOT NULL DEFAULT now(),
    paid_at timestamptz,
    refunded_at timestamptz
    );
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);
CREATE INDEX IF NOT EXISTS idx_payments_txref ON payments(transaction_ref);