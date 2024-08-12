# create user 'myseatdangdb'@'%' identified by 'amatdang';
# show databases;
# create database myseatdangdb;
# grant all privileges on myseatdangdb.* to 'myseatdangdb'@'%';
# grant all privileges on myseatdangdb.* to 'sh'@'%';
# show grants for 'myseatdangdb'@'%';
# show grants for 'sh'@'%';

# DROP TABLE IF EXISTS store CASCADE;

# CREATE TABLE store (
#     store_id int auto_increment PRIMARY KEY,
#     store_name VARCHAR(100),
#     store_address VARCHAR(255),
#     open_time TIME,
#     start_break_time TIME,
#     end_break_time TIME,
#     last_order TIME,
#     close_time TIME,
#     regular_day_off VARCHAR(50),
#     thumbnail VARCHAR(255),
#     image VARCHAR(255),
#     description TEXT,
#     notice TEXT,
#     phone VARCHAR(20),
#     star_rating DECIMAL(2,1),
#     store_type VARCHAR(50),
#     constraint ck_storeType check (binary store_type in ('GENERAL_WAITING', 'GENERAL_RESERVATION', 'CUSTOM'))
# );
# alter table store auto_increment = 1000;

INSERT INTO store
(store_name, store_address, open_time, start_break_time, end_break_time, last_order, close_time, regular_day_off, thumbnail, images, description, notice, phone, star_rating, store_type)
VALUES
    ('강남 베이커리 본점', '서울특별시 강남구 역삼동 123-45', '08:00:00', '15:00:00', '15:30:00', '20:30:00', '21:00:00', '일요일', 'gangnam_bakery_thumbnail.jpg', 'store_bakery.jpg','강남의 인기 베이커리로, 다양한 종류의 빵과 커피를 제공합니다.', '주말에는 대기시간이 길어질 수 있습니다.', '02-1234-5678', 4.5, 'GENERAL_WAITING'),
    ('스위트 데이즈 청담동', '서울특별시 강남구 청담동 234-56', '09:00:00', '14:00:00', '14:30:00', '19:00:00', '20:00:00', '월요일', 'sweet_days_thumbnail.jpg', 'store_cake.jpg', '프리미엄 케이크와 디저트를 제공하는 고급 베이커리입니다.', '예약이 필요할 수 있습니다.', '02-2345-6789', 4.7, 'CUSTOM'),
    ('디저트 월드 삼성점', '서울특별시 강남구 삼성동 345-67', '10:00:00', '15:00:00', '15:30:00', '21:00:00', '22:00:00', '화요일', 'dessert_world_thumbnail.jpg', 'store_cake.jpg', '다양한 디저트와 스낵을 제공하는 카페형 베이커리입니다.', '주말에는 혼잡할 수 있습니다.', '02-3456-7890', 4.3, 'CUSTOM'),
    ('강남 케이크 하우스 강남역점', '서울특별시 강남구 서초동 456-78', '11:00:00', null, null, '20:00:00', '21:00:00', '매월 첫째 월요일', 'cake_house_thumbnail.jpg', 'store_cake.jpg', '맞춤형 케이크 전문 매장으로, 특별한 날을 위한 케이크를 제작합니다.', '예약 필수.', '02-4567-8901', 4.6, 'CUSTOM'),
    ('페이스트리 코너 논현점', '서울특별시 강남구 논현동 567-89', '08:30:00', '15:00:00', '15:30:00', '19:30:00', '20:30:00', '수요일', 'pastry_corner_thumbnail.jpg',  'store_bakery.jpg', '다양한 패스트리와 커피를 제공하는 베이커리입니다.', '저녁 시간대에 붐빌 수 있습니다.', '02-5678-9012', 4.4, 'GENERAL_WAITING'),
    ('베이킹 라운지 본점', '서울특별시 강남구 역삼동 678-90', '10:00:00', null, null, '18:00:00', '19:00:00', '목요일', 'baking_lounge_thumbnail.jpg', 'store_cake.jpg', '편안한 분위기에서 다양한 베이커리 제품을 즐길 수 있는 곳입니다.', '평일 방문을 권장합니다.', '02-6789-0123', 4.2, 'CUSTOM'),
    ('디저트 갤러리 압구정점', '서울특별시 강남구 신사동 789-01', '09:00:00', '15:00:00', '15:30:00', '20:00:00', '21:00:00', '금요일', 'dessert_gallery_thumbnail.jpg',  'store_bakery3.jpg', '매일 새롭게 만들어지는 디저트가 특징인 갤러리 같은 베이커리입니다.', '주말에는 대기시간이 길어질 수 있습니다.', '02-7890-1234', 4.5, 'GENERAL_RESERVATION'),
    ('캔디 케이크 논현점', '서울특별시 강남구 잠원동 890-12', '10:00:00', null, null, '19:00:00', '20:00:00', '일요일', 'candy_cake_thumbnail.jpg', 'store_cake.jpg', '다양한 맞춤형 케이크를 제공하는 전문 매장입니다.', '예약 필수.', '02-8901-2345', 4.8, 'CUSTOM'),
    ('크리스탈 베이커리 신논현점', '서울특별시 강남구 반포동 901-23', '07:00:00', '14:00:00', '14:30:00', '20:00:00', '21:00:00', '월요일', 'crystal_bakery_thumbnail.jpg',  'store_bakery.jpg', '아침 일찍부터 운영하는 베이커리로, 다양한 빵과 커피를 제공합니다.', '정기휴일에 유의하세요.', '02-9012-3456', 4.4, 'GENERAL_RESERVATION'),
    ('하이디 케이크 도산점', '서울특별시 강남구 삼성동 012-34', '09:00:00', null, null, '18:00:00', '19:00:00', '화요일', 'heidi_cake_thumbnail.jpg', 'store_cake.jpg','프리미엄 케이크와 디저트를 전문으로 하는 매장입니다.', '특별한 날에는 미리 예약 부탁드립니다.', '02-0123-4567', 4.6, 'CUSTOM'),

    ('강남 베이커리 신사점', '서울특별시 강남구 신사동 123-45', '08:00:00', '15:00:00', '15:30:00', '20:30:00', '21:00:00', '일요일', 'gangnam_bakery_thumbnail.jpg',  'store_bakery.jpg', '강남의 인기 베이커리로, 다양한 종류의 빵과 커피를 제공합니다.', '주말에는 대기시간이 길어질 수 있습니다.', '02-1234-5678', 4.5, 'GENERAL_WAITING'),
    ('스위트 데이즈 삼성점', '서울특별시 강남구 삼성동 234-56', '09:00:00', '14:00:00', '14:30:00', '19:00:00', '20:00:00', '월요일', 'sweet_days_thumbnail.jpg', 'store_cake.jpg', '프리미엄 케이크와 디저트를 제공하는 고급 베이커리입니다.', '예약이 필요할 수 있습니다.', '02-2345-6789', 4.7, 'CUSTOM'),
    ('디저트 월드 역삼점', '서울특별시 강남구 역삼동 345-67', '10:00:00', '15:00:00', '15:30:00', '21:00:00', '22:00:00', '화요일', 'dessert_world_thumbnail.jpg', 'store_cake.jpg', '다양한 디저트와 스낵을 제공하는 카페형 베이커리입니다.', '주말에는 혼잡할 수 있습니다.', '02-3456-7890', 4.3, 'CUSTOM'),
    ('강남 케이크 하우스 가로수길점', '서울특별시 강남구 신사동 456-78', '11:00:00', null, null, '20:00:00', '21:00:00', '매월 첫째 월요일', 'cake_house_thumbnail.jpg', 'store_cake.jpg', '맞춤형 케이크 전문 매장으로, 특별한 날을 위한 케이크를 제작합니다.', '예약 필수.', '02-4567-8901', 4.6, 'CUSTOM'),
    ('페이스트리 코너 본점', '서울특별시 강남구 도곡동 567-89', '08:30:00', '15:00:00', '15:30:00', '19:30:00', '20:30:00', '수요일', 'pastry_corner_thumbnail.jpg',  'store_bakery.jpg', '다양한 패스트리와 커피를 제공하는 베이커리입니다.', '저녁 시간대에 붐빌 수 있습니다.', '02-5678-9012', 4.4, 'GENERAL_WAITING'),
    ('베이킹 라운지 압구정로데오점', '서울특별시 강남구 압구정동 678-90', '10:00:00', null, null, '18:00:00', '19:00:00', '목요일', 'baking_lounge_thumbnail.jpg', 'store_cake.jpg', '편안한 분위기에서 다양한 베이커리 제품을 즐길 수 있는 곳입니다.', '평일 방문을 권장합니다.', '02-6789-0123', 4.2, 'CUSTOM'),
    ('디저트 갤러리 일원역점', '서울특별시 강남구 일원동 789-01', '09:00:00', '15:00:00', '15:30:00', '20:00:00', '21:00:00', '금요일', 'dessert_gallery_thumbnail.jpg',  'store_bakery.jpg', '매일 새롭게 만들어지는 디저트가 특징인 갤러리 같은 베이커리입니다.', '주말에는 대기시간이 길어질 수 있습니다.', '02-7890-1234', 4.5, 'GENERAL_RESERVATION'),
    ('캔디 케이크 구룡역점', '서울특별시 강남구 개포동 890-12', '10:00:00', null, null, '19:00:00', '20:00:00', '일요일', 'candy_cake_thumbnail.jpg', 'store_cake.jpg', '다양한 맞춤형 케이크를 제공하는 전문 매장입니다.', '예약 필수.', '02-8901-2345', 4.8, 'CUSTOM'),
    ('크리스탈 베이커리 삼성역점', '서울특별시 강남구 삼성동 901-23', '07:00:00', '14:00:00', '14:30:00', '20:00:00', '21:00:00', '월요일', 'crystal_bakery_thumbnail.jpg',  'store_bakery.jpg', '아침 일찍부터 운영하는 베이커리로, 다양한 빵과 커피를 제공합니다.', '정기휴일에 유의하세요.', '02-9012-3456', 4.4, 'GENERAL_RESERVATION'),
    ('하이디 케이크 본점', '서울특별시 강남구 역삼동 012-34', '09:00:00', null, null, '18:00:00', '19:00:00', '화요일', 'heidi_cake_thumbnail.jpg', 'store_cake.jpg', '프리미엄 케이크와 디저트를 전문으로 하는 매장입니다.', '특별한 날에는 미리 예약 부탁드립니다.', '02-0123-4567', 4.6, 'CUSTOM'),

    ('베이킹 라운지 압구정로데오점', '서울특별시 강남구 압구정동 678-90', '10:00:00', null, null, '18:00:00', '19:00:00', '목요일', 'baking_lounge_thumbnail.jpg', 'store_cake.jpg', '편안한 분위기에서 다양한 베이커리 제품을 즐길 수 있는 곳입니다.', '평일 방문을 권장합니다.', '02-6789-0123', 4.2, 'CUSTOM'),
    ('디저트 갤러리 일원역점', '서울특별시 강남구 일원동 789-01', '09:00:00', '15:00:00', '15:30:00', '20:00:00', '21:00:00', '금요일', 'dessert_gallery_thumbnail.jpg', 'store_bakery.jpg', '매일 새롭게 만들어지는 디저트가 특징인 갤러리 같은 베이커리입니다.', '주말에는 대기시간이 길어질 수 있습니다.', '02-7890-1234', 4.5, 'GENERAL_RESERVATION'),
    ('캔디 케이크 구룡역점', '서울특별시 강남구 개포동 890-12', '10:00:00', null, null, '19:00:00', '20:00:00', '일요일', 'candy_cake_thumbnail.jpg', 'store_cake.jpg', '다양한 맞춤형 케이크를 제공하는 전문 매장입니다.', '예약 필수.', '02-8901-2345', 4.8, 'CUSTOM'),
    ('크리스탈 베이커리 삼성역점', '서울특별시 강남구 삼성동 901-23', '07:00:00', '14:00:00', '14:30:00', '20:00:00', '21:00:00', '월요일', 'crystal_bakery_thumbnail.jpg',  'store_bakery.jpg', '아침 일찍부터 운영하는 베이커리로, 다양한 빵과 커피를 제공합니다.', '정기휴일에 유의하세요.', '02-9012-3456', 4.4, 'GENERAL_RESERVATION'),
    ('하이디 케이크 본점', '서울특별시 강남구 역삼동 012-34', '09:00:00', null, null, '18:00:00', '19:00:00', '화요일', 'heidi_cake_thumbnail.jpg', 'store_cake.jpg', '프리미엄 케이크와 디저트를 전문으로 하는 매장입니다.', '특별한 날에는 미리 예약 부탁드립니다.', '02-0123-4567', 4.6, 'CUSTOM');

