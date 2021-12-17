package org.prgrms.yas.domain.post.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.prgrms.yas.domain.BaseEntity;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.routine.domain.Routine;

@Table(name = "routine_post")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE routine_post SET is_deleted = true WHERE id =?")
public class RoutinePost extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false, columnDefinition = "TINYINT default false")
  private boolean isDeleted;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "routine_id")
  private Routine routine;

  @OneToMany(mappedBy = "routinePost", orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();
  
  @Builder
  public RoutinePost(Routine routine) {
    this.routine = routine;
  }
  
  public void addComment(Comment comment) {
    this.comments.add(comment);
    comment.setRoutinePost(this);
  }

  public RoutinePost addComment(List<Comment> comments) {
    comments.forEach(this::addComment);
    return this;
  }
}
