package org.prgrms.yas.domain.post.global.exception;

import org.prgrms.yas.domain.post.global.error.ErrorCode;

public class NotFoundException extends BusinessException {
	
	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}

// 1. dto 네이밍
