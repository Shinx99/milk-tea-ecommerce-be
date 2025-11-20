-- Migration: category - insert_sample_datas
-- Created: Mon Nov 17 01:04:56 PM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:

-- Grandfather: Tra sua
insert into categories (parent_id, category_name, sort_order, is_active)
select null, 'Tra sua', 10, true
where not exists(
    select 1 from categories where parent_id is null and lower(category_name) = lower('Tra sua')
);

-- Child: Size, Da, Duong
-- Size
insert into categories (parent_id, category_name, sort_order, is_active)
    select (select id from categories where parent_id is null and lower(category_name)=lower('Tra sua')),
    'Size', 1, true
where not exists(
    select 1 from categories
    where lower(category_name)=lower('Size') and
    parent_id = (select id from categories where parent_id is null and lower(category_name)=lower('Tra sua'))
);

-- Da
insert into categories (parent_id, category_name, sort_order, is_active)
    select (select id from categories where parent_id is null and lower(category_name)=lower('Tra sua')),
    'Da', 2, true
where not exists(
select 1 from categories
where lower(category_name)=lower('Da') and
parent_id = (select id from categories where parent_id is null and lower(category_name) = lower('Tra sua')));

-- Duong
insert into categories (parent_id, category_name, sort_order, is_active)
    select (select id from categories where parent_id is null and lower(category_name)=lower('Tra sua')),
    'Duong', 3, true
where not exists(
    select 1 from categories
    where lower(category_name)=lower('Duong') and
    parent_id = (select id from categories where parent_id is null and lower(category_name)=lower('Tra sua'))
);


-- GrandChild of Size: M, L, XL
-- M
insert into categories (parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Size')),
'M', 1, true
where not exists(
    select 1 from categories where lower(category_name)=lower('M') and
    parent_id = (select id from categories where lower(category_name)=lower('Size'))
);

-- L
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Size')),
'L', 2, true
where not exists(
    select 1 from categories where lower(category_name)=lower('L') and
    parent_id = (select id from categories where lower(category_name)=lower('Size'))
);

--XL
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Size')),
'XL', 3, true
where not exists(
    select 1 from categories where lower(category_name)=lower('XL') and
    parent_id = (select id from categories where lower(category_name)=lower('Size'))
);

-- GrandChild of Da
-- Nhieu da
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Da')),
'Nhieu da', 1, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Nhieu da') and
    parent_id = (select id from categories where lower(category_name)=lower('Da'))
);

-- It da
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Da')),
'It da', 2, true
where not exists(
    select 1 from categories where lower(category_name)=lower('It da') and
    parent_id = (select id from categories where lower(category_name)=lower('Da'))
);

-- Khong da
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Da')),
'Khong da', 3, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Khong da') and
    parent_id = (select id from categories where lower(category_name)=lower('Da'))
);

-- GrandChild of Duong
-- Nhieu duong
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Duong')),
'Nhieu duong', 1, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Nhieu Duong') and
    parent_id = (select id from categories where lower(category_name)=lower('Duong'))
);

-- Trung binh
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Duong')),
'Trung binh', 2, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Trung binh') and
    parent_id = (select id from categories where lower(category_name)=lower('Duong'))
);

-- It duong
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Duong')),
'It duong', 3, true
where not exists(
    select 1 from categories where lower(category_name)=lower('It duong') and
    parent_id = (select id from categories where lower(category_name)=lower('Duong'))
);

-- Khong duong
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Duong')),
'Khong duong', 4, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Khong duong') and
    parent_id = (select id from categories where lower(category_name)=lower('Duong'))
);



