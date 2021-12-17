package org.prgrms.yas.domain.user.exception;

import org.prgrms.yas.domain.post.global.error.ErrorCode;
import org.prgrms.yas.domain.post.global.exception.BusinessException;

public class NotSamePasswordException extends BusinessException {
	
	public NotSamePasswordException(ErrorCode errorCode) {
		super(errorCode);
	}
}
