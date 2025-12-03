-- Migration: Order_OrderItem_Payment - alter_table_&_alter_columns
-- Created: Sun Nov 30 10:56:14 PM +07 2025
-- Author: mango

-- =====================================================
-- 0) Xử lý view phụ thuộc (mv_revenue_daily)
-- =====================================================
-- 0) Drop các materialized view phụ thuộc orders.status
DROP MATERIALIZED VIEW IF EXISTS mv_revenue_daily;
DROP MATERIALIZED VIEW IF EXISTS mv_revenue_weekly;
DROP MATERIALIZED VIEW IF EXISTS mv_revenue_monthly;
DROP MATERIALIZED VIEW IF EXISTS mv_revenue_yearly;
DROP MATERIALIZED VIEW IF EXISTS mv_top_selling_products;


-- =====================================================
-- 1) ORDERS: enum -> varchar
-- =====================================================

-- 1.1 Bỏ default enum cũ
ALTER TABLE orders
  ALTER COLUMN status DROP DEFAULT;

-- 1.2 Đổi type enum sang varchar(30)
ALTER TABLE orders
  ALTER COLUMN status TYPE varchar(30) USING status::text;

-- 1.3 Đặt default mới varchar
ALTER TABLE orders
  ALTER COLUMN status SET DEFAULT 'pending';

-- 1.4 Thêm các cột còn thiếu cho orders
ALTER TABLE orders
  ADD COLUMN IF NOT EXISTS order_code varchar(50),
  ADD COLUMN IF NOT EXISTS note text,
  ADD COLUMN IF NOT EXISTS currency varchar(3) DEFAULT 'VND',
  ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;



-- 1.5 Đảm bảo order_code unique
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint
    WHERE conname = 'uk_orders_order_code'
  ) THEN
    ALTER TABLE orders
      ADD CONSTRAINT uk_orders_order_code UNIQUE (order_code);
  END IF;
END$$;

-- =====================================================
-- 2) PAYMENTS: enum -> varchar + VNPay fields
-- =====================================================

-- 2.1 Bỏ default enum cũ
ALTER TABLE payments
  ALTER COLUMN status DROP DEFAULT;

-- 2.2 Đổi type enum sang varchar(30)
ALTER TABLE payments
  ALTER COLUMN status TYPE varchar(30) USING status::text;

-- 2.3 Đặt default mới varchar
ALTER TABLE payments
  ALTER COLUMN status SET DEFAULT 'pending';

-- Xóa cột payload để dùng chung là payload_json để nhận cả req/resp
ALTER TABLE payments
  DROP COLUMN IF EXISTS payload;

-- 2.4 Thêm các field hỗ trợ VNPay
ALTER TABLE payments
  ADD COLUMN IF NOT EXISTS currency varchar(3) DEFAULT 'VND',
  ADD COLUMN IF NOT EXISTS vnp_transaction_no varchar(100),
  ADD COLUMN IF NOT EXISTS vnp_response_code varchar(20),
  ADD COLUMN IF NOT EXISTS vnp_bank_code varchar(50),
  ADD COLUMN IF NOT EXISTS vnp_pay_date varchar(20),
  ADD COLUMN IF NOT EXISTS updated_at timestamptz,
  ADD COLUMN IF NOT EXISTS payload_json text;

-- 2.5 Cho phép 1 order có nhiều payment (nếu muốn)
ALTER TABLE payments
  DROP CONSTRAINT IF EXISTS payments_order_id_key;

-- =====================================================
-- 3) ORDER_ITEMS: thêm option trà sữa
-- =====================================================
ALTER TABLE order_items
  ADD COLUMN IF NOT EXISTS product_name varchar(255),
  ADD COLUMN IF NOT EXISTS product_image varchar(500),
  ADD COLUMN IF NOT EXISTS size_category_id uuid,
  ADD COLUMN IF NOT EXISTS sugar_category_id uuid,
  ADD COLUMN IF NOT EXISTS ice_category_id uuid,
  ADD COLUMN IF NOT EXISTS temperature_category_id uuid,
  ADD COLUMN IF NOT EXISTS note text,
  ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- =====================================================
-- 4) Xoá ENUM types cũ (nếu không còn dùng)
-- =====================================================

-- Orders enum
DO $$
DECLARE
  enum_used_count integer;
BEGIN
  SELECT COUNT(*) INTO enum_used_count
  FROM information_schema.columns
  WHERE udt_name = 'order_status_enum';

  IF enum_used_count = 0 THEN
    DROP TYPE IF EXISTS order_status_enum CASCADE;
  END IF;
END $$;

-- Payments enum
DO $$
DECLARE
  enum_used_count integer;
BEGIN
  SELECT COUNT(*) INTO enum_used_count
  FROM information_schema.columns
  WHERE udt_name = 'payment_status_enum';

  IF enum_used_count = 0 THEN
    DROP TYPE IF EXISTS payment_status_enum CASCADE;
  END IF;
END $$;

-- =====================================================
-- 5) Index optimization
-- =====================================================

-- Orders
CREATE INDEX IF NOT EXISTS idx_orders_order_code ON orders(order_code);
CREATE INDEX IF NOT EXISTS idx_orders_customer_status ON orders(customer_id, status);

-- Payments
CREATE INDEX IF NOT EXISTS idx_payments_order_status ON payments(order_id, status);
CREATE INDEX IF NOT EXISTS idx_payments_transaction_ref ON payments(transaction_ref);
CREATE INDEX IF NOT EXISTS idx_payments_vnp_transaction_no ON payments(vnp_transaction_no);

-- =====================================================
-- 6) Default data update
-- =====================================================

UPDATE orders
SET order_code = 'ORDER_' || id::text
WHERE order_code IS NULL;

UPDATE payments
SET currency = 'VND'
WHERE currency IS NULL;

UPDATE payments
SET updated_at = created_at
WHERE updated_at IS NULL;


