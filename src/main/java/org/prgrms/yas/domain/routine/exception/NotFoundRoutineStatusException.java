package org.prgrms.yas.domain.routine.exception;

import org.prgrms.yas.domain.post.global.error.ErrorCode;
import org.prgrms.yas.domain.post.global.error.exception.BusinessException;

public class NotFoundRoutineStatusException extends BusinessException {
	
	public NotFoundRoutineStatusException(ErrorCode errorCode) {
		super(errorCode);
	}
}
