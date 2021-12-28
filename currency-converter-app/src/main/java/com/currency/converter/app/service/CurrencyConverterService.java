package com.currency.converter.app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.currency.converter.app.cache.RatesCache;
import com.currency.converter.app.dto.ItemDTO;
import com.currency.converter.app.external.api.config.CurrencyConverterAPIConfig;
import com.posadskiy.currencyconverter.enums.Currency;
import com.posadskiy.currencyconverter.exception.CurrencyConverterException;

@Service
public class CurrencyConverterService {

	@Autowired
	CurrencyConverterAPIConfig currencyConverterAPIConfig;

	@Autowired
	RatesCache cache;

	/**
	 * This method converts currency. It it using
	 * "https://github.com/posadskiy/currency-converter" for conversion rate using
	 * multiple third party API.
	 * 
	 * @param inputItemList
	 * @param currency
	 * @return resultList
	 * @throws Exception
	 */
	public List<ItemDTO> convertCurrency(final List<ItemDTO> inputItemList, final String currency) throws Exception {
		List<ItemDTO> resultList = new ArrayList<>();

		/* Return exception to caller if requested converting currency is not valid */
		if (!isCurrencyValid(currency.toUpperCase())) {
			throw new Exception("Invalid Target Currency: " + currency);
		}

		for (ItemDTO itemDetailsDTO : inputItemList) {

			/* Checks invalid items in the incoming list */
			if (StringUtils.isAnyBlank(itemDetailsDTO.getItem(), itemDetailsDTO.getCurrency())
					|| null == itemDetailsDTO.getPrice()
					|| !isCurrencyValid(itemDetailsDTO.getCurrency().toUpperCase())) {
				System.out.println("Invalid Item: " + itemDetailsDTO.toString());
				continue;
			}

			String fromCurrencyUpperCase = itemDetailsDTO.getCurrency().toUpperCase();
			String toCurrencyUpperCase = currency.toUpperCase();

			/*
			 * Calling the currency conversion library for latest conversion rates, if the
			 * external service is down try to get the last values for the combination of
			 * desired currencies from cache
			 */
			String rateKey = fromCurrencyUpperCase + toCurrencyUpperCase;
			ItemDTO itemToAdd = null;

			try {
				Double convertRate = currencyConverterAPIConfig.getCurrencyConverter().rate(fromCurrencyUpperCase,
						toCurrencyUpperCase);
				cache.cacheRate(rateKey, BigDecimal.valueOf(convertRate));
				itemToAdd = setResultItem(itemDetailsDTO, toCurrencyUpperCase, BigDecimal.valueOf(convertRate));
			} catch (CurrencyConverterException e) {
				BigDecimal cachedRate = cache.getCachedRate(rateKey);
				if (null != cachedRate) {
					itemToAdd = setResultItem(itemDetailsDTO, toCurrencyUpperCase, cachedRate);
					System.out.println("Returing data for " + rateKey + " from Cache");
				} else {
					System.out.println("Sorry for the inconvenience, Please try again later");
				}
			}

			resultList.add(itemToAdd);
		}
		return resultList;
	}

	private ItemDTO setResultItem(ItemDTO itemDetailsDTO, String toCurrencyUpperCase, BigDecimal convertRate) {
		ItemDTO resultItem = new ItemDTO();
		resultItem.setItem(itemDetailsDTO.getItem());
		resultItem.setCurrency(toCurrencyUpperCase);
		resultItem.setPrice(convertRate.multiply(itemDetailsDTO.getPrice()).setScale(2, BigDecimal.ROUND_HALF_EVEN));

		return resultItem;
	}

	/**
	 * Check if the given currency is valid using third party currency enum
	 */
	private boolean isCurrencyValid(String currency) {
		if (EnumUtils.isValidEnum(Currency.class, currency)) {
			return true;
		}
		return false;
	}

}
