package org.prgrms.yas.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

  private Long id;
  private String name;
  private String email;
  private String nickname;
  private String profileImage;

  @Builder
  public UserResponse(Long id, String name, String email, String nickname, String profileImage) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.nickname = nickname;
    this.profileImage = profileImage;
  }
}
