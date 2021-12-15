package org.prgrms.yas.domain.post.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.BusinessException;
import org.prgrms.yas.global.exception.NotFoundException;

public class NotFoundRoutinePostException extends NotFoundException {
	
	public NotFoundRoutinePostException(ErrorCode errorCode) {
		super(errorCode);
	}
	
}
