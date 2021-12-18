package org.prgrms.yas.domain.post.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.BusinessException;

public class DuplicatePostException extends BusinessException {
	public DuplicatePostException(ErrorCode errorCode) {
		super(errorCode);
	}
}
