BEGIN;

-- Có thể giữ, vô hại nếu đã enable ở migration đầu
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Bật extension cho crypt()/gen_salt() nếu chưa có
-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1) ROLES
INSERT INTO roles (role, description) VALUES
                                          ('admin', 'Quản trị'),
                                          ('staff', 'Nhân viên'),
                                          ('customer', 'Khach hang')
    ON CONFLICT (role) DO UPDATE SET description = EXCLUDED.description;

-- 2) USERS (tạo trước để customers tham chiếu user_id)

-- Admin, Staff (không gắn customer)
INSERT INTO users (email, password_hash, role_id, is_active)
SELECT 'admin@milktea.local',
       crypt('Admin#123', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='admin'),
       true
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password_hash, role_id, is_active)
SELECT 'staff@milktea.local',
       crypt('Staff#1', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='staff'),
       true
    ON CONFLICT (email) DO NOTHING;

-- Customer users (mỗi email ứng với một khách hàng sau này)
INSERT INTO users (email, password_hash, role_id, is_active)
SELECT 'customer1@milktea.local',
       crypt('Customer#1', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       true
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password_hash, role_id, is_active)
SELECT 'customer2@milktea.local',
       crypt('Customer#2', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       true
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password_hash, role_id, is_active)
SELECT 'customer3@milktea.local',
       crypt('Customer#3', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       true
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password_hash, role_id, is_active)
SELECT 'customer4@milktea.local',
       crypt('Customer#4', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       true
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password_hash, role_id, is_active)
SELECT 'customer5@milktea.local',
       crypt('Customer#5', gen_salt('bf')),
       (SELECT id FROM roles WHERE role='customer'),
       true
    ON CONFLICT (email) DO NOTHING;

-- 3) CUSTOMERS (sau users, tham chiếu user_id theo email)

INSERT INTO customers (phone, fullname, user_id)
SELECT '0902000001','Alice MilkTea',
       (SELECT id FROM users WHERE email='customer1@milktea.local')
    ON CONFLICT (phone) DO UPDATE
                               SET fullname = EXCLUDED.fullname,
                               user_id  = EXCLUDED.user_id;

INSERT INTO customers (phone, fullname, user_id)
SELECT '0902000002','Bob MilkTea',
       (SELECT id FROM users WHERE email='customer2@milktea.local')
    ON CONFLICT (phone) DO UPDATE
                               SET fullname = EXCLUDED.fullname,
                               user_id  = EXCLUDED.user_id;

INSERT INTO customers (phone, fullname, user_id)
SELECT '0902000003','Carol MilkTea',
       (SELECT id FROM users WHERE email='customer3@milktea.local')
    ON CONFLICT (phone) DO UPDATE
                               SET fullname = EXCLUDED.fullname,
                               user_id  = EXCLUDED.user_id;

INSERT INTO customers (phone, fullname, user_id)
SELECT '0902000004','Dave MilkTea',
       (SELECT id FROM users WHERE email='customer4@milktea.local')
    ON CONFLICT (phone) DO UPDATE
                               SET fullname = EXCLUDED.fullname,
                               user_id  = EXCLUDED.user_id;

INSERT INTO customers (phone, fullname, user_id)
SELECT '0902000005','Erin MilkTea',
       (SELECT id FROM users WHERE email='customer5@milktea.local')
    ON CONFLICT (phone) DO UPDATE
                               SET fullname = EXCLUDED.fullname,
                               user_id  = EXCLUDED.user_id;


-- =============================================
-- 4) CATEGORIES (Danh mục cha)
-- =============================================
INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT NULL, 'Chè', 1, true
WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Chè')
);

INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT NULL, 'Trà trái cây', 2, true
WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Trà trái cây')
);

-- =============================================
-- 4b) CATEGORIES (Danh mục con)
-- =============================================
INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Chè')),
       'Chè Nóng', 1, true
