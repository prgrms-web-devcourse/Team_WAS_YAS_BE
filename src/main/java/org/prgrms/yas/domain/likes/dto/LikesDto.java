package org.prgrms.yas.domain.likes.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.likes.domain.CommentLikes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikesDto {
	private Long userId;
	private String username;
	
	@Builder
	public LikesDto(CommentLikes commentLikes) {
		this.userId = commentLikes.getUser().getId();
		this.username = commentLikes.getUser().getNickname();
	}
}
