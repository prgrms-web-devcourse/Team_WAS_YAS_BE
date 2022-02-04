package org.prgrms.yas.jwt;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import lombok.Getter;

@Getter
public class JwtAuthentication {
	
	private final String token;
	private final Long id;
	
	public JwtAuthentication(Long id, String token) {
		checkArgument(
				isNotEmpty(token),
				"token must be provided"
		);
		this.id = id;
		this.token = token;
	}
}
