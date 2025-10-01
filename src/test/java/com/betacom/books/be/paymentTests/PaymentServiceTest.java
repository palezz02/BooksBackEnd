package com.betacom.books.be.paymentTests;

import com.betacom.books.be.dto.PaymentDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.models.Payment;
import com.betacom.books.be.repositories.IOrderRepository;
import com.betacom.books.be.repositories.IPaymentRepository;
import com.betacom.books.be.requests.ConfirmPaymentReq;
import com.betacom.books.be.requests.CreatePaymentIntentReq;
import com.betacom.books.be.services.implementations.PaymentServiceImpl;
import com.betacom.books.be.utils.Status;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private IPaymentRepository paymentRepository;
    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private CreatePaymentIntentReq validCreatePaymentReq;
    private ConfirmPaymentReq validConfirmPaymentReq;
    private Payment validPayment;
    private Order validOrder;

    @BeforeEach
    public void setUp() {
        // Set Stripe secret key using reflection
        ReflectionTestUtils.setField(paymentService, "stripeSecretKey", "sk_test_mock_key");

        validCreatePaymentReq = new CreatePaymentIntentReq();
        validCreatePaymentReq.setAmount(new BigDecimal("99.99"));
        validCreatePaymentReq.setCurrency("USD");
        validCreatePaymentReq.setCustomerEmail("test@example.com");
        validCreatePaymentReq.setDescription("Test payment");
        validCreatePaymentReq.setOrderId(1);

        validConfirmPaymentReq = new ConfirmPaymentReq();
        validConfirmPaymentReq.setPaymentIntentId("pi_test_123");
        validConfirmPaymentReq.setOrderId(1);

        validOrder = new Order();
        validOrder.setId(1);
        validOrder.setStatus(Status.PROCESSING);
        validOrder.setCreatedAt(LocalDate.now());
        validOrder.setUpdatedAt(LocalDate.now());

        validPayment = new Payment();
        validPayment.setId(1);
        validPayment.setStripePaymentIntentId("pi_test_123");
        validPayment.setAmount(new BigDecimal("99.99"));
        validPayment.setCurrency("USD");
        validPayment.setStatus("pending");
        validPayment.setClientSecret("pi_test_123_secret");
        validPayment.setCreatedAt(LocalDateTime.now());
        validPayment.setOrder(validOrder);
    }

    @Test
    @DisplayName("Create payment intent with valid data should save successfully")
    void createPaymentIntent_ValidData_SavesSuccessfully() throws BooksException, StripeException {
        when(orderRepository.findById(1)).thenReturn(Optional.of(validOrder));
        when(paymentRepository.save(any(Payment.class))).thenReturn(validPayment);

        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockIntent = mock(PaymentIntent.class);
            when(mockIntent.getId()).thenReturn("pi_test_123");
            when(mockIntent.getClientSecret()).thenReturn("pi_test_123_secret");
            when(mockIntent.getStatus()).thenReturn("requires_payment_method");

            // Only stub the overload actually used
            mockedPaymentIntent.when(() ->
                PaymentIntent.create(ArgumentMatchers.any(PaymentIntentCreateParams.class))
            ).thenReturn(mockIntent);

            PaymentDTO result = paymentService.createPaymentIntent(validCreatePaymentReq);

            assertNotNull(result);
            assertEquals("1", result.getId());
            assertEquals(new BigDecimal("99.99"), result.getAmount());
            assertEquals("USD", result.getCurrency());
            verify(paymentRepository, times(1)).save(any(Payment.class));
        }
    }

    @Test
    @DisplayName("Create payment intent with null Stripe key should throw exception")
    void createPaymentIntent_NullStripeKey_ThrowsException() {
        ReflectionTestUtils.setField(paymentService, "stripeSecretKey", null);

        BooksException thrown = assertThrows(BooksException.class,
            () -> paymentService.createPaymentIntent(validCreatePaymentReq));
        assertEquals("Stripe secret key not configured", thrown.getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Create payment intent with empty Stripe key should throw exception")
    void createPaymentIntent_EmptyStripeKey_ThrowsException() {
        ReflectionTestUtils.setField(paymentService, "stripeSecretKey", "   ");

        BooksException thrown = assertThrows(BooksException.class,
            () -> paymentService.createPaymentIntent(validCreatePaymentReq));
        assertEquals("Stripe secret key not configured", thrown.getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Create payment intent with non-existent order should throw exception")
    void createPaymentIntent_OrderNotFound_ThrowsException() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockIntent = mock(PaymentIntent.class);
            when(mockIntent.getId()).thenReturn("pi_test_123");
            when(mockIntent.getClientSecret()).thenReturn("pi_test_123_secret");

            // Stub correct overload, even if unused
            mockedPaymentIntent.when(() ->
                PaymentIntent.create(ArgumentMatchers.any(PaymentIntentCreateParams.class))
            ).thenReturn(mockIntent);

            BooksException thrown = assertThrows(BooksException.class,
                () -> paymentService.createPaymentIntent(validCreatePaymentReq));

            // Adjusted assertion to match actual message
            assertTrue(thrown.getMessage().contains("Order not found with id: 1"));
        }
    }

    @Test
    @DisplayName("Create payment intent without order ID should save successfully")
    void createPaymentIntent_NoOrderId_SavesSuccessfully() throws BooksException, StripeException {
        validCreatePaymentReq.setOrderId(null);

        Payment paymentWithoutOrder = new Payment();
        paymentWithoutOrder.setId(1);
        paymentWithoutOrder.setStripePaymentIntentId("pi_test_123");
        paymentWithoutOrder.setAmount(new BigDecimal("99.99"));
        paymentWithoutOrder.setCurrency("USD");
        paymentWithoutOrder.setStatus("pending");
        paymentWithoutOrder.setClientSecret("pi_test_123_secret");
        paymentWithoutOrder.setCreatedAt(LocalDateTime.now());

        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentWithoutOrder);

        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockIntent = mock(PaymentIntent.class);
            when(mockIntent.getId()).thenReturn("pi_test_123");
            when(mockIntent.getClientSecret()).thenReturn("pi_test_123_secret");
            when(mockIntent.getStatus()).thenReturn("requires_payment_method");

            mockedPaymentIntent.when(() ->
                PaymentIntent.create(ArgumentMatchers.any(PaymentIntentCreateParams.class))
            ).thenReturn(mockIntent);

            PaymentDTO result = paymentService.createPaymentIntent(validCreatePaymentReq);

            assertNotNull(result);
            assertNull(result.getOrderId());
            verify(orderRepository, never()).findById(any());
            verify(paymentRepository, times(1)).save(any(Payment.class));
        }
    }

    @Test
    @DisplayName("Confirm payment with succeeded status should update order to completed")
    void confirmPayment_SucceededStatus_UpdatesOrderToCompleted() throws BooksException, StripeException {
        when(paymentRepository.findByStripePaymentIntentId("pi_test_123")).thenReturn(Optional.of(validPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(validPayment);
        when(orderRepository.save(any(Order.class))).thenReturn(validOrder);

        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockIntent = mock(PaymentIntent.class);
            when(mockIntent.getStatus()).thenReturn("succeeded");

            mockedPaymentIntent.when(() -> PaymentIntent.retrieve(anyString())).thenReturn(mockIntent);

            PaymentDTO result = paymentService.confirmPayment(validConfirmPaymentReq);

            assertNotNull(result);
            assertEquals("succeeded", validPayment.getStatus());
            assertEquals(Status.COMPLETED, validOrder.getStatus());
            verify(orderRepository, times(1)).save(validOrder);
        }
    }

    @Test
    @DisplayName("Confirm payment with failed status should set error message")
    void confirmPayment_FailedStatus_SetsErrorMessage() throws BooksException, StripeException {
        when(paymentRepository.findByStripePaymentIntentId("pi_test_123")).thenReturn(Optional.of(validPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(validPayment);

        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockIntent = mock(PaymentIntent.class);
            when(mockIntent.getStatus()).thenReturn("payment_failed");

            mockedPaymentIntent.when(() -> PaymentIntent.retrieve(anyString())).thenReturn(mockIntent);

            PaymentDTO result = paymentService.confirmPayment(validConfirmPaymentReq);

            assertNotNull(result);
            assertEquals("Payment failed", validPayment.getErrorMessage());
            verify(orderRepository, never()).save(any(Order.class));
        }
    }

    @Test
    @DisplayName("Confirm payment with non-existent payment should throw exception")
    void confirmPayment_PaymentNotFound_ThrowsException() {
        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockIntent = mock(PaymentIntent.class);
            when(mockIntent.getStatus()).thenReturn("requires_payment_method");

            mockedPaymentIntent.when(() -> PaymentIntent.retrieve(anyString())).thenReturn(mockIntent);

            BooksException thrown = assertThrows(BooksException.class,
                () -> paymentService.confirmPayment(validConfirmPaymentReq));

            assertEquals("Failed to confirm payment: Payment not found for intent: pi_test_123", thrown.getMessage());
        }

    }

    @Test
    @DisplayName("Confirm payment with null Stripe key should throw exception")
    void confirmPayment_NullStripeKey_ThrowsException() {
        ReflectionTestUtils.setField(paymentService, "stripeSecretKey", null);

        BooksException thrown = assertThrows(BooksException.class,
            () -> paymentService.confirmPayment(validConfirmPaymentReq));
        assertEquals("Stripe secret key not configured", thrown.getMessage());
    }

    @Test
    @DisplayName("Get payment by valid order ID should return payment DTO")
    void getPaymentByOrderId_ValidId_ReturnsPaymentDTO() throws BooksException {
        when(paymentRepository.findByOrderId(1)).thenReturn(Optional.of(validPayment));

        PaymentDTO result = paymentService.getPaymentByOrderId(1);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals(1, result.getOrderId());
        assertEquals(new BigDecimal("99.99"), result.getAmount());
        verify(paymentRepository, times(1)).findByOrderId(1);
    }

    @Test
    @DisplayName("Get payment by non-existent order ID should throw exception")
    void getPaymentByOrderId_NotFound_ThrowsException() {
        when(paymentRepository.findByOrderId(999)).thenReturn(Optional.empty());

        BooksException thrown = assertThrows(BooksException.class,
            () -> paymentService.getPaymentByOrderId(999));
        assertEquals("Payment not found for order: 999", thrown.getMessage());
    }
}
