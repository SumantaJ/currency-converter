package com.currency.converter.app.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.currency.converter.app.dto.ApiMessageDTO;

@ControllerAdvice
public class GlobalControllerAdvice {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ApiMessageDTO exceptionHandlerInternalServerException(final Exception e) {
		return getApiErrorMessage(e.getMessage());
	}

	private ApiMessageDTO getApiErrorMessage(String message) {
		return new ApiMessageDTO(message);
	}
}
