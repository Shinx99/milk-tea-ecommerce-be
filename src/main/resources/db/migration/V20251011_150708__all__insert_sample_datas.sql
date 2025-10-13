BEGIN;

-- Có thể giữ, vô hại nếu đã enable ở migration đầu
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1) ROLES
INSERT INTO roles (role, description) VALUES
('admin', 'Quản trị'),
('staff', 'Nhân viên'),
('customer', 'Khach hang')
ON CONFLICT (role) DO UPDATE SET description = EXCLUDED.description;

-- 2) CUSTOMERS
INSERT INTO customers (phone, fullname)
SELECT '0902000001','Alice MilkTea'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE phone='0902000001');

INSERT INTO customers (phone, fullname)
SELECT '0902000002','Bob MilkTea'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE phone='0902000002');

INSERT INTO customers (phone, fullname)
SELECT '0902000003','Carol MilkTea'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE phone='0902000003');

INSERT INTO customers (phone, fullname)
SELECT '0902000004','Dave MilkTea'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE phone='0902000004');

INSERT INTO customers (phone, fullname)
SELECT '0902000005','Erin MilkTea'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE phone='0902000005');

-- 3) USERS
-- Admin: role admin, user hệ thống (customer_id NULL)
INSERT INTO users (email, password_hash, role_id, customer_id)
SELECT 'admin@milktea.local',
       crypt('Admin#123', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='admin'),
       NULL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='admin@milktea.local');

-- Staff: role staff, user hệ thống (customer_id NULL)
INSERT INTO users (email, password_hash, role_id, customer_id)
SELECT 'staff@milktea.local',
       crypt('Staff#1', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='staff'),
       NULL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='staff@milktea.local');

-- Customer1: role customer, gắn với customer '0902000003'
INSERT INTO users (email, password_hash, role_id, customer_id)
SELECT 'customer1@milktea.local',
       crypt('Customer#1', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       (SELECT id FROM customers WHERE phone='0902000003')
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='customer1@milktea.local');

-- Customer2: role customer, gắn với customer '0902000004'
INSERT INTO users (email, password_hash, role_id, customer_id)
SELECT 'customer2@milktea.local',
       crypt('Customer#2', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       (SELECT id FROM customers WHERE phone='0902000004')
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='customer2@milktea.local');

-- 4) CATEGORIES (cha)
INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT NULL, 'Milk Tea', 1, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Milk Tea')
);

INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT NULL, 'Fruit Tea', 2, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Fruit Tea')
);

-- 4b) CATEGORIES (con)
INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Milk Tea')),
       'Brown Sugar Series', 1, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories
  WHERE lower(category_name)=lower('Brown Sugar Series')
    AND parent_id = (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Milk Tea'))
);

INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Milk Tea')),
       'Classic Series', 2, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories
  WHERE lower(category_name)=lower('Classic Series')
    AND parent_id = (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Milk Tea'))
);

INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Fruit Tea')),
       'Cheese Foam', 1, true
WHERE NOT EXISTS (
  SELECT 1 FROM categories
  WHERE lower(category_name)=lower('Cheese Foam')
    AND parent_id = (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Fruit Tea'))
);

-- 5) PRODUCTS
INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Classic Series')),
       'Classic Milk Tea','Trà sữa truyền thống',200,39000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Classic Milk Tea'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Classic Series')),
       'Jasmine Milk Tea','Trà lài sữa',150,42000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Jasmine Milk Tea'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Classic Series')),
       'Oolong Milk Tea','Trà ô long sữa',120,45000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Oolong Milk Tea'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Brown Sugar Series')),
       'Brown Sugar Milk Tea','Trà sữa đường đen',180,49000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Brown Sugar Milk Tea'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Brown Sugar Series')),
       'Brown Sugar Fresh Milk','Sữa tươi đường đen',160,52000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Brown Sugar Fresh Milk'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Brown Sugar Series')),
       'Brown Sugar Pearl Milk Tea','Đường đen + trân châu',140,52000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Brown Sugar Pearl Milk Tea'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Cheese Foam')),
       'Cheese Foam Green Tea','Trà xanh kem cheese',130,55000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Cheese Foam Green Tea'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Cheese Foam')),
       'Cheese Foam Black Tea','Hồng trà kem cheese',110,55000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Cheese Foam Black Tea'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Fruit Tea')),
       'Peach Tea','Trà đào miếng',170,45000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Peach Tea'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Fruit Tea')),
       'Lychee Tea','Trà vải miếng',170,45000,true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Lychee Tea'));

