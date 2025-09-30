package com.betacom.books.be.services.interfaces;

import com.betacom.books.be.dto.PaymentDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.CreatePaymentIntentReq;
import com.betacom.books.be.requests.ConfirmPaymentReq;

public interface IPaymentService {
    PaymentDTO createPaymentIntent(CreatePaymentIntentReq req) throws BooksException;
    PaymentDTO confirmPayment(ConfirmPaymentReq req) throws BooksException;
    PaymentDTO getPaymentByOrderId(Integer orderId) throws BooksException;
}