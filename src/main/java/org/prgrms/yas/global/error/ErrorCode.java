package org.prgrms.yas.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  NOT_FOUND_RESOURCE_ERROR("존재하지 않는 리소스입니다.",
                           HttpStatus.BAD_REQUEST.value()
  ), INTERNAL_SERVER_ERROR("서버 내부 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());

  private final String message;
  private final int status;

  ErrorCode(String message, int status) {
    this.message = message;
    this.status = status;
  }
}
