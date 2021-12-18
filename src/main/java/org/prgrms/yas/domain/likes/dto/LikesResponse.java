package org.prgrms.yas.domain.likes.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.likes.domain.PostLikes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikesResponse {
	private Long userId;
	
	@Builder
	public LikesResponse(PostLikes postLikes) {
		this.userId = postLikes.getUser().getId();
	}
}
