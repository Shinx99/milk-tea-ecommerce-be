-- Migration: product - insert_sample_datas
-- Created: Fri Nov 21 02:34:20 PM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:
-- ===== CÀ PHÊ =====
      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Cà phê')),
      'Cà phê Sữa', 'Cà phê sữa', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Cà phê Sữa')
      );

      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Cà phê')),
      'Bạc xỉu', 'Bạc xỉu', 200, 33000, true
      where not exists(
          select id from products where lower(name)=lower('Bạc xỉu')
      );

      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Cà phê')),
      'Cà phê Bạc Hà', 'Cà phê bạc hà', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Cà phê Bạc Hà')
      );

      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Cà phê')),
      'Cà phê Kem Trứng', 'Cà phê kem trứng', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Cà phê Kem Trứng')
      );

      -- ===== KEM =====
      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Kem')),
      'Kem Dừa', 'Kem dừa', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Kem Dừa')
      );

      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Kem')),
      'Kem Sữa', 'Kem sữa', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Kem Sữa')
      );

      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Kem')),
      'Kem Socola', 'Kem socola', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Kem Socola')
      );

      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Kem')),
      'Kem Vani', 'Kem vani', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Kem Vani')
      );

      -- ===== BÁNH =====
      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Bánh')),
      'Bánh Tacos', 'Bánh tacos', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Bánh Tacos')
      );

      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Bánh')),
      'Bánh Tráng Trộn', 'Bánh tráng trộn', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Bánh Tráng Trộn')
      );

      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Bánh')),
      'Khoai Tây Chiên', 'Khoai tây chiên', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Khoai Tây Chiên')
      );

      insert into products(category_id, name, description, quantity, price, is_active)
      select (select id from categories where lower(category_name)=lower('Bánh')),
      'Bắp Xào', 'Bắp xào', 200, 35000, true
      where not exists(
          select id from products where lower(name)=lower('Bắp Xào')
      );
