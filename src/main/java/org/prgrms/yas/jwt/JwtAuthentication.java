package org.prgrms.yas.jwt;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import lombok.Getter;

@Getter
public class JwtAuthentication {
	
	private final String token;
	private final Long id;
	private final String username;
	
	public JwtAuthentication(Long id, String token, String username) {
		checkArgument(
				isNotEmpty(username),
				"username must be provided"
		);
		this.id = id;
		this.token = token;
		this.username = username;
	}
}
