package org.prgrms.yas.domain.post.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.user.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {
	private Long userId;
	private String nickname;
	private String profileImage;
	
	@Builder
	public UserDto(User user) {
		this.userId = user.getId();
		this.nickname = user.getNickname();
		this.profileImage = user.getProfileImage();
	}
}
