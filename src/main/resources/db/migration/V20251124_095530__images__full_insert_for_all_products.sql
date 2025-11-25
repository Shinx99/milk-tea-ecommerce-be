-- Migration: images - full_update_for_30_products
-- Created: Tue Nov 25 02:35:00 PM +07 2025
-- Author: NgHai112

-- Add your SQL statements below:

-- =============================================
-- 1. 10 SẢN PHẨM CHÈ & TRÀ TRÁI CÂY (Từ V20251011)
-- =============================================

-- Chè Bưởi
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Bưởi')), 0, true,
       'mt1', 'menu/che_buoi', 'image', 'upload', 1700100001, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764055382/564b71d7-f61a-4d8a-8106-99f6a32b201c.png', 'Chè Bưởi'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/che_buoi');

-- Chè Đậu Đen
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đen')), 0, true,
       'mt2', 'menu/che_dau_den', 'image', 'upload', 1700100002, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764055838/f79b30f1-c74b-4cda-a3d6-2ca45851a71c.png', 'Chè Đậu Đen'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/che_dau_den');

-- Chè Đậu Đỏ
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đỏ')), 0, true,
       'mt3', 'menu/che_dau_do', 'image', 'upload', 1700100003, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764056368/image-removebg-preview_kpousx.png', 'Chè Đậu Đỏ'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/che_dau_do');

-- Chè Đậu Xanh
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Xanh')), 0, true,
       'mt4', 'menu/che_dau_xanh', 'image', 'upload', 1700100004, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1763738005/che_dau_xanh_new.png', 'Chè Đậu Xanh'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/che_dau_xanh');

-- Chè Đậu Xanh Nước Cốt Dừa
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Xanh Nước Cốt Dừa')), 0, true,
       'mt5', 'menu/che_dau_xanh_nuoc_cot_dua', 'image', 'upload', 1700100005, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764056510/3a237ce7-df94-46de-851d-5f1a6a438945.png', 'Chè Đậu Xanh Nước Cốt Dừa'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/che_dau_xanh_nuoc_cot_dua');

-- Chè Đậu Đỏ Nước Cốt Dừa
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Chè Đậu Đỏ Nước Cốt Dừa')), 0, true,
       'mt6', 'menu/che_dau_do_nuoc_cot_dua', 'image', 'upload', 1700100006, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764056571/66266954-9a03-45a0-90a2-92f558067f6e.png', 'Chè Đậu Đỏ Nước Cốt Dừa'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/che_dau_do_nuoc_cot_dua');

-- Trà Bắp Kem Cheese
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà Bắp Kem Cheese')), 0, true,
       'mt7', 'menu/tra_bap_kem_cheese', 'image', 'upload', 1700100007, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764056795/c3bdabb3-5134-43de-832b-106a9ffeeea8.png', 'Trà Bắp Kem Cheese'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/tra_bap_kem_cheese');

-- Hồng Trà Kem Cheese
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Hồng Trà Kem Cheese')), 0, true,
       'mt8', 'menu/hong_tra_kem_cheese', 'image', 'upload', 1700100008, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764056879/c4d633da-9ec1-4e0c-8862-1fe40d137f85.png', 'Hồng Trà Kem Cheese'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/hong_tra_kem_cheese');

-- Trà Đào
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà Đào')), 0, true,
       'mt9', 'menu/tra_dao', 'image', 'upload', 1700100009, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764056915/a70ebf22-960e-405a-9126-e2587afc758a.png', 'Trà Đào'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/tra_dao');

-- Trà Vải
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà Vải')), 0, true,
       'mt10', 'menu/tra_vai', 'image', 'upload', 1700100010, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057022/a06d6f37-dc50-46e6-bbcd-f0b9684cc585.png', 'Trà Vải'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/tra_vai');

-- =============================================
-- 2. 8 SẢN PHẨM TRÀ SỮA (Từ V20251117 & V20251118)
-- =============================================

-- Trà sữa Ô Long
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Ô Long')), 0, true,
       'mt13', 'menu/trasua_olong', 'image', 'upload', 1700100013, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1763386877/ae0f86e7-c99d-4a46-999a-7000c0ab79b5.png', 'Trà sữa Ô Long'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_olong');

-- Trà sữa Lài
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Lài')), 0, true,
       'mt14', 'menu/trasua_lai', 'image', 'upload', 1700100014, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057252/f3bfb225-d515-4ecb-aa10-dac33a6737b5.png', 'Trà sữa Lài'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_lai');

-- Trà sữa Nguyên Lá
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Nguyên Lá')), 0, true,
       'mt15', 'menu/trasua_nguyenla', 'image', 'upload', 1700100015, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057348/2dcb5405-3b83-4621-842a-fbbd6010f374.png', 'Trà sữa Nguyên Lá'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_nguyenla');

-- Trà sữa Trân Châu Đường Đen
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Trân Châu Đường Đen')), 0, true,
       'mt16', 'menu/trasua_tranchauduongden', 'image', 'upload', 1700100016, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057400/ed07d895-0b9e-498c-8807-a616098b6837.png', 'Trà sữa Trân Châu Đường Đen'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_tranchauduongden');

