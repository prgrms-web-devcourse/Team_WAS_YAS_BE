package org.prgrms.yas.domain.comment.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.prgrms.yas.domain.comment.domain.Comment;

@Getter
public class CommentDetailResponse {
  private Long id;
  private String nickname;
  private LocalDateTime createdAt;
  private String content;

  @Builder
  public CommentDetailResponse(Comment comment) {
    this.id = comment.getId();
    this.nickname = comment.getUser().getNickname();
    this.createdAt = comment.getCreatedAt();
    this.content = comment.getContent();
  }
}
