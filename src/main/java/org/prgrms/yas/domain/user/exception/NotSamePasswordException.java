package org.prgrms.yas.domain.user.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.BusinessException;

public class NotSamePasswordException extends BusinessException {
	
	public NotSamePasswordException(ErrorCode errorCode) {
		super(errorCode);
	}
}
