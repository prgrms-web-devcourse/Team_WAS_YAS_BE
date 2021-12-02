package org.prgrms.yas.domain.routine.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routine_completion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineCompletion {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime localDateTime;

  @Column(nullable = false, columnDefinition = "TINYINT default false")
  private boolean pass;

  @Column(nullable = false)
  private String startTime;

  @Column(nullable = false)
  private String goalTime;

  @Column(nullable = false)
  private String duration;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "routine_id")
  private Routine routine;

  public void setRoutine(Routine routine) {
    if (Objects.nonNull(this.routine)) {
      this.routine.getRoutineCompletions().remove(this);
    }
    this.routine = routine;
  }
}
