package org.prgrms.yas.domain.user.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.BusinessException;

public class CheckPasswordException extends BusinessException {
	
	public CheckPasswordException(ErrorCode errorCode) {
		super(errorCode);
	}
}
