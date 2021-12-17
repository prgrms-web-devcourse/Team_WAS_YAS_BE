package org.prgrms.yas.domain.post.exception;

import org.prgrms.yas.domain.post.global.error.ErrorCode;
import org.prgrms.yas.domain.post.global.exception.NotFoundException;

public class NotFoundRoutinePostException extends NotFoundException {
	
	public NotFoundRoutinePostException(ErrorCode errorCode) {
		super(errorCode);
	}
	
}
