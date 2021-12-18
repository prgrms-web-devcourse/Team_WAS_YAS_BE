package org.prgrms.yas.domain.mission.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.error.exception.BusinessException;

public class NotFoundMissionStatusException extends BusinessException {
	
	public NotFoundMissionStatusException(ErrorCode errorCode) {
		super(errorCode);
	}
}
