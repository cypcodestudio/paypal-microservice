package com.cypcode.paypal_microservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;

@Configuration
public class PaypalConfiguration {

	@Value("${paypal.client-id}")
	private String id;

	@Value("${paypal.client-secret}")
	private String secret;

	@Value("${paypal.client-mode}")
	private String mode;
	
	@Bean
	public APIContext context() {
		return new APIContext(id, secret, mode);
	}
}
