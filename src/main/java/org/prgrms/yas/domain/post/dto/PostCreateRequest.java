package org.prgrms.yas.domain.post.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreateRequest {
	private String content;
	
	public PostCreateRequest(String content) {
		this.content = content;
	}
}
