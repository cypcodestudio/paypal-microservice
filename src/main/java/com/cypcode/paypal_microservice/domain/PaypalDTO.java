package com.cypcode.paypal_microservice.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaypalDTO implements Serializable{

	private double amount;
	private String currency;
	private String method;
	private String intent;
	private String description;
	private String cancelUrl;
	private String successUrl;
}
