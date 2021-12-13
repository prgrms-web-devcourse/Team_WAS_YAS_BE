package org.prgrms.yas.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponse {
	
	private String name;
	private String email;
	private String nickname;
	private String profileImage;
	
	@Builder
	public UserResponse(String name, String email, String nickname, String profileImage) {
		this.name = name;
		this.email = email;
		this.nickname = nickname;
		this.profileImage = profileImage;
	}
}
