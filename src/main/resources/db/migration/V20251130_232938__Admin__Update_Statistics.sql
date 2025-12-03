-- Migration: Admin - Update_Statistics
-- Created: Sun Nov 30 11:29:38 PM +07 2025
-- Author: mango

-- Add your SQL statements below:

-- Tạo lại mv_revenue_daily
CREATE MATERIALIZED VIEW mv_revenue_daily AS
SELECT
  to_char(o.placed_at, 'YYYY-MM-DD') AS period_day,
  c.category_name,
  SUM(oi.price * oi.quantity) AS total_revenue,
  SUM(oi.quantity) AS total_quantity,
  MAX(oi.price) AS max_price,
  MIN(oi.price) AS min_price,
  ROUND(AVG(oi.price), 2) AS avg_price
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
JOIN products p ON p.id = oi.product_id
JOIN categories c ON c.id = p.category_id
WHERE o.status = 'completed' AND o.placed_at IS NOT NULL
GROUP BY period_day, c.category_name
WITH NO DATA;

-- Tạo lại mv_revenue_weekly
CREATE MATERIALIZED VIEW mv_revenue_weekly AS
SELECT
  to_char(o.placed_at, 'IYYY-\"W\"IW') AS period_week,
  c.category_name,
  SUM(oi.price * oi.quantity) AS total_revenue,
  SUM(oi.quantity) AS total_quantity,
  MAX(oi.price) AS max_price,
  MIN(oi.price) AS min_price,
  ROUND(AVG(oi.price), 2) AS avg_price
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
JOIN products p ON p.id = oi.product_id
JOIN categories c ON c.id = p.category_id
WHERE o.status = 'completed' AND o.placed_at IS NOT NULL
GROUP BY period_week, c.category_name
WITH NO DATA;

-- Tạo lại mv_revenue_monthly
CREATE MATERIALIZED VIEW mv_revenue_monthly AS
SELECT
  to_char(o.placed_at, 'YYYY-MM') AS period_month,
  c.category_name,
  SUM(oi.price * oi.quantity) AS total_revenue,
  SUM(oi.quantity) AS total_quantity,
  MAX(oi.price) AS max_price,
  MIN(oi.price) AS min_price,
  ROUND(AVG(oi.price), 2) AS avg_price
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
JOIN products p ON p.id = oi.product_id
JOIN categories c ON c.id = p.category_id
WHERE o.status = 'completed' AND o.placed_at IS NOT NULL
GROUP BY period_month, c.category_name
WITH NO DATA;

-- Tạo lại mv_revenue_yearly
CREATE MATERIALIZED VIEW mv_revenue_yearly AS
SELECT
  to_char(o.placed_at, 'YYYY') AS period_year,
  c.category_name,
  SUM(oi.price * oi.quantity) AS total_revenue,
  SUM(oi.quantity) AS total_quantity,
  MAX(oi.price) AS max_price,
  MIN(oi.price) AS min_price,
  ROUND(AVG(oi.price), 2) AS avg_price
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
JOIN products p ON p.id = oi.product_id
JOIN categories c ON c.id = p.category_id
WHERE o.status = 'completed' AND o.placed_at IS NOT NULL
GROUP BY period_year, c.category_name
WITH NO DATA;

-- Tạo lại mv_top_selling_products
CREATE MATERIALIZED VIEW mv_top_selling_products AS
SELECT
  p.id AS product_id,
  p.name AS product_name,
  SUM(oi.quantity) AS total_quantity_sold,
  SUM(oi.price * oi.quantity) AS total_revenue
FROM order_items oi
JOIN products p ON p.id = oi.product_id
JOIN orders o ON o.id = oi.order_id
WHERE o.status = 'completed' AND o.placed_at IS NOT NULL
GROUP BY p.id, p.name
WITH NO DATA;

-- refresh ngay sau khi tạo lại
REFRESH MATERIALIZED VIEW mv_revenue_daily;
REFRESH MATERIALIZED VIEW mv_revenue_weekly;
REFRESH MATERIALIZED VIEW mv_revenue_monthly;
REFRESH MATERIALIZED VIEW mv_revenue_yearly;
REFRESH MATERIALIZED VIEW mv_top_selling_products;


