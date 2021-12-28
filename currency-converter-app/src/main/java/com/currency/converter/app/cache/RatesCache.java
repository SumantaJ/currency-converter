package com.currency.converter.app.cache;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class RatesCache {
	
	/* This value has been passed from application.properties which can be changed */
	@Value("${cache.expiry}")
	private int CACHE_DURATION;

	private Cache<String, BigDecimal> cache;

	@PostConstruct
	public void init() {
		if (cache == null) {					
			cache = CacheBuilder.newBuilder().expireAfterWrite(CACHE_DURATION, TimeUnit.MINUTES).build();
		}
	}

	/* Cache key example : EURUSD i.e. combination of desired currencies */
	public BigDecimal getCachedRate(String code) {
		return cache.getIfPresent(code);
	}

	public void cacheRate(String code, BigDecimal rate) {
		cache.put(code, rate);
	}
}
