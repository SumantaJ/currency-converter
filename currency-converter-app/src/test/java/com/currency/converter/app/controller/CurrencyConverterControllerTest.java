package com.currency.converter.app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyConverterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private static final String CURRENCY = "EUR";

	private static final String TEST_VALID_PAYLOAD = "[{\"item\":\"B\",\"price\":9.95,\"currency\":\"USD\"},"
			+ "{\"item\":\"A\",\"price\":99.95,\"currency\":\"JPY\"},{\"item\":\"C\",\"price\":10.99,\"currency\":\"EUR\"}]";

	private static final String TEST_INVALID_PAYLOAD = "[{\"item\":\"B\",\"price\":9.95,\"currency\":\"USD\"},"
			+ "{\"item\":\"A\",\"price\":99.95,\"currency\":\"JPY\"},{\"item\":\"C\",\"price\":10.99,\"currency\":\"INVALID\"}]";

	private static final String MESSAGE = "Input JSON and target Currency are required";
	private static final String URL = "/currencyconverter/convert?currency=";

	@Test
	public void testConvertCurrencyWithAllCorrectPayload() throws Exception {
		mockMvc.perform(post(URL + CURRENCY).content(TEST_VALID_PAYLOAD).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void testConvertCurrencyWithPartialCorrectPayload() throws Exception {
		mockMvc.perform(post(URL + CURRENCY).content(TEST_INVALID_PAYLOAD).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void testConvertCurrencyWhenTargetCurrencyNotValid() throws Exception {
		mockMvc.perform(post(URL + "").content(TEST_VALID_PAYLOAD).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.message", is(MESSAGE)));
	}

}
