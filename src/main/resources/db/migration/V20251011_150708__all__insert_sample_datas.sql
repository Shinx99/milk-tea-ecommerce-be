-- Migration: all - insert_sample_datas
-- Created: Sat Oct 11 03:07:08 PM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:
BEGIN;

-- Bật pgcrypto để dùng crypt()/gen_salt()
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1) Roles
INSERT INTO roles (role, description) VALUES
 ('admin','Quản trị'),
 ('staff','Nhân viên'),
 ('customer','Khách')
ON CONFLICT (role) DO UPDATE SET description = EXCLUDED.description;

-- 2) Customers
INSERT INTO customers (email, password_hash, phone, fullname) VALUES
 ('mt.alice@example.com', crypt('Passw0rd!', gen_salt('bf')), '0902000001','Alice MilkTea'),
 ('mt.bob@example.com',   crypt('Passw0rd!', gen_salt('bf')), '0902000002','Bob MilkTea'),
 ('mt.carol@example.com', crypt('Passw0rd!', gen_salt('bf')), '0902000003','Carol MilkTea'),
 ('mt.dave@example.com',  crypt('Passw0rd!', gen_salt('bf')), '0902000004','Dave MilkTea'),
 ('mt.erin@example.com',  crypt('Passw0rd!', gen_salt('bf')), '0902000005','Erin MilkTea')
ON CONFLICT (email) DO UPDATE
SET phone = EXCLUDED.phone, fullname = EXCLUDED.fullname;

-- 3) Users (map vào customers/roles hiện có)
INSERT INTO users (email, password_hash, role_id, customer_id)
SELECT 'admin@milktea.local', crypt('Admin#123', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='admin'),
       (SELECT id FROM customers WHERE email='mt.alice@example.com')
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='admin@milktea.local');

INSERT INTO users (email, password_hash, role_id, customer_id)
SELECT 'staff@milktea.local', crypt('Staff#1', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='staff'),
       (SELECT id FROM customers WHERE email='mt.bob@example.com')
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='staff@milktea.local');

INSERT INTO users (email, password_hash, role_id, customer_id)
SELECT 'user1@milktea.local', crypt('User#1', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       (SELECT id FROM customers WHERE email='mt.carol@example.com')
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='user1@milktea.local');

INSERT INTO users (email, password_hash, role_id, customer_id)
SELECT 'user2@milktea.local', crypt('User#2', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       (SELECT id FROM customers WHERE email='mt.dave@example.com')
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='user2@milktea.local');

INSERT INTO users (email, password_hash, role_id, customer_id)
SELECT 'user3@milktea.local', crypt('User#3', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       (SELECT id FROM customers WHERE email='mt.erin@example.com')
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='user3@milktea.local');

-- 4) Categories (cha)
INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT NULL, 'Milk Tea', 1, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories WHERE category_name='Milk Tea' AND parent_id IS NULL
);

INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT NULL, 'Fruit Tea', 2, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories WHERE category_name='Fruit Tea' AND parent_id IS NULL
);

-- 4b) Categories (con)
INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Milk Tea' AND parent_id IS NULL),
       'Brown Sugar Series', 1, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories
  WHERE category_name='Brown Sugar Series'
    AND parent_id = (SELECT id FROM categories WHERE category_name='Milk Tea' AND parent_id IS NULL)
);

INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Milk Tea' AND parent_id IS NULL),
       'Classic Series', 2, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories
  WHERE category_name='Classic Series'
    AND parent_id = (SELECT id FROM categories WHERE category_name='Milk Tea' AND parent_id IS NULL)
);

INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Fruit Tea' AND parent_id IS NULL),
       'Cheese Foam', 1, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories
  WHERE category_name='Cheese Foam'
    AND parent_id = (SELECT id FROM categories WHERE category_name='Fruit Tea' AND parent_id IS NULL)
);

