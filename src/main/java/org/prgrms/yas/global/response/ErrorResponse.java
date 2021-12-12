package org.prgrms.yas.global.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
	
	private final LocalDateTime timestamp = LocalDateTime.now();
	private String message;
	private int status;
	private List<FieldError> fieldErrors;
	
	private ErrorResponse(ErrorCode errorCode) {
		this.message = errorCode.getMessage();
		this.status = errorCode.getStatus();
		this.fieldErrors = new ArrayList<>();
	}
	
	private ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
		this.message = errorCode.getMessage();
		this.status = errorCode.getStatus();
		this.fieldErrors = errors;
	}
	
	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode);
	}
	
	public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
		return new ErrorResponse(
				errorCode,
				FieldError.of(bindingResult)
		);
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FieldError {
		
		private String field;
		private String value;
		private String reason;
		
		
		private FieldError(final String field, final String value, final String reason) {
			
			this.field = field;
			this.value = value;
			this.reason = reason;
		}
		
		private static List<FieldError> of(final BindingResult bindingResult) {
			List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
			
			return fieldErrors.stream()
			                  .map(filedError -> new FieldError(
					
					                  "[" + filedError.getField() + "] 필드에",
					                  filedError.getRejectedValue() == null
							                  ? ""
							                  : "입력된 값은 [" + filedError.getRejectedValue()
							                                           .toString() + "]",
					                  filedError.getDefaultMessage()
			                  ))
			                  .collect(Collectors.toList());
		}
	}
}