WHERE NOT EXISTS (
    SELECT 1 FROM categories
    WHERE lower(category_name)=lower('Chè Nóng')
      AND parent_id = (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Chè'))
);

INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Chè')),
       'Chè Lạnh', 2, true
WHERE NOT EXISTS (
    SELECT 1 FROM categories
    WHERE lower(category_name)=lower('Chè Lạnh')
      AND parent_id = (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Chè'))
);

INSERT INTO categories (parent_id, category_name, sort_order, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Trà trái cây')),
       'Trà trái cây nhiệt đới', 1, true
WHERE NOT EXISTS (
    SELECT 1 FROM categories
    WHERE lower(category_name)=lower('Trà trái cây nhiệt đới')
      AND parent_id = (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Trà trái cây'))
);

-- =============================================
-- 5) PRODUCTS
-- =============================================
INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Chè')),
       'Chè Bưởi', 'Chè bưởi truyền thống', 200, 39000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Chè Bưởi'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Chè')),
       'Chè Đậu Đen', 'Chè đậu đen truyền thống', 150, 42000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Chè Đậu Đen'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Chè')),
       'Chè Đậu Đỏ', 'Chè đậu đỏ truyền thống', 120, 45000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Chè Đậu Đỏ'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Chè')),
       'Chè Đậu Xanh', 'Chè đậu xanh truyền thống', 180, 49000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Chè Đậu Xanh'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Chè')),
       'Chè Đậu Xanh Nước Cốt Dừa', 'Chè đậu xanh nước cốt dừa', 160, 52000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Chè Đậu Xanh Nước Cốt Dừa'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Chè')),
       'Chè Đậu Đỏ Nước Cốt Dừa', 'Chè đậu đỏ nước cốt dừa', 140, 52000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Chè Đậu Đỏ Nước Cốt Dừa'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Trà trái cây')),
       'Trà Bắp Kem Cheese', 'Trà bắp kem cheese', 130, 55000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Trà Bắp Kem Cheese'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Trà trái cây')),
       'Hồng Trà Kem Cheese', 'Hồng trà kem cheese', 110, 55000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Hồng Trà Kem Cheese'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Trà trái cây')),
       'Trà Đào', 'Trà đào miếng', 170, 45000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Trà Đào'));

INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE parent_id IS NULL AND lower(category_name)=lower('Trà trái cây')),
       'Trà Vải', 'Trà vải miếng', 170, 45000, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE lower(name)=lower('Trà Vải'));

-- =============================================
-- 6) IMAGES
-- =============================================

-- Chè Bưởi
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Bưởi')), 0, true,
       'mt1', 'menu/che_buoi', 'image', 'upload', 1700100001, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100001/menu/che_buoi.jpg', 'Chè Bưởi'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/che_buoi' AND resource_type='image' AND delivery_type='upload'
);

-- Chè Đậu Đen
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đen')), 0, true,
       'mt2', 'menu/che_dau_den', 'image', 'upload', 1700100002, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100002/menu/che_dau_den.jpg', 'Chè Đậu Đen'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/che_dau_den' AND resource_type='image' AND delivery_type='upload'
);

-- Chè Đậu Đỏ
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đỏ')), 0, true,
       'mt3', 'menu/che_dau_do', 'image', 'upload', 1700100003, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100003/menu/che_dau_do.jpg', 'Chè Đậu Đỏ'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/che_dau_do' AND resource_type='image' AND delivery_type='upload'
);

-- Chè Đậu Xanh
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Xanh')), 0, true,
       'mt4', 'menu/che_dau_xanh', 'image', 'upload', 1700100004, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100004/menu/che_dau_xanh.jpg', 'Chè Đậu Xanh'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/che_dau_xanh' AND resource_type='image' AND delivery_type='upload'
);

-- Chè Đậu Xanh Nước Cốt Dừa
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Xanh Nước Cốt Dừa')), 0, true,
       'mt5', 'menu/che_dau_xanh_nuoc_cot_dua', 'image', 'upload', 1700100005, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100005/menu/che_dau_xanh_nuoc_cot_dua.jpg', 'Chè Đậu Xanh Nước Cốt Dừa'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/che_dau_xanh_nuoc_cot_dua' AND resource_type='image' AND delivery_type='upload'
);

-- Chè Đậu Đỏ Nước Cốt Dừa
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đỏ Nước Cốt Dừa')), 0, true,
       'mt6', 'menu/che_dau_do_nuoc_cot_dua', 'image', 'upload', 1700100006, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100006/menu/che_dau_do_nuoc_cot_dua.jpg', 'Chè Đậu Đỏ Nước Cốt Dừa'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/che_dau_do_nuoc_cot_dua' AND resource_type='image' AND delivery_type='upload'
);

-- Trà Bắp Kem Cheese
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà Bắp Kem Cheese')), 0, true,
       'mt7', 'menu/tra_bap_kem_cheese', 'image', 'upload', 1700100007, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100007/menu/tra_bap_kem_cheese.jpg', 'Trà Bắp Kem Cheese'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/tra_bap_kem_cheese' AND resource_type='image' AND delivery_type='upload'
);

-- Hồng Trà Kem Cheese
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Hồng Trà Kem Cheese')), 0, true,
       'mt8', 'menu/hong_tra_kem_cheese', 'image', 'upload', 1700100008, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100008/menu/hong_tra_kem_cheese.jpg', 'Hồng Trà Kem Cheese'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/hong_tra_kem_cheese' AND resource_type='image' AND delivery_type='upload'
);

-- Trà Đào
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà Đào')), 0, true,
       'mt9', 'menu/tra_dao', 'image', 'upload', 1700100009, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100009/menu/tra_dao.jpg', 'Trà Đào'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/tra_dao' AND resource_type='image' AND delivery_type='upload'
);

