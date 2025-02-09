package com.cypcode.paypal_microservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cypcode.paypal_microservice.domain.PaypalDTO;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;

@Service
public class PaypalService {

	@Autowired
	private APIContext context;
	
	public Payment createPayment(PaypalDTO payload) throws PayPalRESTException {
		
		Amount amount = new Amount();
		amount.setCurrency(payload.getCurrency());
		amount.setTotal(String.format(Locale.forLanguageTag(payload.getCurrency()), "%.2f", payload.getAmount()));
		
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription(payload.getDescription());
		
		
		List<Transaction> transactionList = new ArrayList<>();
		transactionList.add(transaction);
		
		Payer payer = new Payer();
		payer.setPaymentMethod(payload.getMethod());
		
		
		RedirectUrls redirect = new RedirectUrls();
		redirect.setCancelUrl(payload.getCancelUrl());
		redirect.setReturnUrl(payload.getSuccessUrl());
		
		Payment payment = new Payment();
		payment.setIntent(payload.getIntent());
		payment.setPayer(payer);
		payment.setTransactions(transactionList);
		
		payment.setRedirectUrls(redirect);
		
		return payment.create(context);
	}
	
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		
		
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		
		return payment.execute(context, paymentExecution);
	}
}
