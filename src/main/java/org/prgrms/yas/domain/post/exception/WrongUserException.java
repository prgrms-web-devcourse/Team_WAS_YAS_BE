package org.prgrms.yas.domain.post.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.BusinessException;

public class WrongUserException extends BusinessException{
	public WrongUserException(ErrorCode errorCode) {
		super(errorCode);
	}
}

