package org.prgrms.yas.domain.mission.domain;

import java.time.LocalDate;
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

  @Column(nullable = false,length = 30)
  private String name;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false, columnDefinition = "TINYINT default false")
  private boolean pass;

  @Column(nullable = false)
  private String goalTime;

  @Column(nullable = false)
  private int order;

  @Column(nullable = false)
  private String emoji;

  @Column(nullable = false)
  private String durationTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mission_id")
  private Mission mission;
}
