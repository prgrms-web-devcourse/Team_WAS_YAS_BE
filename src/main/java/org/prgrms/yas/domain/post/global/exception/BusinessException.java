package org.prgrms.yas.domain.post.global.exception;

import lombok.Getter;
import org.prgrms.yas.domain.post.global.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
	
	private final ErrorCode errorCode;
	
	public BusinessException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