-- 일반빵 데이터 삽입
# INSERT INTO general_menu (menu_name, menu_price, image, menu_type) VALUES
#     ('치즈빵', 3000, 'cheese_bread.jpg', 일반빵),
#     ('치아바타', 3500, 'ciabatta.jpg', 일반빵),
#     ('초코빵', 3200, 'choco_bread.jpg', 일반빵),
#     ('크로와상', 4000, 'croissant.jpg', 일반빵),
#     ('식빵', 2800, 'bread.jpg', 일반빵),
#     ('초코머핀', 3700, 'choco_muffin.jpg', 일반빵),
#     ('피자빵', 4500, 'pizza_bread.jpg', 일반빵),
#     ('앙버터 라우겐', 3800, 'angbutter_laugen.jpg', 일반빵),
#     ('바게트', 3000, 'baguette.jpg', 일반빵),
#     ('호두 호밀빵', 3500, 'walnut_rye_bread.jpg', 일반빵);
#
# -- 제작케이크 데이터 삽입
# INSERT INTO custom_menu (menu_name, menu_price, image, menu_type, sheet, size, cream, lettering) VALUES
#      ('커스텀 초콜릿 케이크', 35000, 'chocolate_cake.jpg', '제작케이크', '초콜릿 시트', '6', '초코 크림', 'Happy Birthday'),
#      ('커스텀 바닐라 케이크', 30000, 'vanilla_cake.jpg', '제작케이크', '바닐라 시트', '5', '바닐라 크림', 'Congratulations'),
#      ('커스텀 딸기 케이크', 32000, 'strawberry_cake.jpg', '제작케이크', '바닐라 시트', '6', '딸기 크림', 'Best Wishes'),
#      ('커스텀 레드벨벳 케이크', 36000, 'redvelvet_cake.jpg', '제작케이크', '레드벨벳 시트', '4', '크림치즈 크림', 'Love You'),
#      ('커스텀 치즈 케이크', 34000, 'cheesecake.jpg', '제작케이크', '치즈 시트', '3', '치즈 크림', 'Congratulations'),
#      ('커스텀 마카롱 케이크', 38000, 'macaron_cake.jpg', '제작케이크', '마카롱 시트', '2', '버터 크림', 'Happy Anniversary'),
#      ('커스텀 티라미수 케이크', 37000, 'tiramisu_cake.jpg', '제작케이크', '마스카포네 시트', '2', '마스카포네 크림', 'Cheers'),
#      ('커스텀 캐러멜 케이크', 33000, 'caramel_cake.jpg', '제작케이크', '카라멜 시트', '5', '카라멜 크림', 'Get Well Soon'),
#      ('커스텀 모카 케이크', 34000, 'mocha_cake.jpg', '제작케이크', '모카 시트', '1', '모카 크림', 'Happy Graduation'),
#      ('커스텀 초코 바나나 케이크', 36000, 'choco_banana_cake.jpg', '제작케이크', '초콜릿 시트', '3', '초코 바나나 크림', 'Celebrate');
commit;

