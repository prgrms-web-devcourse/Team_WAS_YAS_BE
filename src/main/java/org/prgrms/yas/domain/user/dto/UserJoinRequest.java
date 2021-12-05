package org.prgrms.yas.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import org.prgrms.yas.domain.user.domain.User;

@Getter
public class UserJoinRequest {

  @NotBlank(message = "이메일을 입력해 주세요.")
  @Email(message = "올바른 이메일 주소를 입력해 주세요.")
  private String email;

  @NotBlank(message = "닉네임을 입력해 주세요.")
  @Size(min = 1, max = 12,message = "닉네임은 최소 1글자, 최대 12글자 입니다.")
  private String nickname;

  @NotBlank(message = "비밀번호를 입력해 주세요.")
  @Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
  private String password;

  private String userImage;

  public User toEntity() {
    return User.builder().nickname(nickname).email(email).password(password).userImage(userImage)
               .build();
  }
}
