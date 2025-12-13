-- Migration: order - add_colums
-- Created: Sat Dec 13 01:44:27 PM +07 2025
-- Author: mango

-- Add your SQL statements below:

ALTER TABLE orders
    ADD COLUMN customer_name varchar(255),      --Tên người nhận hàng
    ADD COLUMN phone         varchar(50),       --Sdt người nhận
    ADD COLUMN address       varchar(255);      --Địa chỉ nhận


