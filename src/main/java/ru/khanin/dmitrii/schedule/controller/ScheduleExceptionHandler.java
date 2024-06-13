package ru.khanin.dmitrii.schedule.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.khanin.dmitrii.schedule.dto.ErrorResponse;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;

@RestControllerAdvice
public class ScheduleExceptionHandler {
	
	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<ErrorResponse> missingPathVariableException(MissingPathVariableException ex) {
		return ResponseEntity.status(400).body(new ErrorResponse(
				ex.getClass().getName(),
				"Path variable expected is not present"
		));
	}
	
	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse> missingRequestHeaderException(MissingRequestHeaderException ex) {
		return ResponseEntity.status(400).body(new ErrorResponse(
				ex.getClass().getName(),
				"Request header expected is not present."
		));
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return ResponseEntity.status(400).body(new ErrorResponse(
				ex.getClass().getName(),
				"Wrong request body"
		));
	}
	
	@ExceptionHandler(BadSqlGrammarException.class)
	public ResponseEntity<ErrorResponse> badSqlGrammarException(BadSqlGrammarException ex) {
		return ResponseEntity.status(400).body(new ErrorResponse(
				ex.getClass().getName(),
				"Bad sql grammar"
		));
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException ex) {
		return ResponseEntity.status(400).body(new ErrorResponse(
				ex.getClass().getName(),
				"The entered data violates the integrity"
		));
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> nullPointerException(NullPointerException ex) {
		return ResponseEntity.status(401).body(new ErrorResponse(
				ex.getClass().getName(),
				"Something went wrong"
		));
	}
	
	@ExceptionHandler(NoAccessException.class)
	public ResponseEntity<ErrorResponse> noAccessException(NoAccessException ex) {
		return ResponseEntity.status(401).body(new ErrorResponse(
				ex.getClass().getName(),
				"You haven't access to do this"
		));
	}
}
