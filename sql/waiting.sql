ALTER TABLE waiting_storage
    MODIFY id BIGINT AUTO_INCREMENT;


-- 재귀 깊이를 늘려서 더 많은 데이터를 생성할 수 있도록 설정
SET SESSION cte_max_recursion_depth = 1000000;

-- waiting_storage 테이블에 100만 건의 더미 데이터 삽입
INSERT INTO waiting_storage (saved_date, canceled_time, created_date, customer_id, customer_phone, people_count, store_id, visited_time, waiting_number, waiting_order, waiting_status)
WITH RECURSIVE cte AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 1000000
)
SELECT
            CURRENT_DATE - INTERVAL FLOOR(RAND() * 10) DAY, -- 저장된 날짜
            NULL, -- 취소된 날짜
            CURRENT_DATE - INTERVAL FLOOR(RAND() * 10) DAY, -- 생성된 날짜
            FLOOR(RAND() * 100000) + 2, -- 고객 ID
            CONCAT('010-', LPAD(FLOOR(RAND() * 10000), 4, '0'), '-', LPAD(FLOOR(RAND() * 10000), 4, '0')), -- 고객 전화번호
            FLOOR(RAND() * 10) + 1, -- 인원 수
            FLOOR(RAND() * 1000) + 1, -- 매장 ID
            CURRENT_DATE - INTERVAL FLOOR(RAND() * 10) DAY, -- 방문 시간
            FLOOR(RAND() * 100000) + 1, -- 대기 번호
            FLOOR(RAND() * 100000) + 1, -- 대기 순서
            ELT(FLOOR(RAND() * 5) + 1, 'CUSTOMER_CANCELED', 'NO_SHOW', 'SHOP_CANCELED', 'VISITED', 'WAITING') -- 대기 상태
FROM cte;

select count(*)
from waiting_storage;
