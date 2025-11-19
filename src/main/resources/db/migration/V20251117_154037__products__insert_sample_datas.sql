-- Migration: products - insert_sample_datas
-- Created: Mon Nov 17 03:40:37 PM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:
insert into products(category_id, name, description, quantity, price, is_active)
select (select id from categories where lower(category_name)=lower('Tra sua')),
'Tra sua O Long', 'Tra sua thien nhien', 200, 35000, true
where not exists(
    select id from products where lower(name)=lower('Tra sua O Long')
);

insert into products(category_id, name, description, quantity, price, is_active)
select (select id from categories where lower(category_name)=lower('Tra sua')),
'Tra sua Lai', 'Tra sua thien nhien', 200, 38000, true
where not exists(
    select id from products where lower(name)=lower('Tra sua Lai')
);

insert into products(category_id, name, description, quantity, price, is_active)
select(select id from categories where lower(category_name)=lower('Tra sua')),
'Tra sua Nguyen La', 'Tra sua thien nhien', 200, 38000, true
where not exists(
    select id from products where lower(name)=lower('Tra sua Nguyen La')
);

insert into products(category_id, name, description, quantity, price, is_active)
select(select id from categories where lower(category_name)=lower('Tra sua')),
'Tra sua Tran Chau Duong Den', 'Tra sua Tran Chau', 200, 38000, true
where not exists(
    select id from products where lower(name)=lower('Tra sua Tran Chau Duong Den')
);
