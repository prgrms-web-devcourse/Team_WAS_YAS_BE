package org.prgrms.yas.domain.mission.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.prgrms.yas.domain.routine.domain.Routine;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "mission")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, length = 30)
  private String name;

  @Column(nullable = false)
  private LocalDateTime goalTime;

  @Column(nullable = false)
  private int order;

  @Column(nullable = false)
  private String emoji;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "routine_id")
  private Routine routine;

  @Column(nullable = false, columnDefinition = "TINYINT default false")
  private boolean isDeleted;
}
