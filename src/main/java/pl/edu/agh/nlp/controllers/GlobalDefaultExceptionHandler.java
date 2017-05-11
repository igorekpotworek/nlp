package pl.edu.agh.nlp.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.agh.nlp.dto.ErrorMessage;
import pl.edu.agh.nlp.exceptions.AbsentModelException;
import pl.edu.agh.nlp.exceptions.NotFoundException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Log4j
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

	@ResponseStatus(NOT_FOUND)
	@ExceptionHandler(value = NotFoundException.class)
	@ResponseBody
	protected ErrorMessage handleNotFoundException(NotFoundException ex) {
		return handleException(ex, ex.getMessage());
	}

	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = AbsentModelException.class)
	@ResponseBody
	protected ErrorMessage handleAbsentModelException(AbsentModelException ex) {
		return handleException(ex);
	}

	private ErrorMessage handleException(Throwable ex, String message) {
		log.error(message);
		return new ErrorMessage(ex, message);
	}

	private ErrorMessage handleException(Throwable ex) {
		log.error(ex.getMessage(), ex);
		return new ErrorMessage(ex, ex.getMessage());
	}


}