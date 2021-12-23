package org.prgrms.yas.setting.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.prgrms.yas.domain.user.domain.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationSecurityContextFactory.class)
public @interface WithMockJwtAuthentication {
	long id() default 1L;
	String token() default "testToken";
	String username() default "testName";
	String role() default "USER";
}
