package org.prgrms.yas.domain.comment.exception;

import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.global.exception.BusinessException;
import org.prgrms.yas.global.exception.NotFoundException;

public class NotFoundCommentException extends NotFoundException {

  public NotFoundCommentException(ErrorCode errorCode) {
    super(errorCode);
  }

}
