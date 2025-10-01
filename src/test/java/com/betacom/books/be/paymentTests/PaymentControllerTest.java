package com.betacom.books.be.paymentTests;

import com.betacom.books.be.controller.PaymentController;
import com.betacom.books.be.dto.PaymentDTO;
import com.betacom.books.be.requests.ConfirmPaymentReq;
import com.betacom.books.be.requests.CreatePaymentIntentReq;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.implementations.PaymentServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentServiceImpl paymentService;

    @InjectMocks
    private PaymentController paymentController;

    // Reusable method to create a valid CreatePaymentIntentReq object
    private CreatePaymentIntentReq createValidPaymentIntentReq() {
        CreatePaymentIntentReq req = new CreatePaymentIntentReq();
        req.setAmount(new BigDecimal("99.99"));
        req.setCurrency("USD");
        req.setCustomerEmail("test@example.com");
        req.setDescription("Test payment for order");
        req.setOrderId(1);
        return req;
    }

    // Reusable method to create a valid ConfirmPaymentReq object
    private ConfirmPaymentReq createValidConfirmPaymentReq() {
        ConfirmPaymentReq req = new ConfirmPaymentReq();
        req.setPaymentIntentId("pi_test_123456789");
        req.setOrderId(1);
        return req;
    }

    // Reusable method to create a valid PaymentDTO object
    private PaymentDTO createValidPaymentDTO() {
        return PaymentDTO.builder()
                .id("1")
                .orderId(1)
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .status("pending")
                .paymentIntentId("pi_test_123456789")
                .clientSecret("pi_test_123456789_secret_abc123")
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Reusable method to create a succeeded PaymentDTO object
    private PaymentDTO createSucceededPaymentDTO() {
        return PaymentDTO.builder()
                .id("1")
                .orderId(1)
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .status("succeeded")
                .paymentIntentId("pi_test_123456789")
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Reusable method to create a failed PaymentDTO object
    private PaymentDTO createFailedPaymentDTO() {
        return PaymentDTO.builder()
                .id("1")
                .orderId(1)
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .status("payment_failed")
                .paymentIntentId("pi_test_123456789")
                .createdAt(LocalDateTime.now())
                .errorMessage("Payment failed")
                .build();
    }

    //region CREATE PAYMENT INTENT TESTS
    @Test
    @Order(1)
    public void createPaymentIntent_Success() throws Exception {
        log.debug("Test Create Payment Intent - Success");
        CreatePaymentIntentReq req = createValidPaymentIntentReq();
        PaymentDTO paymentDTO = createValidPaymentDTO();
        when(paymentService.createPaymentIntent(any(CreatePaymentIntentReq.class))).thenReturn(paymentDTO);

        ResponseObject<PaymentDTO> r = paymentController.createPaymentIntent(req);
        Assertions.assertThat(r.getRc()).isEqualTo(true);
        Assertions.assertThat(r.getMsg()).isNull();
        Assertions.assertThat(r.getDati()).isNotNull();
        Assertions.assertThat(r.getDati().getPaymentIntentId()).isEqualTo("pi_test_123456789");
        Assertions.assertThat(r.getDati().getStatus()).isEqualTo("pending");
        Assertions.assertThat(r.getDati().getClientSecret()).isNotNull();
        verify(paymentService, times(1)).createPaymentIntent(req);
    }

    @Test
    @Order(2)
    public void createPaymentIntent_ServiceError_ReturnsRcFalse() throws Exception {
        log.debug("Test Create Payment Intent - Failure");
        CreatePaymentIntentReq req = createValidPaymentIntentReq();
        doThrow(new RuntimeException("Stripe API error")).when(paymentService).createPaymentIntent(any(CreatePaymentIntentReq.class));

        ResponseObject<PaymentDTO> r = paymentController.createPaymentIntent(req);
        Assertions.assertThat(r.getRc()).isEqualTo(false);
        Assertions.assertThat(r.getMsg()).isEqualTo("Stripe API error");
        Assertions.assertThat(r.getDati()).isNull();
        verify(paymentService, times(1)).createPaymentIntent(req);
    }

    @Test
    @Order(3)
    public void createPaymentIntent_InvalidOrder_ReturnsRcFalse() throws Exception {
        log.debug("Test Create Payment Intent - Invalid Order");
        CreatePaymentIntentReq req = createValidPaymentIntentReq();
        req.setOrderId(999);
        doThrow(new IllegalArgumentException("Order not found with id: 999")).when(paymentService).createPaymentIntent(any(CreatePaymentIntentReq.class));

        ResponseObject<PaymentDTO> r = paymentController.createPaymentIntent(req);
        Assertions.assertThat(r.getRc()).isEqualTo(false);
        Assertions.assertThat(r.getMsg()).isEqualTo("Order not found with id: 999");
        Assertions.assertThat(r.getDati()).isNull();
        verify(paymentService, times(1)).createPaymentIntent(req);
    }

    @Test
    @Order(4)
    public void createPaymentIntent_WithoutOrderId_Success() throws Exception {
        log.debug("Test Create Payment Intent Without Order - Success");
        CreatePaymentIntentReq req = createValidPaymentIntentReq();
        req.setOrderId(null);
        PaymentDTO paymentDTO = createValidPaymentDTO();
        paymentDTO.setOrderId(null);
        when(paymentService.createPaymentIntent(any(CreatePaymentIntentReq.class))).thenReturn(paymentDTO);

        ResponseObject<PaymentDTO> r = paymentController.createPaymentIntent(req);
        Assertions.assertThat(r.getRc()).isEqualTo(true);
        Assertions.assertThat(r.getMsg()).isNull();
        Assertions.assertThat(r.getDati()).isNotNull();
        Assertions.assertThat(r.getDati().getOrderId()).isNull();
        verify(paymentService, times(1)).createPaymentIntent(req);
    }
    //endregion

    //region CONFIRM PAYMENT TESTS
    @Test
    @Order(5)
    public void confirmPayment_Success() throws Exception {
        log.debug("Test Confirm Payment - Success");
        ConfirmPaymentReq req = createValidConfirmPaymentReq();
        PaymentDTO paymentDTO = createSucceededPaymentDTO();
        when(paymentService.confirmPayment(any(ConfirmPaymentReq.class))).thenReturn(paymentDTO);

        ResponseObject<PaymentDTO> r = paymentController.confirmPayment(req);
        Assertions.assertThat(r.getRc()).isEqualTo(true);
        Assertions.assertThat(r.getMsg()).isNull();
        Assertions.assertThat(r.getDati()).isNotNull();
        Assertions.assertThat(r.getDati().getStatus()).isEqualTo("succeeded");
        Assertions.assertThat(r.getDati().getPaymentIntentId()).isEqualTo("pi_test_123456789");
        verify(paymentService, times(1)).confirmPayment(req);
    }

    @Test
    @Order(6)
    public void confirmPayment_ServiceError_ReturnsRcFalse() throws Exception {
        log.debug("Test Confirm Payment - Failure");
        ConfirmPaymentReq req = createValidConfirmPaymentReq();
        doThrow(new RuntimeException("Failed to confirm payment")).when(paymentService).confirmPayment(any(ConfirmPaymentReq.class));

        ResponseObject<PaymentDTO> r = paymentController.confirmPayment(req);
        Assertions.assertThat(r.getRc()).isEqualTo(false);
        Assertions.assertThat(r.getMsg()).isEqualTo("Failed to confirm payment");
        Assertions.assertThat(r.getDati()).isNull();
        verify(paymentService, times(1)).confirmPayment(req);
    }

    @Test
    @Order(7)
    public void confirmPayment_PaymentNotFound_ReturnsRcFalse() throws Exception {
        log.debug("Test Confirm Payment - Payment Not Found");
        ConfirmPaymentReq req = createValidConfirmPaymentReq();
        req.setPaymentIntentId("pi_nonexistent_999");
        doThrow(new IllegalArgumentException("Payment not found for intent: pi_nonexistent_999")).when(paymentService).confirmPayment(any(ConfirmPaymentReq.class));

        ResponseObject<PaymentDTO> r = paymentController.confirmPayment(req);
        Assertions.assertThat(r.getRc()).isEqualTo(false);
        Assertions.assertThat(r.getMsg()).isEqualTo("Payment not found for intent: pi_nonexistent_999");
        Assertions.assertThat(r.getDati()).isNull();
        verify(paymentService, times(1)).confirmPayment(req);
    }

    @Test
    @Order(8)
    public void confirmPayment_Failed_ReturnsFailedStatus() throws Exception {
        log.debug("Test Confirm Payment - Failed Status");
        ConfirmPaymentReq req = createValidConfirmPaymentReq();
        PaymentDTO paymentDTO = createFailedPaymentDTO();
        when(paymentService.confirmPayment(any(ConfirmPaymentReq.class))).thenReturn(paymentDTO);

        ResponseObject<PaymentDTO> r = paymentController.confirmPayment(req);
        Assertions.assertThat(r.getRc()).isEqualTo(true);
        Assertions.assertThat(r.getMsg()).isNull();
        Assertions.assertThat(r.getDati()).isNotNull();
        Assertions.assertThat(r.getDati().getStatus()).isEqualTo("payment_failed");
        Assertions.assertThat(r.getDati().getErrorMessage()).isEqualTo("Payment failed");
        verify(paymentService, times(1)).confirmPayment(req);
    }
    //endregion

    //region GET PAYMENT BY ORDER TESTS
    @Test
    @Order(9)
    public void getPaymentByOrder_Success_ReturnsPaymentDTO() throws Exception {
        log.debug("Test Get Payment By Order - Success");
        Integer orderId = 1;
        PaymentDTO paymentDTO = createValidPaymentDTO();
        when(paymentService.getPaymentByOrderId(orderId)).thenReturn(paymentDTO);

        ResponseObject<PaymentDTO> r = paymentController.getPaymentByOrder(orderId);
        Assertions.assertThat(r.getRc()).isEqualTo(true);
        Assertions.assertThat(r.getMsg()).isNull();
        Assertions.assertThat(r.getDati()).isNotNull();
        Assertions.assertThat(r.getDati().getOrderId()).isEqualTo(orderId);
        Assertions.assertThat(r.getDati().getAmount()).isEqualTo(new BigDecimal("99.99"));
        verify(paymentService, times(1)).getPaymentByOrderId(orderId);
    }

    @Test
    @Order(10)
    public void getPaymentByOrder_ServiceError_ReturnsRcFalse() throws Exception {
        log.debug("Test Get Payment By Order - Failure");
        Integer orderId = 999;
        when(paymentService.getPaymentByOrderId(orderId)).thenThrow(new IllegalArgumentException("Payment not found for order: 999"));

        ResponseObject<PaymentDTO> r = paymentController.getPaymentByOrder(orderId);
        Assertions.assertThat(r.getRc()).isEqualTo(false);
        Assertions.assertThat(r.getMsg()).isEqualTo("Payment not found for order: 999");
        Assertions.assertThat(r.getDati()).isNull();
        verify(paymentService, times(1)).getPaymentByOrderId(orderId);
    }

    @Test
    @Order(11)
    public void getPaymentByOrder_NullOrderId_ReturnsRcFalse() throws Exception {
        log.debug("Test Get Payment By Order - Null Order ID");
        when(paymentService.getPaymentByOrderId(null)).thenThrow(new IllegalArgumentException("Order ID cannot be null"));

        ResponseObject<PaymentDTO> r = paymentController.getPaymentByOrder(null);
        Assertions.assertThat(r.getRc()).isEqualTo(false);
        Assertions.assertThat(r.getMsg()).isEqualTo("Order ID cannot be null");
        Assertions.assertThat(r.getDati()).isNull();
        verify(paymentService, times(1)).getPaymentByOrderId(null);
    }
    //endregion
}