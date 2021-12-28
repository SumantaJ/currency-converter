package com.currency.converter.app.service;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.currency.converter.app.dto.ItemDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyConverterServiceTest {

	@Autowired
	private CurrencyConverterService currencyConverterService;

	private static final String TEST_ITEM_A = "A";
	private static final String TEST_ITEM_B = "B";
	private static final String TEST_CURRENCY_EUR = "EUR";
	private static final String TEST_CURRENCY_USD = "USD";
	private static final BigDecimal TEST_CURRENCY_PRICE = new BigDecimal("10.65");
	private static final String TEST_CURRENCY_INVALID = "INVALID";

	@Test
	public void testCurrencyConverterReturnsEmptyList() throws Exception {
		List<ItemDTO> itemList = new ArrayList<>();

		List<ItemDTO> resultList = currencyConverterService.convertCurrency(itemList, TEST_CURRENCY_EUR);

		assertTrue(resultList.size() == 0);
	}

	@Test
	public void testCurrencyConverter() throws Exception {
		List<ItemDTO> itemList = new ArrayList<>();
		itemList.add(getItem(TEST_ITEM_A, TEST_CURRENCY_EUR, TEST_CURRENCY_PRICE));

		List<ItemDTO> resultList = currencyConverterService.convertCurrency(itemList, TEST_CURRENCY_USD);

		assertTrue(resultList.size() == 1);
		assertTrue(resultList.get(0).getCurrency() == TEST_CURRENCY_USD);
		assertTrue(resultList.get(0).getItem() == TEST_ITEM_A);
	}

	@Test(expected = Exception.class)
	public void testCurrencyConverterWhenTargetCurrencyInvalid() throws Exception {
		List<ItemDTO> itemList = new ArrayList<>();
		itemList.add(getItem(TEST_ITEM_A, TEST_CURRENCY_EUR, TEST_CURRENCY_PRICE));

		currencyConverterService.convertCurrency(itemList, TEST_CURRENCY_INVALID);
	}

	@Test
	public void testCurrencyConverterWhenPartialPayloadMalformed() throws Exception {
		List<ItemDTO> itemList = new ArrayList<>();
		itemList.add(getItem(TEST_ITEM_B, TEST_CURRENCY_EUR, TEST_CURRENCY_PRICE));
		itemList.add(getItem(TEST_ITEM_A, TEST_CURRENCY_INVALID, TEST_CURRENCY_PRICE));

		List<ItemDTO> resultList = currencyConverterService.convertCurrency(itemList, TEST_CURRENCY_USD);

		assertTrue(resultList.size() == 1);
		assertTrue(resultList.get(0).getCurrency() == TEST_CURRENCY_USD);
		assertTrue(resultList.get(0).getItem() == TEST_ITEM_B);
	}

	private ItemDTO getItem(String item, String currency, BigDecimal price) {
		ItemDTO items = new ItemDTO();
		items.setItem(item);
		items.setCurrency(currency);
		items.setPrice(price);

		return items;
	}

}
