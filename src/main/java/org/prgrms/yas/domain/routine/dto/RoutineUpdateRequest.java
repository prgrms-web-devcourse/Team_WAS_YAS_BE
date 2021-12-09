package org.prgrms.yas.domain.routine.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.routine.domain.Week;

@Getter
@NoArgsConstructor
public class RoutineUpdateRequest {

  private List<String> weeks;

  public List<Week> getEnumWeeks(List<String> weeks) {
    List<Week> result = new ArrayList<>();
    for (String week : weeks) {
      result.add(Week.valueOf(week));
    }
    return result;
  }

  @Builder
  public RoutineUpdateRequest(List<String> weeks) {
    this.weeks = weeks;
  }
}
