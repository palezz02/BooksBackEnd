package com.betacom.books.be.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.betacom.books.be.models.Payment;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Integer> {
    
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
    
    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") Integer orderId);
    
    @Query("SELECT p FROM Payment p WHERE p.status = :status")
    Optional<Payment> findByStatus(@Param("status") String status);
}