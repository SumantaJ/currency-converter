package com.currency.converter.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.currency.converter.app.dto.ItemDTO;
import com.currency.converter.app.service.CurrencyConverterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * A controller that takes a JSON array and a target currency in String as
 * input. Returns a JSON array with converted price.
 *
 */
@RestController
@RequestMapping("/currencyconverter")
@Api(value = "Currency Converter Controller")
public class CurrencyConverterController {

	@Autowired
	private CurrencyConverterService currencyConvertorServiceImpl;

	/**
	 * @param inputItemList
	 * @param currency
	 * @return convertedList
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "Api to convert currency", response = ItemDTO.class, responseContainer = "List")
	@PostMapping(value = "/convert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity convert(@RequestBody final List<ItemDTO> inputItemList,
			@RequestParam("currency") final String currency) throws Exception {

		if (StringUtils.isEmpty(inputItemList) || StringUtils.isEmpty(currency)) {
			throw new Exception("Input JSON and target Currency are required");
		}
		return ResponseEntity.ok(currencyConvertorServiceImpl.convertCurrency(inputItemList, currency.trim()));
	}
}