# -- 테이블 삭제
# DROP TABLE IF EXISTS tbl_payment_order CASCADE;
# DROP TABLE IF EXISTS tbl_payment CASCADE;
# DROP TABLE IF EXISTS tbl_order_menu CASCADE;
# DROP TABLE IF EXISTS tbl_order CASCADE;
# DROP TABLE IF EXISTS tbl_menu CASCADE;
# DROP TABLE IF EXISTS tbl_category CASCADE;
#
# -- 테이블 생성
# -- category 테이블 생성
# CREATE TABLE IF NOT EXISTS tbl_category
# (
#     category_code    INT AUTO_INCREMENT COMMENT '카테고리코드',
#     category_name    VARCHAR(30) NOT NULL COMMENT '카테고리명',
#     ref_category_code    INT COMMENT '상위카테고리코드',
#     CONSTRAINT pk_category_code PRIMARY KEY (category_code),
#     CONSTRAINT fk_ref_category_code FOREIGN KEY (ref_category_code) REFERENCES tbl_category (category_code)
#     ) ENGINE=INNODB COMMENT '카테고리';
#
# CREATE TABLE IF NOT EXISTS tbl_menu
# (
#     menu_code    INT AUTO_INCREMENT COMMENT '메뉴코드',
#     menu_name    VARCHAR(30) NOT NULL COMMENT '메뉴명',
#     menu_price    INT NOT NULL COMMENT '메뉴가격',
#     category_code    INT COMMENT '카테고리코드',
#     orderable_status    CHAR(1) NOT NULL COMMENT '주문가능상태',
#     CONSTRAINT pk_menu_code PRIMARY KEY (menu_code),
#     CONSTRAINT fk_category_code FOREIGN KEY (category_code) REFERENCES tbl_category (category_code)
#     ) ENGINE=INNODB COMMENT '메뉴';
#
#
# CREATE TABLE IF NOT EXISTS tbl_order
# (
#     order_code    INT AUTO_INCREMENT COMMENT '주문코드',
#     order_date    DATE NOT NULL COMMENT '주문일자',
#     order_time    TIME NOT NULL COMMENT '주문시간',
#     total_order_price    INT NOT NULL COMMENT '총주문금액',
#     CONSTRAINT pk_order_code PRIMARY KEY (order_code)
#     ) ENGINE=INNODB COMMENT '주문';
#
#
# CREATE TABLE IF NOT EXISTS tbl_order_menu
# (
#     order_code INT NOT NULL COMMENT '주문코드',
#     menu_code    INT NOT NULL COMMENT '메뉴코드',
#     order_amount    INT NOT NULL COMMENT '주문수량',
#     CONSTRAINT pk_comp_order_menu_code PRIMARY KEY (order_code, menu_code),
#     CONSTRAINT fk_order_menu_order_code FOREIGN KEY (order_code) REFERENCES tbl_order (order_code),
#     CONSTRAINT fk_order_menu_menu_code FOREIGN KEY (menu_code) REFERENCES tbl_menu (menu_code)
#     ) ENGINE=INNODB COMMENT '주문별메뉴';
#
#
# CREATE TABLE IF NOT EXISTS tbl_payment
# (
#     payment_code    INT AUTO_INCREMENT COMMENT '결제코드',
#     payment_date    VARCHAR(8) NOT NULL COMMENT '결제일',
#     payment_time    VARCHAR(8) NOT NULL COMMENT '결제시간',
#     payment_price    INT NOT NULL COMMENT '결제금액',
#     payment_type    VARCHAR(6) NOT NULL COMMENT '결제구분',
#     CONSTRAINT pk_payment_code PRIMARY KEY (payment_code)
#     ) ENGINE=INNODB COMMENT '결제';
#
#
# CREATE TABLE IF NOT EXISTS tbl_payment_order
# (
#     order_code    INT NOT NULL COMMENT '주문코드',
#     payment_code    INT NOT NULL COMMENT '결제코드',
#     CONSTRAINT pk_comp_payment_order_code PRIMARY KEY (payment_code, order_code),
#     CONSTRAINT fk_payment_order_order_code FOREIGN KEY (order_code) REFERENCES tbl_order (order_code),
#     CONSTRAINT fk_payment_order_payment_code FOREIGN KEY (order_code) REFERENCES tbl_payment (payment_code)
#     ) ENGINE=INNODB COMMENT '결제별주문';
#
# -- 데이터 삽입
# INSERT INTO tbl_category VALUES (null, '식사', null);
# INSERT INTO tbl_category VALUES (null, '음료', null);
# INSERT INTO tbl_category VALUES (null, '디저트', null);
# INSERT INTO tbl_category VALUES (null, '한식', 1);
# INSERT INTO tbl_category VALUES (null, '중식', 1);
#
# INSERT INTO tbl_category VALUES (null, '일식', 1);
# INSERT INTO tbl_category VALUES (null, '퓨전', 1);
# INSERT INTO tbl_category VALUES (null, '커피', 2);
# INSERT INTO tbl_category VALUES (null, '쥬스', 2);
# INSERT INTO tbl_category VALUES (null, '기타', 2);
#
# INSERT INTO tbl_category VALUES (null, '동양', 3);
# INSERT INTO tbl_category VALUES (null, '서양', 3);
#
# INSERT INTO tbl_menu VALUES (null, '열무김치라떼', 4500, 8, 'Y');
# INSERT INTO tbl_menu VALUES (null, '우럭스무디', 5000, 10, 'Y');
# INSERT INTO tbl_menu VALUES (null, '생갈치쉐이크', 6000, 10, 'Y');
# INSERT INTO tbl_menu VALUES (null, '갈릭미역파르페', 7000, 10, 'Y');
# INSERT INTO tbl_menu VALUES (null, '앙버터김치찜', 13000, 4, 'N');
#
# INSERT INTO tbl_menu VALUES (null, '생마늘샐러드', 12000, 4, 'Y');
# INSERT INTO tbl_menu VALUES (null, '민트미역국', 15000, 4, 'Y');
# INSERT INTO tbl_menu VALUES (null, '한우딸기국밥', 20000, 4, 'Y');
# INSERT INTO tbl_menu VALUES (null, '홍어마카롱', 9000, 12, 'Y');
# INSERT INTO tbl_menu VALUES (null, '코다리마늘빵', 7000, 12, 'N');
#
# INSERT INTO tbl_menu VALUES (null, '정어리빙수', 10000, 10, 'Y');
# INSERT INTO tbl_menu VALUES (null, '날치알스크류바', 2000, 10, 'Y');
# INSERT INTO tbl_menu VALUES (null, '직화구이젤라또', 8000, 12, 'Y');
# INSERT INTO tbl_menu VALUES (null, '과메기커틀릿', 13000, 6, 'Y');
# INSERT INTO tbl_menu VALUES (null, '죽방멸치튀김우동', 11000, 6, 'N');
#
# INSERT INTO tbl_menu VALUES (null, '흑마늘아메리카노', 9000, 8, 'Y');
# INSERT INTO tbl_menu VALUES (null, '아이스가리비관자육수', 6000, 10, 'Y');
# INSERT INTO tbl_menu VALUES (null, '붕어빵초밥', 35000, 6, 'Y');
# INSERT INTO tbl_menu VALUES (null, '까나리코코넛쥬스', 9000, 9, 'Y');
# INSERT INTO tbl_menu VALUES (null, '마라깐쇼한라봉', 22000, 5, 'N');
#
# INSERT INTO tbl_menu VALUES (null, '돌미나리백설기', 5000, 11, 'Y');
# INSERT INTO tbl_menu VALUES (null, '찰순대쥬스', 7000, null, 'Y');
#
# COMMIT;