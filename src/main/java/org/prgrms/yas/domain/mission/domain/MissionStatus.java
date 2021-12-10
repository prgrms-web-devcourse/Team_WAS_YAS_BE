package org.prgrms.yas.domain.mission.domain;

import java.time.LocalDateTime;
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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "mission_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private int orders;

  @ColumnDefault("-1")
  private Long userDurationTime;

  private LocalDateTime startTime;
  private LocalDateTime endTime;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mission_id")
  private Mission mission;


  public void setMission(Mission mission) {
    if (Objects.nonNull(this.mission)) {
      this.mission.getMissionStatuses()
                  .remove(this);
    }
    this.mission = mission;
  }
}