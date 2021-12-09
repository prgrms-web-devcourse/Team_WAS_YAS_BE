package org.prgrms.yas.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserSignInRequest {

  @NotBlank(message = "이메일을 입력해 주세요.")
  @Email(message = "올바른 이메일 주소를 입력해 주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해 주세요.")
  @Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
  private String password;

  protected UserSignInRequest() {
  }

  public UserSignInRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