-- 6) IMAGES (Cloudinary metadata demo)
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Classic Milk Tea')),0,true,'mt1','menu/classic_milk_tea','image','upload',1700100001,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100001/menu/classic_milk_tea.jpg','Classic Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/classic_milk_tea' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Jasmine Milk Tea')),0,true,'mt2','menu/jasmine_milk_tea','image','upload',1700100002,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100002/menu/jasmine_milk_tea.jpg','Jasmine Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/jasmine_milk_tea' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Oolong Milk Tea')),0,true,'mt3','menu/oolong_milk_tea','image','upload',1700100003,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100003/menu/oolong_milk_tea.jpg','Oolong Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/oolong_milk_tea' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Milk Tea')),0,true,'mt4','menu/brown_sugar_milk_tea','image','upload',1700100004,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100004/menu/brown_sugar_milk_tea.jpg','Brown Sugar Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/brown_sugar_milk_tea' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Fresh Milk')),0,true,'mt5','menu/brown_sugar_fresh_milk','image','upload',1700100005,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100005/menu/brown_sugar_fresh_milk.jpg','Brown Sugar Fresh Milk'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/brown_sugar_fresh_milk' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Pearl Milk Tea')),0,true,'mt6','menu/brown_sugar_pearl_milk_tea','image','upload',1700100006,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100006/menu/brown_sugar_pearl_milk_tea.jpg','Brown Sugar Pearl Milk Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/brown_sugar_pearl_milk_tea' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Cheese Foam Green Tea')),0,true,'mt7','menu/cheese_foam_green_tea','image','upload',1700100007,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100007/menu/cheese_foam_green_tea.jpg','Cheese Foam Green Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/cheese_foam_green_tea' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Cheese Foam Black Tea')),0,true,'mt8','menu/cheese_foam_black_tea','image','upload',1700100008,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100008/menu/cheese_foam_black_tea.jpg','Cheese Foam Black Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/cheese_foam_black_tea' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Peach Tea')),0,true,'mt9','menu/peach_tea','image','upload',1700100009,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100009/menu/peach_tea.jpg','Peach Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/peach_tea' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Lychee Tea')),0,true,'mt10','menu/lychee_tea','image','upload',1700100010,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100010/menu/lychee_tea.jpg','Lychee Tea'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/lychee_tea' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Classic Milk Tea')),1,false,'mt11','menu/classic_milk_tea_side','image','upload',1700100011,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100011/menu/classic_milk_tea_side.jpg','Classic Milk Tea angle'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/classic_milk_tea_side' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Milk Tea')),1,false,'mt12','menu/brown_sugar_milk_tea_side','image','upload',1700100012,'jpg',800,800,120000,'https://res.cloudinary.com/demo/image/upload/v1700100012/menu/brown_sugar_milk_tea_side.jpg','Brown Sugar Milk Tea angle'
WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/brown_sugar_milk_tea_side' AND resource_type='image' AND delivery_type='upload');

-- 7) VOUCHERS
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

-- 8) ORDERS
INSERT INTO orders (customer_id, status, placed_at, description, subtotal, discount_total, tax_total, shipping_fee, total)
SELECT (SELECT id FROM customers WHERE phone='0902000001'),'confirmed', now() - interval '1 day','Order MT A', 94000, 9400, 0, 0, 84600
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE description='Order MT A');

INSERT INTO orders (customer_id, status, placed_at, description, subtotal, discount_total, tax_total, shipping_fee, total)
SELECT (SELECT id FROM customers WHERE phone='0902000002'),'pending', now(),'Order MT B', 52000, 0, 0, 0, 52000
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE description='Order MT B');

-- 9) ORDER_ITEMS
INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT (SELECT id FROM orders   WHERE description='Order MT A'),
       (SELECT id FROM products WHERE lower(name)=lower('Classic Milk Tea')),
       1, 39000
WHERE NOT EXISTS (
  SELECT 1 FROM order_items
  WHERE order_id=(SELECT id FROM orders WHERE description='Order MT A')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Classic Milk Tea'))
);

INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT (SELECT id FROM orders   WHERE description='Order MT A'),
       (SELECT id FROM products WHERE lower(name)=lower('Jasmine Milk Tea')),
       1, 42000
