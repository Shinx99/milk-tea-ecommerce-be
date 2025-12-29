-- Migration: addresses - create_trigger
-- Created: Tue Oct 21 10:14:35 PM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:
-- Tạo function
CREATE OR REPLACE FUNCTION trg_addresses_unset_default_when_inactive()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
  IF NEW.is_active = FALSE THEN
    NEW.is_default := FALSE;
  END IF;
  RETURN NEW;
END;
$$;

-- Xóa trigger cũ nếu có (đặt sau function cũng được)
DROP TRIGGER IF EXISTS addresses_unset_default_when_inactive ON addresses;

-- Tạo trigger
CREATE TRIGGER addresses_unset_default_when_inactive
BEFORE INSERT OR UPDATE OF is_active ON addresses
FOR EACH ROW
EXECUTE FUNCTION trg_addresses_unset_default_when_inactive();



