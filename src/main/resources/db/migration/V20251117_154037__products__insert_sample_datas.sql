-- Migration: products - insert_sample_datas
-- Created: Mon Nov 17 03:40:37 PM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:
-- Trà sữa Ô Long
insert into products(category_id, name, description, quantity, price, is_active)
select (select id from categories where lower(category_name)=lower('Trà sữa')),
'Trà sữa Ô Long', 'Trà sữa thiên nhiên', 200, 35000, true
where not exists(
    select id from products where lower(name)=lower('Trà sữa Ô Long')
);

-- Trà sữa Lài
insert into products(category_id, name, description, quantity, price, is_active)
select (select id from categories where lower(category_name)=lower('Trà sữa')),
'Trà sữa Lài', 'Trà sữa thiên nhiên', 200, 38000, true
where not exists(
    select id from products where lower(name)=lower('Trà sữa Lài')
);

-- Trà sữa Nguyên Lá
insert into products(category_id, name, description, quantity, price, is_active)
select (select id from categories where lower(category_name)=lower('Trà sữa')),
'Trà sữa Nguyên Lá', 'Trà sữa thiên nhiên', 200, 38000, true
where not exists(
    select id from products where lower(name)=lower('Trà sữa Nguyên Lá')
);

-- Trà sữa Trân Châu Đường Đen
insert into products(category_id, name, description, quantity, price, is_active)
select (select id from categories where lower(category_name)=lower('Trà sữa')),
'Trà sữa Trân Châu Đường Đen', 'Trà sữa trân châu', 200, 38000, true
where not exists(
    select id from products where lower(name)=lower('Trà sữa Trân Châu Đường Đen')
);

-- Trà sữa Thái Xanh
INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Trà sữa')),
'Trà sữa Thái Xanh', 'Trà sữa Thái Xanh đậm đà', 200, 35000, true
WHERE NOT EXISTS (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Thái Xanh'));

-- Trà sữa Thái Đỏ
INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Trà sữa')),
'Trà sữa Thái Đỏ', 'Trà sữa Thái Đỏ truyền thống', 200, 35000, true
WHERE NOT EXISTS (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Thái Đỏ'));

-- Trà sữa Khoai Môn
INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Trà sữa')),
'Trà sữa Khoai Môn', 'Trà sữa hương khoai môn thơm béo', 200, 38000, true
WHERE NOT EXISTS (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Khoai Môn'));

-- Trà sữa Matcha
INSERT INTO products (category_id, name, description, quantity, price, is_active)
SELECT (SELECT id FROM categories WHERE lower(category_name)=lower('Trà sữa')),
'Trà sữa Matcha', 'Trà sữa Matcha Nhật Bản', 200, 38000, true
WHERE NOT EXISTS (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Matcha'));