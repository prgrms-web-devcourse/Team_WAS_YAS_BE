package org.prgrms.yas.global.error.exception;

import lombok.Getter;
import org.prgrms.yas.global.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
