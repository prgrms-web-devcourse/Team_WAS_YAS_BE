package org.prgrms.yas.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import org.prgrms.yas.domain.user.exception.NotSamePasswordException;
import org.prgrms.yas.global.error.ErrorCode;

@Getter
public class UserPasswordRequest {
	@NotBlank(message = "현재 비밀번호를 입력해 주세요.")
	@Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 숫자,영문,특수문자를 조합해야 합니다.")
	private String nowPassword;
	
	@NotBlank(message = "변경할 비밀번호를 입력해 주세요.")
	@Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "변경할 비밀번호는 숫자,영문,특수문자를 조합해야 합니다.")
	private String newPassword;
	
	@NotBlank(message = "변경할 비밀번호를 한번 더 입력해 주세요.")
	@Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "변경할 비밀번호와 일치하지 않습니다.")
	private String newPasswordCheck;
	
	public boolean isDifferentPassword() {
		if (!this.newPassword.equals(this.newPasswordCheck)) {
			throw new NotSamePasswordException(ErrorCode.CONFLICT_VALUE_ERROR);
		}
		return false;
	}
}
