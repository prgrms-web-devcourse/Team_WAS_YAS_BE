package org.prgrms.yas.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateRequest {
	
	@Size(max = 12)
	@NotBlank(message = "닉네임을 입력해 주세요.")
	private String nickname;
	
	private String profileImage;
	
	public UserUpdateRequest(String nickname, String profileImage) {
		this.nickname = nickname;
		this.profileImage = profileImage;
	}
	
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
}
