package org.prgrms.yas.configures;

import javax.servlet.http.HttpServletResponse;
import org.prgrms.yas.domain.user.service.OAuth2UserService;
import org.prgrms.yas.domain.user.service.UserService;
import org.prgrms.yas.jwt.Jwt;
import org.prgrms.yas.jwt.JwtAuthenticationFilter;
import org.prgrms.yas.jwt.JwtAuthenticationProvider;
import org.prgrms.yas.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final JwtConfig jwtConfig;
	private final OAuth2UserService oAuth2UserService;
	
	
	public WebSecurityConfig(JwtConfig jwtConfig, OAuth2UserService oAuth2UserService) {
		this.jwtConfig = jwtConfig;
		this.oAuth2UserService = oAuth2UserService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public Jwt jwt() {
		return new Jwt(
				jwtConfig.getIssuer(),
				jwtConfig.getClientSecret(),
				jwtConfig.getExpirySeconds()
		);
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, e) -> {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter()
			        .write("ACCESS DENIED");
			response.getWriter()
			        .flush();
			response.getWriter()
			        .close();
		};
	}
	
	@Bean
	public JwtAuthenticationProvider jwtAuthenticationProvider(UserService userService, Jwt jwt) {
		return new JwtAuthenticationProvider(
				jwt,
				userService
		);
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new JwtAuthenticationFilter(
				jwtConfig.getHeader(),
				jwt
		);
	}
	
	@Bean
	public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new OAuth2AuthenticationSuccessHandler(
				jwt,
				oAuth2UserService
		);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		    .antMatchers(HttpMethod.POST,"/users")
		    .permitAll()
				.antMatchers("/users")
				.hasAnyRole(
						"USER",
						"ADMIN"
				)
				.anyRequest()
				.permitAll()
		    .and()
		    .formLogin()
		    .disable()
		    .csrf()
		    .disable()
		    .headers()
		    .disable()
		    .httpBasic()
		    .disable()
		    .rememberMe()
		    .disable()
		    .logout()
		    .disable()
		    .sessionManagement()
		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		    .and()
		    .oauth2Login()
		    .successHandler(oAuth2AuthenticationSuccessHandler())
		    .and()
		    .exceptionHandling()
		    .accessDeniedHandler(accessDeniedHandler())
		    .and()
		    .addFilterAfter(
				    jwtAuthenticationFilter(),
				    SecurityContextPersistenceFilter.class
		    );
		
	}
}

