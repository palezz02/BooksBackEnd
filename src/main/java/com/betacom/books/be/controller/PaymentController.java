package com.betacom.books.be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.books.be.dto.PaymentDTO;
import com.betacom.books.be.requests.CreatePaymentIntentReq;
import com.betacom.books.be.requests.ConfirmPaymentReq;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.implementations.PaymentServiceImpl;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/rest/payment")
@Log4j2
public class PaymentController {

    private final PaymentServiceImpl paymentServiceImpl;

    public PaymentController(PaymentServiceImpl paymentServiceImpl) {
        this.paymentServiceImpl = paymentServiceImpl;
    }

    @PostMapping("/create-intent")
    public ResponseObject<PaymentDTO> createPaymentIntent(@RequestBody CreatePaymentIntentReq req) {
        ResponseObject<PaymentDTO> response = new ResponseObject<>();
        try {
            PaymentDTO payment = paymentServiceImpl.createPaymentIntent(req);
            response.setDati(payment);
            response.setRc(true);
        } catch (Exception e) {
            log.error("Error creating payment intent: {}", e.getMessage());
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return response;
    }

    @PostMapping("/confirm")
    public ResponseObject<PaymentDTO> confirmPayment(@RequestBody ConfirmPaymentReq req) {
        ResponseObject<PaymentDTO> response = new ResponseObject<>();
        try {
            PaymentDTO payment = paymentServiceImpl.confirmPayment(req);
            response.setDati(payment);
            response.setRc(true);
        } catch (Exception e) {
            log.error("Error confirming payment: {}", e.getMessage());
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return response;
    }

    @GetMapping("/by-order")
    public ResponseObject<PaymentDTO> getPaymentByOrder(@RequestParam Integer orderId) {
        ResponseObject<PaymentDTO> response = new ResponseObject<>();
        try {
            PaymentDTO payment = paymentServiceImpl.getPaymentByOrderId(orderId);
            response.setDati(payment);
            response.setRc(true);
        } catch (Exception e) {
            log.error("Error getting payment by order: {}", e.getMessage());
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return response;
    }
}