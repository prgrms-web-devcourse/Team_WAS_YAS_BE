package org.prgrms.yas.setting.security;


import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import org.prgrms.yas.jwt.JwtAuthentication;
import org.prgrms.yas.jwt.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockJwtAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtAuthentication> {
	
	@Override
	public SecurityContext createSecurityContext(
			WithMockJwtAuthentication annotation
	) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		JwtAuthenticationToken authentication = new JwtAuthenticationToken(
				new JwtAuthentication(annotation.id(),
						annotation.token(),
						annotation.username()
				),
				null,
				createAuthorityList(annotation.role())
		);
		context.setAuthentication(authentication);
		return context;
	}
}
