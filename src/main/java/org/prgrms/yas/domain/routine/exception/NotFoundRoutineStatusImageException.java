package org.prgrms.yas.domain.routine.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.error.exception.BusinessException;

public class NotFoundRoutineStatusImageException extends BusinessException {
	
	public NotFoundRoutineStatusImageException(ErrorCode errorCode) {
		super(errorCode);
	}
}