-- Trà Vải
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà Vải')), 0, true,
       'mt10', 'menu/tra_vai', 'image', 'upload', 1700100010, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/demo/image/upload/v1700100010/menu/tra_vai.jpg', 'Trà Vải'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/tra_vai' AND resource_type='image' AND delivery_type='upload'
);


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

-- =============================================
-- 9) ORDER_ITEMS (Đã sửa tên món cho khớp với bảng Products tiếng Việt)
-- =============================================
-- Map 'Classic Milk Tea' -> 'Hồng Trà Kem Cheese'
INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT (SELECT id FROM orders WHERE description='Order MT A'),
       (SELECT id FROM products WHERE lower(name)=lower('Hồng Trà Kem Cheese')),
       1, 55000
WHERE NOT EXISTS (
  SELECT 1 FROM order_items
  WHERE order_id=(SELECT id FROM orders WHERE description='Order MT A')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Hồng Trà Kem Cheese'))
);

-- Map 'Jasmine Milk Tea' -> 'Trà Bắp Kem Cheese'
INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT (SELECT id FROM orders WHERE description='Order MT A'),
       (SELECT id FROM products WHERE lower(name)=lower('Trà Bắp Kem Cheese')),
       1, 55000
WHERE NOT EXISTS (
  SELECT 1 FROM order_items
  WHERE order_id=(SELECT id FROM orders WHERE description='Order MT A')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Trà Bắp Kem Cheese'))
);

-- Map 'Brown Sugar Fresh Milk' -> 'Chè Đậu Đỏ Nước Cốt Dừa' (ví dụ)
INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT (SELECT id FROM orders WHERE description='Order MT B'),
       (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đỏ Nước Cốt Dừa')),
       1, 52000
WHERE NOT EXISTS (
  SELECT 1 FROM order_items
  WHERE order_id=(SELECT id FROM orders WHERE description='Order MT B')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đỏ Nước Cốt Dừa'))
);

-- =============================================
-- 10) CARTS (Đã sửa tên món)
-- =============================================
INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000001'),
       (SELECT id FROM products  WHERE lower(name)=lower('Hồng Trà Kem Cheese')), 2, 55000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
    SELECT 1 FROM carts
    WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000001')
  AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Hồng Trà Kem Cheese'))
  AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000002'),
       (SELECT id FROM products  WHERE lower(name)=lower('Chè Đậu Đỏ Nước Cốt Dừa')), 1, 52000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
    SELECT 1 FROM carts
    WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000002')
  AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đỏ Nước Cốt Dừa'))
  AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000003'),
       (SELECT id FROM products  WHERE lower(name)=lower('Trà Đào')), 1, 45000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
    SELECT 1 FROM carts
    WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000003')
  AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Trà Đào'))
  AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000004'),
       (SELECT id FROM products  WHERE lower(name)=lower('Chè Bưởi')), 1, 39000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
    SELECT 1 FROM carts
    WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000004')
  AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Chè Bưởi'))
  AND status='active'
);

INSERT INTO carts (customer_id, product_id, quantity, price, status, expires_at)
SELECT (SELECT id FROM customers WHERE phone='0902000005'),
       (SELECT id FROM products  WHERE lower(name)=lower('Trà Vải')), 1, 45000, 'active', now() + interval '1 day'
WHERE NOT EXISTS (
    SELECT 1 FROM carts
    WHERE customer_id=(SELECT id FROM customers WHERE phone='0902000005')
  AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Trà Vải'))
  AND status='active'
);

-- =============================================
-- 11) VOUCHER_PRODUCTS (Đã sửa tên món)
-- =============================================
-- Voucher MT10 áp dụng cho Hồng Trà Kem Cheese
INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM products WHERE lower(name)=lower('Hồng Trà Kem Cheese'))
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Hồng Trà Kem Cheese'))
);

-- Voucher MT10 áp dụng cho Trà Bắp Kem Cheese
INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM products WHERE lower(name)=lower('Trà Bắp Kem Cheese'))
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Trà Bắp Kem Cheese'))
);

-- Voucher MT10 áp dụng cho Chè Đậu Đen
INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MT10'),
       (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đen'))
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MT10')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đen'))
);

-- Voucher MTFREESHIP áp dụng cho Chè Đậu Đỏ Nước Cốt Dừa
INSERT INTO voucher_products (voucher_id, product_id)
SELECT (SELECT id FROM vouchers WHERE code='MTFREESHIP'),
       (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đỏ Nước Cốt Dừa'))
WHERE NOT EXISTS (
  SELECT 1 FROM voucher_products
  WHERE voucher_id=(SELECT id FROM vouchers WHERE code='MTFREESHIP')
    AND product_id=(SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đỏ Nước Cốt Dừa'))
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