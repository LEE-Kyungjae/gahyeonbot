package com.gahyeonbot.core.scheduler;

import com.gahyeonbot.models.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class LeaveSchedulerManagerImpl implements LeaveSchedulerManager {

    private static final Logger logger = LoggerFactory.getLogger(LeaveSchedulerManagerImpl.class);

    // Member ID -> Reservation ID -> Reservation 구조
    private final Map<Long, Map<Long, Reservation>> reservations = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final ReentrantLock lock = new ReentrantLock();
    private long nextId = 1;

    @Override
    public long addReservation(Reservation reservation) {
        lock.lock();
        try {
            reservations
                    .computeIfAbsent(reservation.getMemberId(), k -> new ConcurrentHashMap<>())
                    .put(reservation.getId(), reservation);
            logger.info("예약 추가: {}", reservation);
            return reservation.getId();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean cancelReservation(long reservationId) {
        for (var memberReservations : reservations.values()) {
            Reservation reservation = memberReservations.remove(reservationId);
            if (reservation != null) {
                reservation.getTask().cancel(false);
                logger.info("예약 취소: {}", reservationId);
                return true;
            }
        }
        logger.warn("예약 ID {}를 찾을 수 없습니다.", reservationId);
        return false;
    }

    @Override
    public List<Reservation> getReservations(long memberId) {
        var memberReservations = reservations.get(memberId);
        return memberReservations != null ? new ArrayList<>(memberReservations.values()) : Collections.emptyList();
    }

    @Override
    public ScheduledFuture<?> scheduleTask(Runnable task, long delay, TimeUnit unit) {
        logger.info("예약된 작업: {}ms 후 실행", unit.toMillis(delay));
        return scheduler.schedule(() -> {
            task.run();
            cleanUpCompletedReservations(task);
        }, delay, unit);
    }

    @Override
    public long generateId() {
        lock.lock();
        try {
            return nextId++;
        } finally {
            lock.unlock();
        }
    }

    private void cleanUpCompletedReservations(Runnable task) {
        lock.lock();
        try {
            reservations.values().forEach(memberReservations ->
                    memberReservations.entrySet().removeIf(entry -> entry.getValue().getTask().equals(task))
            );
            logger.info("완료된 예약 정리: {}", task);
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        logger.info("스케줄러 종료");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("스케줄러 종료 중단", e);
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void logCurrentReservations() {
        logger.info("현재 예약 상태:");
        reservations.forEach((memberId, memberReservations) ->
                logger.info("Member ID {}: {}", memberId, memberReservations.values())
        );
    }
}
