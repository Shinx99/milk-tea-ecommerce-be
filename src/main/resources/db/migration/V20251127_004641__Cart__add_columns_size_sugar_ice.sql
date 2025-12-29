-- Migration: Cart - add_columns_size_sugar_ice
-- Created: Thu Nov 27 12:46:41 AM +07 2025
-- Author: mango

-- Add your SQL statements below:
ALTER TABLE carts
    ADD COLUMN size_category_id UUID REFERENCES categories(id),
    ADD COLUMN sugar_category_id UUID REFERENCES categories(id),
    ADD COLUMN ice_category_id UUID REFERENCES categories(id),
    ADD COLUMN temperature_category_id UUID REFERENCES categories(id);



