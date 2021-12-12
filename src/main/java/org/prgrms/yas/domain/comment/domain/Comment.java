package org.prgrms.yas.domain.comment.domain;

import java.time.LocalDateTime;
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
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.prgrms.yas.domain.BaseEntity;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.user.domain.User;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "routine_post_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private RoutinePost routinePost;

  @Column(nullable = false, columnDefinition = "TINYINT default false")
  private boolean isDeleted;

  @Builder
  public Comment(String content, User user, RoutinePost routinePost) {
    this.content = content;
    this.user = user;
    this.routinePost = routinePost;
  }

  public void setRoutinePost(RoutinePost routinePost) {
    if (Objects.nonNull(this.routinePost)) {
      this.routinePost.getComments().remove(this);
    }
    this.routinePost = routinePost;
  }
}
