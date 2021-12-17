package org.prgrms.yas.domain.comment.exception;

import org.prgrms.yas.domain.post.global.error.ErrorCode;
import org.prgrms.yas.domain.post.global.exception.NotFoundException;

public class NotFoundCommentException extends NotFoundException {

  public NotFoundCommentException(ErrorCode errorCode) {
    super(errorCode);
  }

}
