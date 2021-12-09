package org.prgrms.yas.domain.routine.dto;

import java.time.LocalDate;
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
    for (String x : weeks) {
      result.add(Week.valueOf(x));
    }
    return result;
  }

  @Builder
  public RoutineUpdateRequest(List<String> weeks) {
    this.weeks = weeks;
  }


}
