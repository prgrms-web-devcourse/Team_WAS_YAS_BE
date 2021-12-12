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
public class CommentCreateRequest {
  private String content;

  @Builder
  public CommentCreateRequest(String content) {
    this.content = content;
  }

  public Comment toEntity(User user, RoutinePost routinePost){
      return Comment.builder()
                    .content(this.content)
                    .user(user)
                    .routinePost(routinePost)
                    .build();
  }
}
