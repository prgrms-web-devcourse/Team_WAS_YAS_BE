package org.prgrms.yas.domain.routine.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;
import org.prgrms.yas.domain.routine.domain.Week;

@Getter
@NoArgsConstructor
public class RoutineCreateRequest {

  private String name;
  private List<String> routineCategory;
  private LocalDate startTime;
  private LocalDate durationTime;
  private List<String> weeks;
  private String emoji;
  private String color;

  @Builder
  public RoutineCreateRequest(
      String name, LocalDate startTime, LocalDate durationTime, List<String> weeks,
      List<String> routineCategory, String color, String emoji
  ) {
    this.name = name;
    this.startTime = startTime;
    this.durationTime = durationTime;
    this.weeks = weeks;
    this.routineCategory = routineCategory;
    this.color = color;
    this.emoji = emoji;
  }

  public List<Week> getEnumWeeks(List<String> weeks) {
    List<Week> result = new ArrayList<>();
    for (String x : weeks) {
      result.add(Week.valueOf(x));
    }
    return result;
  }

  public List<RoutineCategory> getEnumRoutineCategory(List<String> routineCategory) {
    List<RoutineCategory> result = new ArrayList<>();
    for (String x : routineCategory) {
      result.add(RoutineCategory.valueOf(x));
    }
    return result;
  }
}
