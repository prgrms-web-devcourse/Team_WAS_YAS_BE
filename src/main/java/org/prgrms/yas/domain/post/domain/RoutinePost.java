package org.prgrms.yas.domain.post.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.BaseEntity;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineCompletion;
import org.prgrms.yas.domain.user.domain.User;

@Table(name = "routine_post")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutinePost extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "routine_id")
  private Routine routine;

  @OneToMany(mappedBy = "routinePost")
  private List<Comment> comments = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public void addComment(Comment comment) {
    this.comments.add(comment);
    comment.setRoutinePost(this);
  }

  public RoutinePost addComment(List<Comment> comments) {
    comments.forEach(this::addComment);
    return this;
  }
}
