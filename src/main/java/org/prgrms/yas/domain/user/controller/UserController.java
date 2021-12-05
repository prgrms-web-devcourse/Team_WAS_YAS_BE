package org.prgrms.yas.domain.user.controller;

import javax.validation.Valid;
import org.prgrms.yas.domain.user.dto.UserJoinRequest;
import org.prgrms.yas.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  public ResponseEntity<Long> joinUser(@Valid @RequestBody UserJoinRequest userJoinRequest) {
    return ResponseEntity.ok(userService.join(userJoinRequest));
  }
}
