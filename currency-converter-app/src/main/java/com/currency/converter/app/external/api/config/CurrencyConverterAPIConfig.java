package com.currency.converter.app.external.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.posadskiy.currencyconverter.CurrencyConverter;
import com.posadskiy.currencyconverter.config.ConfigBuilder;

@Component
public class CurrencyConverterAPIConfig {

	@Value("${currency.converter.api.key}")
	private String currencyConverterApiKey;

	@Value("${currency.layer.api.key}")
	private String currencyLayerApiKey;

	/*
	 * Get a new currency converter instance configured with two external converter
	 * API connection, <br> Note: It will work with any one also
	 */
	public CurrencyConverter getCurrencyConverter() {
		return new CurrencyConverter(new ConfigBuilder().currencyConverterApiApiKey(currencyConverterApiKey)
				.currencyLayerApiKey(currencyLayerApiKey).build());
	}
}