-- 5) Products
INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Classic Series'),
       'Classic Milk Tea','Trà sữa truyền thống',200,39000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Classic Milk Tea');

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Classic Series'),
       'Jasmine Milk Tea','Trà lài sữa',150,42000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Jasmine Milk Tea');

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Classic Series'),
       'Oolong Milk Tea','Trà ô long sữa',120,45000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Oolong Milk Tea');

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Brown Sugar Series'),
       'Brown Sugar Milk Tea','Trà sữa đường đen',180,49000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Brown Sugar Milk Tea');

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Brown Sugar Series'),
       'Brown Sugar Fresh Milk','Sữa tươi đường đen',160,52000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Brown Sugar Fresh Milk');

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Brown Sugar Series'),
       'Brown Sugar Pearl Milk Tea','Đường đen + trân châu',140,52000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Brown Sugar Pearl Milk Tea');

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Cheese Foam'),
       'Cheese Foam Green Tea','Trà xanh kem cheese',130,55000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Cheese Foam Green Tea');

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Cheese Foam'),
       'Cheese Foam Black Tea','Hồng trà kem cheese',110,55000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Cheese Foam Black Tea');

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Fruit Tea'),
       'Peach Tea','Trà đào miếng',170,45000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Peach Tea');

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE category_name='Fruit Tea'),
       'Lychee Tea','Trà vải miếng',170,45000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Lychee Tea');

-- 6) Images (Cloudinary metadata demo)
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Classic Milk Tea'),0,true,'mt1','menu/classic_milk_tea','image','upload',1700100001,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100001/menu/classic_milk_tea.jpg','Classic Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/classic_milk_tea');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Jasmine Milk Tea'),0,true,'mt2','menu/jasmine_milk_tea','image','upload',1700100002,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100002/menu/jasmine_milk_tea.jpg','Jasmine Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/jasmine_milk_tea');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Oolong Milk Tea'),0,true,'mt3','menu/oolong_milk_tea','image','upload',1700100003,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100003/menu/oolong_milk_tea.jpg','Oolong Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/oolong_milk_tea');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Brown Sugar Milk Tea'),0,true,'mt4','menu/brown_sugar_milk_tea','image','upload',1700100004,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100004/menu/brown_sugar_milk_tea.jpg','Brown Sugar Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/brown_sugar_milk_tea');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Brown Sugar Fresh Milk'),0,true,'mt5','menu/brown_sugar_fresh_milk','image','upload',1700100005,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100005/menu/brown_sugar_fresh_milk.jpg','Brown Sugar Fresh Milk'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/brown_sugar_fresh_milk');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Brown Sugar Pearl Milk Tea'),0,true,'mt6','menu/brown_sugar_pearl_milk_tea','image','upload',1700100006,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100006/menu/brown_sugar_pearl_milk_tea.jpg','Brown Sugar Pearl Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/brown_sugar_pearl_milk_tea');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Cheese Foam Green Tea'),0,true,'mt7','menu/cheese_foam_green_tea','image','upload',1700100007,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100007/menu/cheese_foam_green_tea.jpg','Cheese Foam Green Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/cheese_foam_green_tea');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Cheese Foam Black Tea'),0,true,'mt8','menu/cheese_foam_black_tea','image','upload',1700100008,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100008/menu/cheese_foam_black_tea.jpg','Cheese Foam Black Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/cheese_foam_black_tea');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Peach Tea'),0,true,'mt9','menu/peach_tea','image','upload',1700100009,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100009/menu/peach_tea.jpg','Peach Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/peach_tea');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Lychee Tea'),0,true,'mt10','menu/lychee_tea','image','upload',1700100010,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100010/menu/lychee_tea.jpg','Lychee Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/lychee_tea');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Classic Milk Tea'),1,false,'mt11','menu/classic_milk_tea_side','image','upload',1700100011,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100011/menu/classic_milk_tea_side.jpg','Classic Milk Tea angle'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/classic_milk_tea_side');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE name='Brown Sugar Milk Tea'),1,false,'mt12','menu/brown_sugar_milk_tea_side','image','upload',1700100012,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100012/menu/brown_sugar_milk_tea_side.jpg','Brown Sugar Milk Tea angle'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/brown_sugar_milk_tea_side');

-- 7) Vouchers
INSERT INTO vouchers (code, discount_type, number, start_at, expired_at, is_active)
VALUES ('MT10', 'percent', 10, now(), now() + interval '30 days', true)
ON CONFLICT (code) DO UPDATE
SET discount_type = EXCLUDED.discount_type,
    number = EXCLUDED.number,
    start_at = LEAST(vouchers.start_at, EXCLUDED.start_at),
    expired_at = GREATEST(vouchers.expired_at, EXCLUDED.expired_at),
    is_active = true;