-- Trà sữa Thái Xanh
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Thái Xanh')), 0, true,
       'mt17', 'menu/trasua_thaixanh', 'image', 'upload', 1700100017, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057497/89092820-e9ae-4bab-bdd1-6c8e33d04048.png', 'Trà sữa Thái Xanh'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_thaixanh');

-- Trà sữa Thái Đỏ
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Thái Đỏ')), 0, true,
       'mt18', 'menu/trasua_thaido', 'image', 'upload', 1700100018, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057545/e405823d-501a-4602-8c4e-1e3ec68c5704.png', 'Trà sữa Thái Đỏ'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_thaido');


-- Trà sữa Khoai Môn
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Khoai Môn')), 0, true,
       'mt19', 'menu/trasua_khoaimon', 'image', 'upload', 1700100019, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057643/1e87f4a5-72c6-4525-8995-34c62abc1f50.png', 'Trà sữa Khoai Môn'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_khoaimon');

-- Trà sữa Matcha
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Trà sữa Matcha')), 0, true,
       'mt20', 'menu/trasua_matcha', 'image', 'upload', 1700100020, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057741/381a591b-b86a-4fdd-a78d-8e8764c22e42.png', 'Trà sữa Matcha'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/trasua_matcha');

-- =============================================
-- 3. 12 SẢN PHẨM CÀ PHÊ, KEM, BÁNH (Từ V20251121)
-- =============================================

-- Cà phê Sữa
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Cà phê Sữa')), 0, true,
       'cf1', 'menu/caphe_sua', 'image', 'upload', 1700100021, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057887/be02296b-4394-47d8-a284-b02dd416e2b3.png', 'Cà phê Sữa'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/caphe_sua');

-- Bạc xỉu
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Bạc xỉu')), 0, true,
       'cf2', 'menu/bacxiu', 'image', 'upload', 1700100022, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057962/c07deb4e-f66f-4cf9-a035-a324fefa0d7c.png', 'Bạc xỉu'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/bacxiu');

-- Cà phê Bạc Hà
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Cà phê Bạc Hà')), 0, true,
       'cf3', 'menu/caphe_bacha', 'image', 'upload', 1700100023, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764057986/3d63dd92-5a7b-470d-95d8-ba6b8e0c0c45.png', 'Cà phê Bạc Hà'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/caphe_bacha');

-- Cà phê Kem Trứng
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Cà phê Kem Trứng')), 0, true,
       'cf4', 'menu/caphe_kemtrung', 'image', 'upload', 1700100024, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764058050/fdd5c6ff-0575-4ec2-a12b-07a414ee292f.png', 'Cà phê Kem Trứng'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/caphe_kemtrung');

-- Kem Dừa
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Kem Dừa')), 0, true,
       'ice1', 'menu/kem_dua', 'image', 'upload', 1700100025, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764054733/kemdua_dc1mas.png', 'Kem Dừa'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/kem_dua');

-- Kem Sữa
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Kem Sữa')), 0, true,
       'ice2', 'menu/kem_sua', 'image', 'upload', 1700100026, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764054735/kemsua_km7otq.png', 'Kem Sữa'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/kem_sua');

-- Kem Socola
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Kem Socola')), 0, true,
       'ice3', 'menu/kem_socola', 'image', 'upload', 1700100027, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764054884/9bafcbf1-9f89-48bb-a52d-888280375002.png', 'Kem Socola'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/kem_socola');

-- Kem Vani
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Kem Vani')), 0, true,
       'ice4', 'menu/kem_vani', 'image', 'upload', 1700100028, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764054835/41f95294-78a6-4e07-80aa-254e306aa846.png', 'Kem Vani'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/kem_vani');

-- Bánh Tacos
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Bánh Tacos')), 0, true,
       'cake1', 'menu/banh_tacos', 'image', 'upload', 1700100029, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764054281/tacos_fslr9r.png', 'Bánh Tacos'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/banh_tacos');

-- Bánh Tráng Trộn
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Bánh Tráng Trộn')), 0, true,
       'cake2', 'menu/banh_trang_tron', 'image', 'upload', 1700100030, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764054280/banh_trang_tron_tuulmd.png', 'Bánh Tráng Trộn'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/banh_trang_tron');

-- Khoai Tây Chiên
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Khoai Tây Chiên')), 0, true,
       'cake3', 'menu/khoai_tay_chien', 'image', 'upload', 1700100031, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764054280/khoaitaychien_r7lvv1.png', 'Khoai Tây Chiên'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/khoai_tay_chien');

-- Bắp Xào
INSERT INTO images
(product_id, sort_order, is_primary, asset_id, public_id, resource_type, delivery_type, version, format, width, height, bytes, secure_url, alt_text)
SELECT (SELECT id FROM products WHERE lower(name)=lower('Bắp Xào')), 0, true,
       'cake4', 'menu/bap_xao', 'image', 'upload', 1700100032, 'jpg', 800, 800, 120000,
       'https://res.cloudinary.com/drri2uxvy/image/upload/v1764054280/bapxap_yqfsyc.png', 'Bắp Xào'
    WHERE NOT EXISTS (SELECT 1 FROM images WHERE public_id='menu/bap_xao');