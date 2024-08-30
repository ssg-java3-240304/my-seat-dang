package com.matdang.seatdang.reservation.repository;


import com.matdang.seatdang.reservation.entity.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationSlotLockRepository extends JpaRepository<ReservationSlot, Long> {
    // 1초동안 Named Lock 획득을 시도
    @Query(value = "select get_lock(:key, 100)", nativeQuery = true)
    Integer getLock(String key);    // 성공시 1, 실패시 0 반환
    // 락 해제에 성공하는 경우 1을 반환하지만, 락이 없는 경우 등에 대해서는 NULL이나 0을 반환
    @Query(value = "select release_lock(:key)", nativeQuery = true)
    Integer releaseLock(String key);
}
