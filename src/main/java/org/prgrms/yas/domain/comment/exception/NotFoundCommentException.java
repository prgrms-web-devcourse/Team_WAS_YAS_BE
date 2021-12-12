package org.prgrms.yas.domain.comment.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.error.exception.BusinessException;

public class NotFoundCommentException extends BusinessException {
  public NotFoundCommentException(ErrorCode errorCode) {
    super(errorCode);
  }

}
