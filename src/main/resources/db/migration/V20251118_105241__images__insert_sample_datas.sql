-- Migration: images - insert_sample_datas
-- Created: Tue Nov 18 10:52:41 AM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:
-- Trà sữa Ô Long
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Ô Long')), 0, true,
       'mt13', 'menu/trasua_olong', 'image', 'upload', 1700100013, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1763386877/ae0f86e7-c99d-4a46-999a-7000c0ab79b5.png', 'Trà sữa Ô Long'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/trasua_olong' AND resource_type='image' AND delivery_type='upload'
);

-- Trà sữa Lài
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Lài')), 0, true,
       'mt14', 'menu/trasua_lai', 'image', 'upload', 1700100014, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1760867235/bdc941a6-ef63-4cbb-95b7-6d6a81761a27.png', 'Trà sữa Lài'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/trasua_lai' AND resource_type='image' AND delivery_type='upload'
);

-- Trà sữa Nguyên Lá
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Nguyên Lá')), 0, true,
       'mt15', 'menu/trasua_nguyenla', 'image', 'upload', 1700100015, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1763386954/ee554944-d8e6-4371-ae39-4662189b92fe.png', 'Trà sữa Nguyên Lá'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/trasua_nguyenla' AND resource_type='image' AND delivery_type='upload'
);

-- Trà sữa Trân Châu Đường Đen
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Trân Châu Đường Đen')), 0, true,
       'mt16', 'menu/trasua_tranchauduongden', 'image', 'upload', 1700100016, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1763737957/6589cc26-d797-46f7-939a-2797325ec36a.png', 'Trà sữa Trân Châu Đường Đen'
WHERE NOT EXISTS (
    SELECT 1 FROM images WHERE public_id='menu/trasua_tranchauduongden' AND resource_type='image' AND delivery_type='upload'
);