WHERE NOT EXISTS (
  SELECT 1 FROM order_items
  WHERE order_id=(SELECT id FROM orders WHERE description='Order MT A')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Jasmine Milk Tea'))
);

INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT (SELECT id FROM orders   WHERE description='Order MT B'),
       (SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Fresh Milk')),
       1, 52000
WHERE NOT EXISTS (
  SELECT 1 FROM order_items
  WHERE order_id=(SELECT id FROM orders WHERE description='Order MT B')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Fresh Milk'))
);

-- 10) CARTS
INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000001'),
       (SELECT id FROM products  WHERE lower(name)=lower('Classic Milk Tea')), 2, 39000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts
  WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000001')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Classic Milk Tea'))
    AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000002'),
       (SELECT id FROM products  WHERE lower(name)=lower('Brown Sugar Fresh Milk')), 1, 52000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts
  WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000002')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Fresh Milk'))
    AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000003'),
       (SELECT id FROM products  WHERE lower(name)=lower('Peach Tea')), 1, 45000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts
  WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000003')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Peach Tea'))
    AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000004'),
       (SELECT id FROM products  WHERE lower(name)=lower('Oolong Milk Tea')), 1, 45000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts
  WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000004')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Oolong Milk Tea'))
    AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000005'),
       (SELECT id FROM products  WHERE lower(name)=lower('Lychee Tea')), 1, 45000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
  SELECT 1 FROM carts
  WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000005')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Lychee Tea'))
    AND status='active'
);

-- 11) VOUCHER_PRODUCTS
INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM products WHERE lower(name)=lower('Classic Milk Tea'))
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Classic Milk Tea'))
);

INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM products WHERE lower(name)=lower('Jasmine Milk Tea'))
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Jasmine Milk Tea'))
);

INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Milk Tea'))
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Milk Tea'))
);

INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MTFREESHIP'),
       (SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Fresh Milk'))
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MTFREESHIP')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Brown Sugar Fresh Milk'))
);

-- 12) VOUCHER_CUSTOMERS (map theo phone)
INSERT INTO voucher_customers (voucher_id, customer_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM customers WHERE phone='0902000001')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_customers
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND customer_id=(SELECT id FROM customers WHERE phone='0902000001')
);

INSERT INTO voucher_customers (voucher_id, customer_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM customers WHERE phone='0902000002')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_customers
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND customer_id=(SELECT id FROM customers WHERE phone='0902000002')
);

INSERT INTO voucher_customers (voucher_id, customer_id)
SELECT (SELECT id FROM vouchers WHERE code='MTFREESHIP'),
       (SELECT id FROM customers WHERE phone='0902000003')
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_customers
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MTFREESHIP')
    AND customer_id=(SELECT id FROM customers WHERE phone='0902000003')
);

-- 13) VOUCHER_REDEMPTIONS
INSERT INTO voucher_redemptions (voucher_id, order_id, customer_id, discount_amount)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM orders WHERE description='Order MT A'),
       (SELECT id FROM customers WHERE phone='0902000001'),
       9400
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_redemptions
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND order_id=(SELECT id FROM orders WHERE description='Order MT A')
);

-- 14) PAYMENTS
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
    created_at      = LEAST(payments.created_at, EXCLUDED.created_at),
    paid_at         = COALESCE(EXCLUDED.paid_at, payments.paid_at);

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

-- 15) ADDRESSES (map theo phone)
INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE phone='0902000001'),
       '12A','Le Loi','Ben Nghe','1','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000001') AND is_default=true
);

INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE phone='0902000002'),
       '22','Nguyen Hue','Ben Nghe','1','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000002') AND is_default=true
);

INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE phone='0902000003'),
       '8','Tran Hung Dao','Pham Ngu Lao','1','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000003') AND is_default=true
);

INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE phone='0902000004'),
       '101','Vo Van Tan','Vo Thi Sau','3','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000004') AND is_default=true
);

INSERT INTO addresses (customer_id, number, street, ward, district, city, province, country, is_default, is_active)
SELECT (SELECT id FROM customers WHERE phone='0902000005'),
       '5B','Dinh Tien Hoang','Da Kao','1','Ho Chi Minh City','HCM','VN', true, true
WHERE NOT EXISTS (
  SELECT 1 FROM addresses WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000005') AND is_default=true
);

COMMIT;
