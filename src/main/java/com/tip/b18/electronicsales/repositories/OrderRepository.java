package com.tip.b18.electronicsales.repositories;

import com.tip.b18.electronicsales.entities.Order;
import com.tip.b18.electronicsales.enums.Delivery;
import com.tip.b18.electronicsales.enums.PaymentMethod;
import com.tip.b18.electronicsales.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE (o.orderCode LIKE CONCAT('%', :search, '%') OR o.account.phoneNumber LIKE CONCAT('%', :search, '%')) " +
            "AND (:accountId IS NULL OR o.account.id = :accountId)" +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:paymentMethod IS NULL OR o.paymentMethod = :paymentMethod) " +
            "AND (:delivery IS NULL OR o.delivery = :delivery)")
    Page<Order> findAllByConditions(
            @Param("search") String search,
            @Param("accountId") UUID accountId,
            @Param("status") Status status,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("delivery") Delivery delivery,
            Pageable pageable);

    Optional<Order> findByIdAndAccountId(UUID id, UUID accountId);
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt > :startDay AND o.createdAt < :endDay AND o.status <> :status")
    int countQuantityNewOrders(LocalDateTime startDay, LocalDateTime endDay, Status status);
}
