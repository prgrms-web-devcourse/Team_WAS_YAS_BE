package org.prgrms.yas.domain.comment.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.prgrms.yas.domain.BaseEntity;
import org.prgrms.yas.domain.comment.dto.CommentUpdateRequest;
import org.prgrms.yas.domain.likes.domain.CommentLikes;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.prgrms.yas.domain.user.domain.User;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
//@Where(clause = "is_deleted = false")
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "routine_post_id")
  private RoutinePost routinePost;
  
  @OneToMany(mappedBy = "comment")
  private List<CommentLikes> commentLikes = new ArrayList<>();

  @Column(nullable = false, columnDefinition = "TINYINT default false")
  private boolean isDeleted;

  @Builder
  public Comment(String content, User user, RoutinePost routinePost) {
    this.content = content;
    this.user = user;
    this.routinePost = routinePost;
  }

  public void updateComment(final CommentUpdateRequest commentUpdateRequest) {
    this.content = commentUpdateRequest.getContent();
  }
  
  public void setRoutinePost(RoutinePost routinePost) {
    if (Objects.nonNull(this.routinePost)) {
      this.routinePost.getComments()
                      .remove(this);
    }
    this.routinePost = routinePost;
  }
  
  public void addCommentLikes(CommentLikes commentLike) {
    this.commentLikes.add(commentLike);
    commentLike.setComment(this);
  }
}
