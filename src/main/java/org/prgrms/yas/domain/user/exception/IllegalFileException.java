package org.prgrms.yas.domain.user.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.BusinessException;

public class IllegalFileException extends BusinessException {
	
	public IllegalFileException(ErrorCode errorCode) {
		super(errorCode);
	}
}
