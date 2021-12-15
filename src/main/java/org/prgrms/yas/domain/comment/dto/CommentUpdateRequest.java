package org.prgrms.yas.domain.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.user.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUpdateRequest {

  private String content;

  @Builder
  public CommentUpdateRequest(String content) {
    this.content = content;
  }
}
