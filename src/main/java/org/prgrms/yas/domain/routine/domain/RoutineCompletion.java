package org.prgrms.yas.domain.routine.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
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

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private String userDurationTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "routine_id")
  private Routine routine;

  @Builder
  public RoutineCompletion(
      Long id, LocalDateTime startTime, LocalDateTime endTime, String userDurationTime,
      Routine routine
  ) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.userDurationTime = userDurationTime;
    this.routine = routine;
  }

  public void setRoutine(Routine routine) {
    if (Objects.nonNull(this.routine)) {
      this.routine.getRoutineCompletions()
                  .remove(this);
    }
    this.routine = routine;
  }

}
