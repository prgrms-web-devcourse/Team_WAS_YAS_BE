package org.prgrms.yas.oauth2;

import static org.prgrms.yas.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.service.UserService;
import org.prgrms.yas.jwt.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final UserService userService;
	
	private final Jwt jwt;
	
	private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;
	
	public OAuth2AuthenticationSuccessHandler(
			UserService userService,
			Jwt jwt,
			HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository
	) {
		this.userService = userService;
		this.jwt = jwt;
		this.authorizationRequestRepository = authorizationRequestRepository;
	}
	
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest req, HttpServletResponse res, Authentication authentication
	) throws ServletException, IOException {
		if(authentication instanceof OAuth2AuthenticationToken){
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
			OAuth2User oAuth2User = oauth2Token.getPrincipal();
			String provider = oauth2Token.getAuthorizedClientRegistrationId();
			User user = processUserOAuth2UserJoin(oAuth2User,provider);
			String token = generateToken(user);
			String redirectUri = determineTargetUrl(req, res, authentication);
			String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
			                                       .queryParam("token", token)
			                                       .queryParam("userId", user.getId())
			                                       .build().toUriString();
			logger.info("redirectUri : {}" ,redirectUri);
			clearAuthenticationAttributes(req,res);
			getRedirectStrategy().sendRedirect(req,res,targetUrl);
		}else{
			super.onAuthenticationSuccess(req,res,authentication);
		}
	}
	
	private User processUserOAuth2UserJoin(OAuth2User oAuth2User, String provider){
		return userService.signUp(oAuth2User,provider);
	}
	
	private String generateLoginSuccessJson(User user){
		String token = generateToken(user);
		return "{\"token\":\"" + token + "\", \"email\":\"" + user.getEmail() + "\"}";
	}
	
	private String generateToken(User user){
		return jwt.sign(Jwt.Claims.from(user.getId(),user.getEmail(),new String[]{"ROLE_USER"}));
	}
	
	protected void clearAuthenticationAttributes(HttpServletRequest req, HttpServletResponse res){
		super.clearAuthenticationAttributes(req);
		authorizationRequestRepository.removeAuthorizationRequestCookies(req, res);
	}
	
	protected String determineTargetUrl(HttpServletRequest req, HttpServletResponse res, Authentication authentication){
		Optional<String> redirectUri = CookieUtil.getCookie(req,REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
		
		return redirectUri.orElse(getDefaultTargetUrl());
	}
}
