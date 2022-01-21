package org.prgrms.yas.domain.user.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.BusinessException;

public class NotValidPasswordException extends BusinessException {
	
	public NotValidPasswordException(ErrorCode errorCode) {
		super(errorCode);
	}
}
