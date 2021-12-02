package org.prgrms.yas.domain.routine.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import org.prgrms.yas.domain.user.domain.User;

@Entity
@Table(name = "routine")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false,length = 60)
  private String name;

  @Column(nullable = false)
  private RoutineCategory routineCategory;

  @Column(nullable = false)
  private LocalDate startTime;

  @Column(nullable = false)
  private LocalDate goalTime;

  @Column(nullable = false)
  private Week week;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "routine")
  private List<RoutineCompletion> routineCompletions = new ArrayList<>();

  @Column(nullable = false, columnDefinition = "TINYINT default false")
  private boolean isDeleted;

  public void addRoutineCompletion(RoutineCompletion routineCompletion) {
    this.routineCompletions.add(routineCompletion);
    routineCompletion.setRoutine(this);
  }

  public Routine addRoutineCompletions(List<RoutineCompletion> routineCompletions) {
    routineCompletions.forEach(this::addRoutineCompletion);
    return this;
  }
}
