package org.prgrms.yas.domain.mission.exception;

import org.prgrms.yas.domain.post.global.error.ErrorCode;
import org.prgrms.yas.domain.post.global.error.exception.BusinessException;

public class NotFoundMissionStatusException extends BusinessException {
	
	public NotFoundMissionStatusException(ErrorCode errorCode) {
		super(errorCode);
	}
}