INSERT INTO vouchers (code, discount_type, number, start_at, expired_at, is_active)
VALUES ('MTFREESHIP', 'shipping', 0, now(), now() + interval '30 days', true)
ON CONFLICT (code) DO UPDATE
SET is_active = true,
    expired_at = GREATEST(vouchers.expired_at, EXCLUDED.expired_at);

-- 8) Orders
INSERT INTO orders (customer_id, status, placed_at, description, subtotal, discount_total, tax_total, shipping_fee, total)
SELECT (SELECT id FROM customers WHERE email='mt.alice@example.com'),'confirmed', now() - interval '1 day','Order MT A', 94000, 9400, 0, 0, 84600
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE description='Order MT A');

INSERT INTO orders (customer_id, status, placed_at, description, subtotal, discount_total, tax_total, shipping_fee, total)
SELECT (SELECT id FROM customers WHERE email='mt.bob@example.com'),'pending', now(),'Order MT B', 52000, 0, 0, 0, 52000
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE description='Order MT B');

-- 9) Order items
INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT (SELECT id FROM orders WHERE description='Order MT A'),
       (SELECT id FROM products WHERE name='Classic Milk Tea'),
       1, 39000
WHERE NOT EXISTS (
  SELECT 1 FROM order_items
  WHERE order_id=(SELECT id FROM orders WHERE description='Order MT A')
    AND product_id=(SELECT id FROM products WHERE name='Classic Milk Tea')
);

INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT (SELECT id FROM orders WHERE description='Order MT A'),
       (SELECT id FROM products WHERE name='Jasmine Milk Tea'),
       1, 42000
WHERE NOT EXISTS (
  SELECT 1 FROM order_items
  WHERE order_id=(SELECT id FROM orders WHERE description='Order MT A')
    AND product_id=(SELECT id FROM products WHERE name='Jasmine Milk Tea')
);

INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT (SELECT id FROM orders WHERE description='Order MT B'),
       (SELECT id FROM products WHERE name='Brown Sugar Fresh Milk'),
       1, 52000
WHERE NOT EXISTS (
  SELECT 1 FROM order_items
  WHERE order_id=(SELECT id FROM orders WHERE description='Order MT B')
    AND product_id=(SELECT id FROM products WHERE name='Brown Sugar Fresh Milk')
);

-- 10) Carts (mỗi customer 1 hàng demo)
INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE email='mt.alice@example.com'),
       (SELECT id FROM products  WHERE name='Classic Milk Tea'), 2, 39000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts WHERE customer_id=(SELECT id FROM customers WHERE email='mt.alice@example.com')
    AND product_id=(SELECT id FROM products WHERE name='Classic Milk Tea')
    AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE email='mt.bob@example.com'),
       (SELECT id FROM products  WHERE name='Brown Sugar Fresh Milk'), 1, 52000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts WHERE customer_id=(SELECT id FROM customers WHERE email='mt.bob@example.com')
    AND product_id=(SELECT id FROM products WHERE name='Brown Sugar Fresh Milk')
    AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE email='mt.carol@example.com'),
       (SELECT id FROM products  WHERE name='Peach Tea'), 1, 45000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts WHERE customer_id=(SELECT id FROM customers WHERE email='mt.carol@example.com')
    AND product_id=(SELECT id FROM products WHERE name='Peach Tea')
    AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE email='mt.dave@example.com'),
       (SELECT id FROM products  WHERE name='Oolong Milk Tea'), 1, 45000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts WHERE customer_id=(SELECT id FROM customers WHERE email='mt.dave@example.com')
    AND product_id=(SELECT id FROM products WHERE name='Oolong Milk Tea')
    AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE email='mt.erin@example.com'),
       (SELECT id FROM products  WHERE name='Lychee Tea'), 1, 45000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts WHERE customer_id=(SELECT id FROM customers WHERE email='mt.erin@example.com')
    AND product_id=(SELECT id FROM products WHERE name='Lychee Tea')
    AND status='active'
);

-- 11) Voucher products
INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM products WHERE name='Classic Milk Tea')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND product_id=(SELECT id FROM products WHERE name='Classic Milk Tea')
);

INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM products WHERE name='Jasmine Milk Tea')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND product_id=(SELECT id FROM products WHERE name='Jasmine Milk Tea')
);

INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM products WHERE name='Brown Sugar Milk Tea')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND product_id=(SELECT id FROM products WHERE name='Brown Sugar Milk Tea')
);

INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MTFREESHIP'),
       (SELECT id FROM products WHERE name='Brown Sugar Fresh Milk')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MTFREESHIP')
    AND product_id=(SELECT id FROM products WHERE name='Brown Sugar Fresh Milk')
);

-- 12) Voucher customers
INSERT INTO voucher_customers (voucher_id, customer_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM customers WHERE email='mt.alice@example.com')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_customers
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND customer_id=(SELECT id FROM customers WHERE email='mt.alice@example.com')
);

INSERT INTO voucher_customers (voucher_id, customer_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM customers WHERE email='mt.bob@example.com')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_customers
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND customer_id=(SELECT id FROM customers WHERE email='mt.bob@example.com')
);

INSERT INTO voucher_customers (voucher_id, customer_id)
SELECT (SELECT id FROM vouchers WHERE code='MTFREESHIP'),
       (SELECT id FROM customers WHERE email='mt.carol@example.com')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_customers
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MTFREESHIP')
    AND customer_id=(SELECT id FROM customers WHERE email='mt.carol@example.com')
);

-- 13) Voucher redemptions
INSERT INTO voucher_redemptions (voucher_id, order_id, customer_id, discount_amount)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM orders WHERE description='Order MT A'),
       (SELECT id FROM customers WHERE email='mt.alice@example.com'),
       9400
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_redemptions
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND order_id=(SELECT id FROM orders WHERE description='Order MT A')
);

-- 14) PAYMENTS (mỗi order 1 bản ghi)

-- Order MT A: đã thanh toán bằng MOMO
INSERT INTO payments (order_id, provider, payload, transaction_ref, status, amount, created_at, paid_at)
SELECT (SELECT id FROM orders WHERE description='Order MT A'),
       'MOMO', '{}'::jsonb, 'MOMO-MTA-0001', 'paid', 84600,
       now() - interval '1 day', now() - interval '1 day'
ON CONFLICT (order_id) DO UPDATE
SET provider        = EXCLUDED.provider,
    payload         = EXCLUDED.payload,
    transaction_ref = EXCLUDED.transaction_ref,
    status          = EXCLUDED.status,
    amount          = EXCLUDED.amount,
    -- giữ thời điểm tạo sớm nhất nếu đã có
    created_at      = LEAST(payments.created_at, EXCLUDED.created_at),
    paid_at         = COALESCE(EXCLUDED.paid_at, payments.paid_at);

-- Order MT B: một payment COD (pending)
INSERT INTO payments (order_id, provider, payload, transaction_ref, status, amount, created_at, paid_at)
SELECT (SELECT id FROM orders WHERE description='Order MT B'),
       'COD', '{}'::jsonb, 'COD-MTB-0001', 'pending', 52000,
       now(), NULL
ON CONFLICT (order_id) DO UPDATE
SET provider        = EXCLUDED.provider,
    payload         = EXCLUDED.payload,
    transaction_ref = EXCLUDED.transaction_ref,
    status          = EXCLUDED.status,
    amount          = EXCLUDED.amount,
    created_at      = LEAST(payments.created_at, EXCLUDED.created_at),
    paid_at         = COALESCE(EXCLUDED.paid_at, payments.paid_at);


-- 15) Addresses (mặc định mỗi customer 1 địa chỉ)
INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE email='mt.alice@example.com'),
       '12A','Le Loi','Ben Nghe','1','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE email='mt.alice@example.com') AND is_default=true
);

INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE email='mt.bob@example.com'),
       '22','Nguyen Hue','Ben Nghe','1','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE email='mt.bob@example.com') AND is_default=true
);

INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE email='mt.carol@example.com'),
       '8','Tran Hung Dao','Pham Ngu Lao','1','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE email='mt.carol@example.com') AND is_default=true
);

INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE email='mt.dave@example.com'),
       '101','Vo Van Tan','Vo Thi Sau','3','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE email='mt.dave@example.com') AND is_default=true
);

INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE email='mt.erin@example.com'),
       '5B','Dinh Tien Hoang','Da Kao','1','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE email='mt.erin@example.com') AND is_default=true
);

COMMIT;

