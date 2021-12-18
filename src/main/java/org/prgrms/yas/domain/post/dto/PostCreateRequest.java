package org.prgrms.yas.domain.post.dto;

import lombok.Getter;

@Getter
public class PostCreateRequest {
	private final String content;
	
	public PostCreateRequest(String content) {
		this.content = content;
	}
}
