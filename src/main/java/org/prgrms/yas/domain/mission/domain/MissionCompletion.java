package org.prgrms.yas.domain.mission.domain;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "mission_completion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionCompletion {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private int orders;

  private Long userDurationTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mission_id")
  private Mission mission;


  public void setMission(Mission mission) {
    if (Objects.nonNull(this.mission)) {
      this.mission.getMissionCompletions().remove(this);
    }
    this.mission = mission;
  }
}