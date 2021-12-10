package org.prgrms.yas.domain.user.controller;

import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserSignInRequest;
import org.prgrms.yas.domain.user.dto.UserToken;
import org.prgrms.yas.jwt.Jwt;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.prgrms.yas.jwt.JwtAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final AuthenticationManager authenticationManager;

  private final Jwt jwt;

  public UserController(
      AuthenticationManager authenticationManager, Jwt jwt
  ) {
    this.authenticationManager = authenticationManager;
    this.jwt = jwt;
  }

  /**
   * 정상적인 커스텀 로그인을 하게 될 경우, 토큰 발행
   *
   * @return JWT 토큰
   * @Param UserSignInRequest Dto
   */

  @PostMapping("/users/login")
  public UserToken signIn(@RequestBody UserSignInRequest userSignInRequest) {

    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
        userSignInRequest.getEmail(),
        userSignInRequest.getPassword()
    );
    Authentication resultToken = authenticationManager.authenticate(authenticationToken);
    JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) resultToken;
    JwtAuthentication principal = (JwtAuthentication) jwtAuthenticationToken.getPrincipal();
    User user = (User) jwtAuthenticationToken.getDetails();
   return new UserToken(
        user.getId(),
        principal.getToken(),
        principal.getUsername(),
        user.getRoles()
            .toString()
    );
  }
}
