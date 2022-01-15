package org.prgrms.yas.oauth2;

import java.util.Base64;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

public class CookieUtil {
	public static Optional<Cookie> getCookie(HttpServletRequest req, String name){
		Cookie[] cookies = req.getCookies();
		
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				if(name.equals(cookie.getName())){
					return Optional.of(cookie);
				}
			}
		}
		return Optional.empty();
	}
	
	public static void addCookie(HttpServletResponse res, String name, String value, int maxAge){
		Cookie cookie = new Cookie(name,value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(maxAge);
		
		res.addCookie(cookie);
	}
	
	public static void deleteCookie(HttpServletRequest req, HttpServletResponse res, String name){
		Cookie[] cookies = req.getCookies();
		
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				if(name.equals(cookie.getName())){
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					res.addCookie(cookie);
				}
			}
		}
	}
	
	public static String serialize(Object obj){
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
	}
	
	public static <T> T deserialize(Cookie cookie, Class<T> cls){
		return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
	}
}
