-- Migration: category - insert_sample_datas
-- Created: Thu Nov 20 03:20:49 PM +07 2025
-- Author: hoangvuongbui

-- Add your SQL statements below:

-- Grandfather: Cà phê, Bánh, Kem
insert into categories (parent_id, category_name, sort_order, is_active)
select null, 'Cà phê', 5, true
where not exists (
    select 1
    from categories
    where parent_id is null
      and lower(category_name) = lower('Cà phê')
);

-- Kem (Ice cream)
insert into categories (parent_id, category_name, sort_order, is_active)
select null, 'Kem', 15, true
where not exists (
    select 1
    from categories
    where parent_id is null
      and lower(category_name) = lower('Kem')
);

-- Bánh
insert into categories (parent_id, category_name, sort_order, is_active)
select null, 'Bánh', 6, true
where not exists(
    select 1
    from categories
    where parent_id is null
        and lower(category_name) = lower('Bánh')
);

-- Child cho Cà phê: Nhiệt độ, Đường
-- Nhiệt độ
insert into categories (parent_id, category_name, sort_order, is_active)
select (
    select id
    from categories
    where parent_id is null
      and lower(category_name) = lower('Cà phê')
), 'Nhiệt độ', 1, true
where not exists (
    select 1
    from categories
    where lower(category_name) = lower('Nhiệt độ')
      and parent_id = (
          select id
          from categories
          where parent_id is null
            and lower(category_name) = lower('Cà phê')
      )
);

-- Đường (con của Cà phê)
insert into categories (parent_id, category_name, sort_order, is_active)
select (
    select id
    from categories
    where parent_id is null
      and lower(category_name) = lower('Cà phê')
), 'Đường', 2, true
where not exists (
    select 1
    from categories
    where lower(category_name) = lower('Đường')
      and parent_id = (
          select id
          from categories
          where parent_id is null
            and lower(category_name) = lower('Cà phê')
      )
);

-- GrandChild of Nhiệt độ: Nóng, Bình thường, Lạnh
-- Nóng
insert into categories (parent_id, category_name, sort_order, is_active)
select (
    select id
    from categories
    where lower(category_name) = lower('Nhiệt độ')
      and parent_id = (
          select id
          from categories
          where parent_id is null
            and lower(category_name) = lower('Cà phê')
      )
),
'Nóng', 1, true
where not exists (
    select 1
    from categories
    where lower(category_name) = lower('Nóng')
      and parent_id = (
          select id
          from categories
          where lower(category_name) = lower('Nhiệt độ')
            and parent_id = (
                select id
                from categories
                where parent_id is null
                  and lower(category_name) = lower('Cà phê')
            )
      )
);

-- Bình thường
insert into categories (parent_id, category_name, sort_order, is_active)
select (
    select id
    from categories
    where lower(category_name) = lower('Nhiệt độ')
      and parent_id = (
          select id
          from categories
          where parent_id is null
            and lower(category_name) = lower('Cà phê')
      )
),
'Bình thường', 2, true
where not exists (
    select 1
    from categories
    where lower(category_name) = lower('Bình thường')
      and parent_id = (
          select id
          from categories
          where lower(category_name) = lower('Nhiệt độ')
            and parent_id = (
                select id
                from categories
                where parent_id is null
                  and lower(category_name) = lower('Cà phê')
            )
      )
);

-- Lạnh
insert into categories (parent_id, category_name, sort_order, is_active)
select (
    select id
    from categories
    where lower(category_name) = lower('Nhiệt độ')
      and parent_id = (
          select id
          from categories
          where parent_id is null
            and lower(category_name) = lower('Cà phê')
      )
),
'Lạnh', 3, true
where not exists (
    select 1
    from categories
    where lower(category_name) = lower('Lạnh')
      and parent_id = (
          select id
          from categories
          where lower(category_name) = lower('Nhiệt độ')
            and parent_id = (
                select id
                from categories
                where parent_id is null
                  and lower(category_name) = lower('Cà phê')
            )
      )
);

-- GrandChild of Đường (con của Đường -> con của Cà phê)
-- Nhiều đường
insert into categories(parent_id, category_name, sort_order, is_active)
select (
    select id
    from categories
    where lower(category_name) = lower('Đường')
      and parent_id = (
          select id
          from categories
          where parent_id is null
            and lower(category_name) = lower('Cà phê')
      )
),
'Nhiều đường', 1, true
where not exists (
    select 1
    from categories
    where lower(category_name) = lower('Nhiều đường')
      and parent_id = (
          select id
          from categories
          where lower(category_name) = lower('Đường')
            and parent_id = (
                select id
                from categories
                where parent_id is null
                  and lower(category_name) = lower('Cà phê')
            )
      )
);

-- Trung bình
insert into categories(parent_id, category_name, sort_order, is_active)
select (
    select id
    from categories
    where lower(category_name) = lower('Đường')
      and parent_id = (
          select id
          from categories
          where parent_id is null
            and lower(category_name) = lower('Cà phê')
      )
),
'Trung bình', 2, true
where not exists (
    select 1
    from categories
    where lower(category_name) = lower('Trung bình')
      and parent_id = (
          select id
          from categories
          where lower(category_name) = lower('Đường')
            and parent_id = (
                select id
                from categories
                where parent_id is null
                  and lower(category_name) = lower('Cà phê')
            )
      )
);

-- Ít đường
insert into categories(parent_id, category_name, sort_order, is_active)
select (
    select id
    from categories
    where lower(category_name) = lower('Đường')
      and parent_id = (
          select id
          from categories
          where parent_id is null
            and lower(category_name) = lower('Cà phê')
      )
),
'Ít đường', 3, true
where not exists (
    select 1
    from categories
    where lower(category_name) = lower('Ít đường')
      and parent_id = (
          select id
          from categories
          where lower(category_name) = lower('Đường')
            and parent_id = (
                select id
                from categories
                where parent_id is null
                  and lower(category_name) = lower('Cà phê')
            )
      )
);

-- Không đường
insert into categories(parent_id, category_name, sort_order, is_active)
select (
    select id
    from categories
    where lower(category_name) = lower('Đường')
      and parent_id = (
          select id
          from categories
          where parent_id is null
            and lower(category_name) = lower('Cà phê')
      )
),
'Không đường', 4, true
where not exists (
    select 1
    from categories
    where lower(category_name) = lower('Không đường')
      and parent_id = (
          select id
          from categories
          where lower(category_name) = lower('Đường')
            and parent_id = (
                select id
                from categories
                where parent_id is null
                  and lower(category_name) = lower('Cà phê')
            )
      )
);
