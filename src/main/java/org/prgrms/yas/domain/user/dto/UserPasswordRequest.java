package org.prgrms.yas.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserPasswordRequest {
	
	@NotBlank(message = "현재 비밀번호를 입력해 주세요.")
	@Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 숫자,영문,특수문자를 조합해야 합니다.")
	private String password;
}
