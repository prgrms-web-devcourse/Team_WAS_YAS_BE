package org.prgrms.yas.domain.user.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.NotFoundException;

public class NotFoundUserException extends NotFoundException {
	
	public NotFoundUserException(ErrorCode errorCode) {
		super(errorCode);
	}
}
