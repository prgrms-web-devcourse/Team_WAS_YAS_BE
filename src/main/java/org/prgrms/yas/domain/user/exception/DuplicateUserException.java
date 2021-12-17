package org.prgrms.yas.domain.user.exception;

import org.prgrms.yas.domain.post.global.error.ErrorCode;
import org.prgrms.yas.domain.post.global.exception.BusinessException;

public class DuplicateUserException extends BusinessException {
	
	public DuplicateUserException(ErrorCode errorCode) {
		super(errorCode);
	}
}
