package org.prgrms.yas.domain.user.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.BusinessException;

public class DuplicateUserException extends BusinessException {
	
	public DuplicateUserException(ErrorCode errorCode) {
		super(errorCode);
	}
}
