package org.prgrms.yas.domain.routine.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.routine.domain.Week;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineDetailResponse {

  private Long routineId;
  private String name;
  private List<String> routineCategory;
  private LocalDateTime startGoalTime;
  private Long durationGoalTime;
  private List<String> weeks;
  private String color;
  private String emoji;

  @Builder
  public RoutineDetailResponse(
      Long routineId, String name, LocalDateTime startGoalTime, Long durationGoalTime,
      List<String> weeks, List<String> routineCategory, String emoji, String color
  ) {
    this.routineId = routineId;
    this.name = name;
    this.startGoalTime = startGoalTime;
    this.durationGoalTime = durationGoalTime;
    this.weeks = weeks;
    this.routineCategory = routineCategory;
    this.color = color;
    this.emoji = emoji;
  }

  public List<String> getStringWeeks(List<Week> weeks) {
    List<String> result = new ArrayList<>();
    for (Week week : weeks) {
      result.add(week.toString());
    }
    return result;
  }
}
