-- Migration: images - insert_sample_datas
-- Created: Tue Nov 18 10:52:41 AM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:
 INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Tra sua O Long')),0,true,'mt13','menu/trasua_olong','image','upload',1700100013,'jpg',800,800,120000,'https://res.cloudinary.com/drri2uxvy/image/upload/v1763386877/ae0f86e7-c99d-4a46-999a-7000c0ab79b5.png','Tra sua O Long'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/olong_trasua' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Tra sua Lai')),0,true,'mt14','menu/trasua_lai','image','upload',1700100014,'jpg',800,800,120000,'https://res.cloudinary.com/drri2uxvy/image/upload/v1760867235/bdc941a6-ef63-4cbb-95b7-6d6a81761a27.png','Tra sua Lai'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_lai' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Tra sua Nguyen La')),0,true,'mt15','menu/trasua_nguyenla','image','upload',1700100015,'jpg',800,800,120000,'https://res.cloudinary.com/drri2uxvy/image/upload/v1763386954/ee554944-d8e6-4371-ae39-4662189b92fe.png','Tra sua Nguyen La'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_nguyenla' AND resource_type='image' AND delivery_type='upload');

INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Tra sua Tran Chau Duong Den')),0,true,'mt16','menu/trasua_tranchauduongden','image','upload',1700100016,'jpg',800,800,120000,'https://res.cloudinary.com/drri2uxvy/image/upload/v1760867124/125d4407-d144-479f-94ee-8705202f1e3b.png','Tra sua Tran Chau Duong Den'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_tranchauduongden' AND resource_type='image' AND delivery_type='upload');


