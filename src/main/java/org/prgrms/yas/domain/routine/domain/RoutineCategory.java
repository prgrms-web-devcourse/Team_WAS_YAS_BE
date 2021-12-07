package org.prgrms.yas.domain.routine.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoutineCategory {
  EXERCISE("exercise"), HEALTH("health"), FOOD("food");

  private String routineCategory;

  RoutineCategory(String routineCategory) {
    this.routineCategory = routineCategory;
  }
}
