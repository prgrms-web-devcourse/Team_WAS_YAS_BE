package org.prgrms.yas.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public final static String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
	public static final int cookieExpireSeconds = 180;
	
	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(
			HttpServletRequest req
	) {
		return CookieUtil.getCookie(req,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
		                 .map(cookie -> CookieUtil.deserialize(cookie,OAuth2AuthorizationRequest.class))
		                 .orElse(null);
	}
	
	@Override
	public void saveAuthorizationRequest(
			OAuth2AuthorizationRequest authorizationReq, HttpServletRequest req,
			HttpServletResponse res
	) {
		if(authorizationReq == null){
			CookieUtil.deleteCookie(req,res,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
			CookieUtil.deleteCookie(req,res,REDIRECT_URI_PARAM_COOKIE_NAME);
			return;
		}
		CookieUtil.addCookie(res,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,CookieUtil.serialize(authorizationReq),cookieExpireSeconds);
		String redirectUriAfterLogin = req.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
		logger.info("redirectUriAfterLogin : {}",redirectUriAfterLogin);
		if(StringUtils.isNotBlank(redirectUriAfterLogin)){
			CookieUtil.addCookie(res,REDIRECT_URI_PARAM_COOKIE_NAME,redirectUriAfterLogin,cookieExpireSeconds);
		}
	}
	
	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(
			HttpServletRequest req
	) {
		return this.loadAuthorizationRequest(req);
	}
	
	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(
			HttpServletRequest req, HttpServletResponse res
	) {
		return this.loadAuthorizationRequest(req);
	}
	
	public void removeAuthorizationRequestCookies(HttpServletRequest req, HttpServletResponse res){
		CookieUtil.deleteCookie(req,res,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
		CookieUtil.deleteCookie(req,res,REDIRECT_URI_PARAM_COOKIE_NAME);
	}
}
