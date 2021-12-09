package org.prgrms.yas.domain.routine.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;
import org.prgrms.yas.domain.routine.domain.Week;
import org.prgrms.yas.domain.user.domain.User;

@Getter
public class RoutineCreateResponse {

  private Long routineId;
  private String name;
  private List<String> routineCategory;
  private LocalDate startTime;
  private LocalDate durationTime;
  private List<String> weeks;
  private String color;
  private String emoji;

  @Builder
  public RoutineCreateResponse(
      Long routineId, String name, LocalDate startTime, LocalDate durationTime, List<String> weeks,
      List<String> routineCategory, String emoji, String color
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
