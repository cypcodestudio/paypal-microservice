package com.cypcode.paypal_microservice.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.cypcode.paypal_microservice.domain.PaypalDTO;
import com.cypcode.paypal_microservice.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PaypalController {

	@Autowired
	private PaypalService paypalService;
	
	@Value("${app.cancel.url}")
	private String cancelUrl;

	@Value("${app.success.url}")
	private String successUrl;
	
	
	
	@GetMapping
	public String home () {
		return "index";
	}
	
	@PostMapping("pay")
	public RedirectView sendPayment( @RequestParam("method") String method,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description){
		try {
			PaypalDTO payload = PaypalDTO.builder()
					.amount(Double.valueOf(amount))
					.method(method)
					.currency(currency)
					.description(description)
					.cancelUrl(cancelUrl)
					.successUrl(successUrl)
					.intent("sale")
					.build();
					
			Payment payment = paypalService.createPayment(payload);
			
			Links link = payment.getLinks().stream()
					.filter(l -> l.getRel().equals("approval_url"))
					.findFirst()
					.orElse(null);
			
			if(link != null) {
				return new RedirectView(link.getHref());
			}
			
		}catch (Exception e) {
			log.error("Payment Failed: " + e.getMessage());
		}


        return new RedirectView("error");
	}
		
	@GetMapping("success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return "error";
    }

    @GetMapping("cancel")
    public String paymentCancel() {
        return "cancel";
    }

    @GetMapping("error")
    public String paymentError() {
        return "error";
    }
}
