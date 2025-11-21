/********************************************************************************************
 * Migration:   V20251020_123000__update_real_cloudinary_links.sql
 * Purpose:     Cập nhật link ảnh thật (secure_url) cho bảng IMAGES trong DB e-commerce
 * Author:      Hai
 * Date:        2025-10-20
 *
 * ✅ Lưu ý:
 * - Giữ nguyên tất cả dữ liệu khác (không xóa hay chèn lại).
 * - Chỉ cập nhật cột secure_url của bảng images dựa vào public_id.
 * - Bạn tự điền đúng link Cloudinary thật vào từng dòng tương ứng.
 * - Có thể chạy lại nhiều lần, không gây lỗi (idempotent).
 ********************************************************************************************/

BEGIN;

UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760866451/tra-sua-matcha_vrbgkk.jpg'
WHERE public_id = 'menu/che_buoi';

UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760866451/tra-sua-matcha_vrbgkk.jpg'
WHERE public_id = 'menu/che_dau_den';

UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760866449/hong-tra-sua_kcndu4.jpg'
WHERE public_id = 'menu/che_dau_do';

UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760866450/tra-dao-cam-sa_rz9w3f.jpg'
WHERE public_id = 'menu/che_dau_xanh';

UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760866452/tra-vai_vebbfh.jpg'
WHERE public_id = 'menu/che_dau_xanh_nuoc_cot_dua';

UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760866449/hong-tra-sua_kcndu4.jpg'
WHERE public_id = 'menu/che_dau_do_nuoc_cot_dua';

UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760866451/tra-sua-matcha_vrbgkk.jpg'
WHERE public_id = 'menu/tra_bap_kem_cheese';

-- 🧀 Cheese Foam Black Tea
UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760866452/tra-vai_vebbfh.jpg'
WHERE public_id = 'menu/hong_tra_kem_cheese';

-- 🍑 Peach Tea
UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760881121/f5d56406-a0cf-4f62-9f94-20072cf703a6.png'
WHERE public_id = 'menu/tra_dao';

-- 🍇 Lychee Tea
UPDATE images
SET secure_url = 'https://res.cloudinary.com/drri2uxvy/image/upload/v1760881206/9df47813-8589-463b-8f56-87e7a9970acb.png'
WHERE public_id = 'menu/tra_vai';

COMMIT;

/********************************************************************************************
 * ✅ Hướng dẫn:
 * - Thay phần “vXXXXXXX” bằng version thật trong link Cloudinary của bạn.
 * - Mỗi URL tương ứng 1 ảnh thực tế trong folder "milk-tea-products".
 * - Sau khi lưu file, khởi động lại backend → Flyway tự apply migration này.
 ********************************************************************************************/
