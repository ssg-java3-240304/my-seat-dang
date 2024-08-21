INSERT INTO tbl_reservation (
    id,
    room_num,
    created_at,
    customer_id,
    customer_name,
    customer_phone,
    reservation_status,
    reserved_at,
    store_id,
    store_name,
    store_phone,
    store_owner_id,
    store_owner_name
) VALUES (
             1, -- id (예시 값)
             'A101', -- room_num (예시 값)
             '2024-08-19 14:30:00', -- created_at (예시 값)
             1, -- customer_id (예시 값)
             '이재용', -- customer_name (예시 값)
             '010-1234-5678', -- customer_phone (예시 값)
             'DETAILING', -- reservation_status (enum 값)
             '2024-08-20 18:00:00', -- reserved_at (예시 값)
             1, -- store_id (예시 값)
             '마싯당 1호점', -- store_name (예시 값)
             '02-9876-5432', -- store_phone (예시 값) // 얜.. 모를수도
             6, -- store_owner_id (예시 값)
             '마싯당' -- store_owner_name (예시 값)
         );