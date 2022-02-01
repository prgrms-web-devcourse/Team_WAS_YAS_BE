package org.prgrms.yas.domain.user.domain;

import lombok.Getter;

@Getter
public enum Role {
	ROLE_USER("ROLE_USER","일반 사용자"),
	ROLE_ADMIN("ROLEL_ADMIN","관리자");
	
	private final String code;
	
	private final String codeName;
	
	Role(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}
}
