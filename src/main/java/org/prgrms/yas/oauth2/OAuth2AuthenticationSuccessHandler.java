package org.prgrms.yas.oauth2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.service.OAuth2UserService;
import org.prgrms.yas.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private final Jwt jwt;
	private final OAuth2UserService oAuth2UserService;
	
	public OAuth2AuthenticationSuccessHandler(Jwt jwt, OAuth2UserService oAuth2UserService) {
		this.jwt = jwt;
		this.oAuth2UserService = oAuth2UserService;
	}
	
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws ServletException, IOException {
		if (authentication instanceof OAuth2AuthenticationToken) {
			
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
			OAuth2User principal = oauth2Token.getPrincipal();
			String registrationId = oauth2Token.getAuthorizedClientRegistrationId();
			
			User user = processUserOAuth2UserJoin(
					principal,
					registrationId
			);
			String loginSuccessJson = generateLoginSuccessJson(user);
			response.setContentType("application/json;charset=UTF-8");
			response.setContentLength(loginSuccessJson.getBytes(StandardCharsets.UTF_8).length);
			response.getWriter()
			        .write(loginSuccessJson);
		} else {
			super.onAuthenticationSuccess(
					request,
					response,
					authentication
			);
		}
	}
	
	private User processUserOAuth2UserJoin(OAuth2User oAuth2User, String provider) {
		return oAuth2UserService.signUp(
				oAuth2User,
				provider
		);
	}
	
	private String generateLoginSuccessJson(User user) {
		String token = generateToken(user);
		return "{\"token\":\"" + token + "\", \"Email\":\"" + user.getEmail() + "\", \"Id\":\""
				+ user.getId() + "\", \"role\":\"" + user.getRoles()
				                                         .toString() + "\"}";
		
	}
	
	private String generateToken(User user) {
		return jwt.sign(Jwt.Claims.from(
				user.getId(),
				user.getEmail(),
				new String[]{"ROLE_USER"}
		));
	}
}
