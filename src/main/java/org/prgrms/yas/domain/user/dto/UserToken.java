package org.prgrms.yas.domain.user.dto;

import lombok.Getter;

@Getter
public class UserToken {
	
	private final Long id;
	private final String token;
	private final String roles;
	
	public UserToken(Long id, String token, String roles) {
		this.id = id;
		this.token = token;
		this.roles = roles;
	}
}
