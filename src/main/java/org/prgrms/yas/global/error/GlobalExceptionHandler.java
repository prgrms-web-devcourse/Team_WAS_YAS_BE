package org.prgrms.yas.global.error;

import lombok.extern.slf4j.Slf4j;
import org.prgrms.yas.global.response.ErrorResponse;
import org.prgrms.yas.domain.user.exception.DuplicateUserException;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.exception.NotSamePasswordException;
import org.prgrms.yas.global.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
		log.error(
				"Handle all Exception {}",
				e.toString()
		);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(
				errorResponse,
				HttpStatus.INTERNAL_SERVER_ERROR
		);
	}

	
	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException e
	) {
		log.error(
				"Handle MethodArgumentNotValidException {}",
				e.toString()
		);
		
		ErrorResponse errorResponse = ErrorResponse.of(
				ErrorCode.INVALID_INPUT_ERROR,
				e.getBindingResult()
		);
		return new ResponseEntity<>(
				errorResponse,
				HttpStatus.BAD_REQUEST
		);
	}
	
	@ExceptionHandler({DuplicateUserException.class})
	public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateUserException e) {
		log.error(
				"Handle Duplicate User Exception {}",
				e.toString()
		);
		
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.CONFLICT_VALUE_ERROR);
		return new ResponseEntity<>(
				errorResponse,
				HttpStatus.CONFLICT
		);
	}
	
	@ExceptionHandler({NotSamePasswordException.class})
	public ResponseEntity<ErrorResponse> handleNotSamePasswordException(NotSamePasswordException e) {
		log.error(
				"Handle Check Password Exception {}",
				e.toString()
		);
		
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.CONFLICT_VALUE_ERROR);
		return new ResponseEntity<>(
				errorResponse,
				HttpStatus.CONFLICT
		);
	}
	
	@ExceptionHandler({NotFoundUserException.class})
	public ResponseEntity<ErrorResponse> handleNotFountException(NotFoundException e) {
		log.error(
				"Handle Not Found Exception {}",
				e.getMessage()
		);
		
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NOT_FOUND_RESOURCE_ERROR);
		return new ResponseEntity<>(
				errorResponse,
				HttpStatus.NOT_FOUND
		);
	}
}
