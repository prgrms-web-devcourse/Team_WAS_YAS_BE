package org.prgrms.yas.domain.mission.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.error.exception.BusinessException;

public class NotFoundMissionException extends BusinessException {
	
	public NotFoundMissionException(ErrorCode errorCode) {
		super(errorCode);
	}
}
