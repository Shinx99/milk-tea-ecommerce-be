-- Migration: cart - alter_and_drop
-- Created: Sat Nov 29 05:21:54 PM +07 2025
-- Author: mango

-- Add your SQL statements below:
-- 1. Xoá unique index có điều kiện dùng enum
DROP INDEX IF EXISTS ux_one_active_cart_per_customer;

-- 2. (Không bắt buộc nhưng nên) xoá index cũ trên status
DROP INDEX IF EXISTS idx_carts_status;

-- 3. Bỏ default cũ dạng enum
ALTER TABLE carts
  ALTER COLUMN status DROP DEFAULT;

-- 4. Đổi type từ enum sang varchar
ALTER TABLE carts
  ALTER COLUMN status TYPE varchar(50) USING status::text;

-- 5. Đặt default mới kiểu varchar
ALTER TABLE carts
  ALTER COLUMN status SET DEFAULT 'active';

-- 6. Tạo lại index/unique index với varchar
--CREATE INDEX IF NOT EXISTS idx_carts_status ON carts(status);
--CREATE UNIQUE INDEX IF NOT EXISTS ux_one_active_cart_per_customer
--  ON carts(customer_id) WHERE status = 'active';
--DROP INDEX IF EXISTS ux_one_active_cart_per_customer;

  -- Tạo unique index mới cho 1 item trong giỏ
--CREATE UNIQUE INDEX IF NOT EXISTS ux_cart_item_per_customer
--ON carts (
--  customer_id,
--  product_id,
--  size_category_id,
--  sugar_category_id,
--  ice_category_id,
--  temperature_category_id
--)
--WHERE status = 'active'
--NULLS NOT DISTINCT;

-- 7. Sau cùng, bỏ type enum nếu không dùng chỗ khác
DROP TYPE IF EXISTS carts_status_enum;