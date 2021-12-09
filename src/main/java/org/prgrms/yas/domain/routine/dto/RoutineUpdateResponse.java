package org.prgrms.yas.domain.routine.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.prgrms.yas.domain.routine.domain.Week;

@Getter
public class RoutineUpdateResponse {

  private Long routineId;
  private String name;
  private List<String> routineCategory;
  private LocalDate startTime;
  private LocalDate durationTime;
  private List<String> weeks;
  private String color;
  private String emoji;

  @Builder
  public RoutineUpdateResponse(
      Long routineId, String name, LocalDate startTime, LocalDate durationTime, List<String> weeks,
      List<String> routineCategory, String color, String emoji
  ) {
    this.routineId = routineId;
    this.name = name;
    this.startTime = startTime;
    this.durationTime = durationTime;
    this.weeks = weeks;
    this.routineCategory = routineCategory;
    this.color = color;
    this.emoji = emoji;
  }

  public List<String> getStringWeeks(List<Week> weeks) {
    List<String> result = new ArrayList<>();
    for (Week x : weeks) {
      result.add(x.toString());
    }
    return result;
  }

}
