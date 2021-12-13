package org.prgrms.yas.global.exception;

import org.prgrms.yas.global.error.ErrorCode;

public class NotFoundException extends BusinessException {
	
	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
