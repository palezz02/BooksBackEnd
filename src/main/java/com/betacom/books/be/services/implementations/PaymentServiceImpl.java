package com.betacom.books.be.services.implementations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.PaymentDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.models.Payment;
import com.betacom.books.be.repositories.IOrderRepository;
import com.betacom.books.be.repositories.IPaymentRepository;
import com.betacom.books.be.requests.CreatePaymentIntentReq;
import com.betacom.books.be.requests.ConfirmPaymentReq;
import com.betacom.books.be.services.interfaces.IPaymentService;
import com.betacom.books.be.utils.Status;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PaymentServiceImpl implements IPaymentService {

    @Value("${stripe.secret.key:}")
    private String stripeSecretKey;

    private final IPaymentRepository paymentRepository;
    private final IOrderRepository orderRepository;

    // Constructor injection
    public PaymentServiceImpl(IPaymentRepository paymentRepository, IOrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentDTO createPaymentIntent(CreatePaymentIntentReq req) throws BooksException {
        log.debug("Creating payment intent: {}", req);
        
        // Validate Stripe key
        if (stripeSecretKey == null || stripeSecretKey.trim().isEmpty()) {
            throw new BooksException("Stripe secret key not configured");
        }
        
        try {
            Stripe.apiKey = stripeSecretKey;

            // Convert amount to cents (Stripe works with smallest currency unit)
            Long amountInCents = req.getAmount().multiply(BigDecimal.valueOf(100)).longValue();

            PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(req.getCurrency().toLowerCase())
                .setDescription(req.getDescription());

            if (req.getCustomerEmail() != null && !req.getCustomerEmail().trim().isEmpty()) {
                paramsBuilder.setReceiptEmail(req.getCustomerEmail());
            }

            // Add metadata
            Map<String, String> metadata = new HashMap<>();
            if (req.getOrderId() != null) {
                metadata.put("order_id", req.getOrderId().toString());
            }
            metadata.put("source", "bookstore");
//            paramsBuilder.setMetadata(metadata);
            paramsBuilder.putMetadata("source", "bookstore");

            PaymentIntent intent = PaymentIntent.create(paramsBuilder.build());

            // Save payment record
            Payment payment = new Payment();
            payment.setStripePaymentIntentId(intent.getId());
            payment.setAmount(req.getAmount());
            payment.setCurrency(req.getCurrency());
            payment.setStatus("pending");
            payment.setClientSecret(intent.getClientSecret());
            payment.setCreatedAt(LocalDateTime.now());

            if (req.getOrderId() != null) {
                Optional<Order> order = orderRepository.findById(req.getOrderId());
                if (order.isPresent()) {
                    payment.setOrder(order.get());
                } else {
                    throw new BooksException("Order not found with id: " + req.getOrderId());
                }
            }

            Payment savedPayment = paymentRepository.save(payment);

            return PaymentDTO.builder()
                .id(savedPayment.getId().toString())
                .orderId(req.getOrderId())
                .amount(savedPayment.getAmount())
                .currency(savedPayment.getCurrency())
                .status(savedPayment.getStatus())
                .paymentIntentId(savedPayment.getStripePaymentIntentId())
                .clientSecret(savedPayment.getClientSecret())
                .createdAt(savedPayment.getCreatedAt())
                .build();

        } catch (StripeException e) {
            log.error("Stripe error creating payment intent: {}", e.getMessage());
            throw new BooksException("Failed to create payment intent: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error creating payment intent: {}", e.getMessage());
            throw new BooksException("Failed to create payment intent: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentDTO confirmPayment(ConfirmPaymentReq req) throws BooksException {
        log.debug("Confirming payment: {}", req);

        if (stripeSecretKey == null || stripeSecretKey.trim().isEmpty()) {
            throw new BooksException("Stripe secret key not configured");
        }

        try {
            Stripe.apiKey = stripeSecretKey;

            PaymentIntent intent = PaymentIntent.retrieve(req.getPaymentIntentId());
            
            Optional<Payment> paymentOpt = paymentRepository.findByStripePaymentIntentId(req.getPaymentIntentId());
            if (paymentOpt.isEmpty()) {
                throw new BooksException("Payment not found for intent: " + req.getPaymentIntentId());
            }

            Payment payment = paymentOpt.get();
            payment.setStatus(intent.getStatus());
            payment.setUpdatedAt(LocalDateTime.now());

            if ("succeeded".equals(intent.getStatus())) {
                // Update order status to paid
                if (payment.getOrder() != null) {
                    Order order = payment.getOrder();
                    order.setStatus(Status.COMPLETED);
                    order.setUpdatedAt(LocalDate.now());
                    orderRepository.save(order);
                }
            } else if ("payment_failed".equals(intent.getStatus())) {
                payment.setErrorMessage("Payment failed");
            }

            Payment savedPayment = paymentRepository.save(payment);

            return PaymentDTO.builder()
                .id(savedPayment.getId().toString())
                .orderId(savedPayment.getOrder() != null ? savedPayment.getOrder().getId() : null)
                .amount(savedPayment.getAmount())
                .currency(savedPayment.getCurrency())
                .status(savedPayment.getStatus())
                .paymentIntentId(savedPayment.getStripePaymentIntentId())
                .createdAt(savedPayment.getCreatedAt())
                .errorMessage(savedPayment.getErrorMessage())
                .build();

        } catch (StripeException e) {
            log.error("Stripe error confirming payment: {}", e.getMessage());
            throw new BooksException("Failed to confirm payment: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error confirming payment: {}", e.getMessage());
            throw new BooksException("Failed to confirm payment: " + e.getMessage());
        }
    }

    @Override
    public PaymentDTO getPaymentByOrderId(Integer orderId) throws BooksException {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isEmpty()) {
            throw new BooksException("Payment not found for order: " + orderId);
        }

        Payment payment = paymentOpt.get();
        return PaymentDTO.builder()
            .id(payment.getId().toString())
            .orderId(payment.getOrder().getId())
            .amount(payment.getAmount())
            .currency(payment.getCurrency())
            .status(payment.getStatus())
            .paymentIntentId(payment.getStripePaymentIntentId())
            .createdAt(payment.getCreatedAt())
            .errorMessage(payment.getErrorMessage())
            .build();
    }
}