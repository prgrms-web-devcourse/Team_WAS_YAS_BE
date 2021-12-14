package org.prgrms.yas.domain.mission.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.prgrms.yas.domain.mission.dto.MissionStatusDetailResponse;

@Entity
@Table(name = "mission_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MissionStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	@Builder
	public MissionStatus(
			int orders, Long userDurationTime, LocalDateTime startTime, LocalDateTime endTime,
			Mission mission
	) {
		this.orders = orders;
		this.userDurationTime = userDurationTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.mission = mission;
	}
	
	public void setMission(Mission mission) {
		if (Objects.nonNull(this.mission)) {
			this.mission.getMissionStatuses()
			            .remove(this);
		}
		this.mission = mission;
	}
	
	public void updateEndTime(
			int orders, Long userDurationTime, LocalDateTime endTime
	) {
		this.orders = orders;
		this.userDurationTime = userDurationTime;
		this.endTime = endTime;
	}
	
	public void updateStartTime(
			int orders, LocalDateTime startTime
	) {
		this.orders = orders;
		this.startTime = startTime;
	}
	
	public Optional<MissionStatusDetailResponse> toMissionStatusDetailResponse() {
		return Optional.ofNullable(MissionStatusDetailResponse.builder()
		                                                      .endTime(endTime)
		                                                      .orders(orders)
		                                                      .startTime(startTime)
		                                                      .build());
	}
}
