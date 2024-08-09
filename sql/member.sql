-- Customer 데이터 삽입
INSERT INTO tbl_member (member_email, join_date, member_password, member_phone, member_role, member_status, image_gen_left, customer_gender, customer_birthday, DTYPE,member_name)
VALUES ('customer@naver.com', '2024-01-01', '1234', '010-1234-5678', 'ROLE_CUSTOMER', 'APPROVED', 3, 'FEMALE', '1990-01-01', 'Customer','김나경');

-- StoreOwner 데이터 삽입
INSERT INTO tbl_member (member_email, join_date, member_password, member_phone, member_role, member_status, business_license_image, business_license, bank_account_copy, bank_account, store_id, store_name, store_type, store_address, DTYPE,member_name)
VALUES ('storeowner@naver.com', '2024-01-01', '1234', '010-8765-4321', 'ROLE_STORE_OWNER', 'APPROVED', 'http://example.com/business-license.png', '123-45-67890', 'http://example.com/bank-account.png', '123-456-789', 1, '스위트 데이즈', 'CUSTOM', '서울특별시 강남구 청담동 234-56', 'StoreOwner','박정은');

-- Admin 데이터 삽입
INSERT INTO tbl_member (member_email, join_date, member_password, member_phone, member_role, member_status, DTYPE,member_name)
VALUES ('admin@naver.com', '2024-01-01', '1234', '010-0000-0000', 'ROLE_ADMIN', 'APPROVED', 'Admin','구민상');
