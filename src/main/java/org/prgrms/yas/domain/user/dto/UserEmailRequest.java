package org.prgrms.yas.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserEmailRequest {
	@NotBlank(message = "이메일을 입력해 주세요.")
	@Email(message = "올바른 이메일 주소를 입력해 주세요.")
	private String email;
}
