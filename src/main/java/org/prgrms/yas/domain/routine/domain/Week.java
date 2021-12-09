package org.prgrms.yas.domain.routine.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Week {
  MON("MON"), TUE("TUE"), WED("WED"), THU("THU"), FRI("FRI"), SAT("SAT"), SUN("SUN");

  private String week;

  Week(String week) {
    this.week = week;
  }

  //  public List<Week> getWeeks(List<String> weeks){
  //    List<Week> result = new ArrayList<>();
  //    for (String x : weeks) {
  //      result.add(valueOf(x));
  //    }
  //    return  result;
  //  }
}
