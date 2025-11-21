-- Migration: category - insert_sample_datas
-- Created: Mon Nov 17 01:04:56 PM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:

-- Grandfather: Trà sữa
insert into categories (parent_id, category_name, sort_order, is_active)
select null, 'Trà sữa', 10, true
where not exists(
    select 1 from categories where parent_id is null and lower(category_name) = lower('Trà sữa')
);

-- Child: Size, Đá, Đường
-- Size
insert into categories (parent_id, category_name, sort_order, is_active)
    select (select id from categories where parent_id is null and lower(category_name)=lower('Trà sữa')),
    'Size', 1, true
where not exists(
    select 1 from categories
    where lower(category_name)=lower('Size') and
    parent_id = (select id from categories where parent_id is null and lower(category_name)=lower('Trà sữa'))
);

-- Đá
insert into categories (parent_id, category_name, sort_order, is_active)
    select (select id from categories where parent_id is null and lower(category_name)=lower('Trà sữa')),
    'Đá', 2, true
where not exists(
select 1 from categories
where lower(category_name)=lower('Đá') and
parent_id = (select id from categories where parent_id is null and lower(category_name) = lower('Trà sữa')));

-- Đường
insert into categories (parent_id, category_name, sort_order, is_active)
    select (select id from categories where parent_id is null and lower(category_name)=lower('Trà sữa')),
    'Đường', 3, true
where not exists(
    select 1 from categories
    where lower(category_name)=lower('Đường') and
    parent_id = (select id from categories where parent_id is null and lower(category_name)=lower('Trà sữa'))
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

-- XL
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Size')),
'XL', 3, true
where not exists(
    select 1 from categories where lower(category_name)=lower('XL') and
    parent_id = (select id from categories where lower(category_name)=lower('Size'))
);

-- GrandChild of Đá
-- Nhiều đá
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Đá')),
'Nhiều đá', 1, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Nhiều đá') and
    parent_id = (select id from categories where lower(category_name)=lower('Đá'))
);

-- Ít đá
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Đá')),
'Ít đá', 2, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Ít đá') and
    parent_id = (select id from categories where lower(category_name)=lower('Đá'))
);

-- Không đá
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Đá')),
'Không đá', 3, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Không đá') and
    parent_id = (select id from categories where lower(category_name)=lower('Đá'))
);

-- GrandChild of Đường
-- Nhiều đường
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Đường')),
'Nhiều đường', 1, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Nhiều đường') and
    parent_id = (select id from categories where lower(category_name)=lower('Đường'))
);

-- Trung bình
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Đường')),
'Trung bình', 2, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Trung bình') and
    parent_id = (select id from categories where lower(category_name)=lower('Đường'))
);

-- Ít đường
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Đường')),
'Ít đường', 3, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Ít đường') and
    parent_id = (select id from categories where lower(category_name)=lower('Đường'))
);

-- Không đường
insert into categories(parent_id, category_name, sort_order, is_active)
select (select id from categories where lower(category_name)=lower('Đường')),
'Không đường', 4, true
where not exists(
    select 1 from categories where lower(category_name)=lower('Không đường') and
    parent_id = (select id from categories where lower(category_name)=lower('Đường'))
);



