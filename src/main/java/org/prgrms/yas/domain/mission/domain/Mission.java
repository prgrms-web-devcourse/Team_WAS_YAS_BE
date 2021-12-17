package org.prgrms.yas.domain.mission.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.mission.dto.MissionDetailResponse;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.prgrms.yas.domain.mission.dto.MissionDetailResponse;
import org.prgrms.yas.domain.mission.dto.MissionDetailStatusResponse;
import org.prgrms.yas.domain.routine.domain.Routine;

@Entity
@Table(name = "mission")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE mission SET is_deleted = true WHERE id =?")
@DynamicInsert
@DynamicUpdate
public class Mission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(nullable = false)
	private Long durationGoalTime;
	
	@Column(nullable = false)
	private int orders;
	
	@Column(nullable = false)
	private String emoji;
	
	@Column(nullable = false)
	private String color;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_id")
	private Routine routine;
	
	@OneToMany(mappedBy = "mission")
	private List<MissionStatus> missionStatuses = new ArrayList<>();
	
	@Column(nullable = false, columnDefinition = "TINYINT default false")
	private boolean isDeleted;
	
	@Builder
	public Mission(
			String name, Long durationGoalTime, int orders, String emoji, Routine routine, String color
	) {
		this.name = name;
		this.durationGoalTime = durationGoalTime;
		this.orders = orders;
		this.emoji = emoji;
		this.routine = routine;
		this.color = color;
		
	}
	
	public void addMissionStatus(MissionStatus missionStatus) {
		this.missionStatuses.add(missionStatus);
		missionStatus.setMission(this);
	}
	
	public Mission addMissionStatuses(List<MissionStatus> missionStatuses) {
		missionStatuses.forEach(this::addMissionStatus);
		return this;
	}
	
	public void setRoutine(Routine routine) {
		if (Objects.nonNull(this.routine)) {
			this.routine.getMissions()
			            .remove(this);
		}
		this.routine = routine;
	}
	
	public void updateOrders(int orders) {
		this.orders = orders;
	}
	
	public MissionDetailResponse toMissionDetailResponse() {
		return MissionDetailResponse.builder()
		                            .missionId(id)
		                            .orders(orders)
		                            .durationGoalTime(durationGoalTime)
		                            .emoji(emoji)
		                            .color(color)
		                            .name(name)
		                            .build();
	}
	
	
	public MissionDetailStatusResponse toMissionDetailStatusResponse(MissionStatus missionStatus) {
		return MissionDetailStatusResponse.builder()
		                                  .missionStatusDetailResponse(missionStatus.toMissionStatusDetailResponse())
		                                  .name(name)
		                                  .durationGoalTime(durationGoalTime)
		                                  .missionId(id)
		                                  .color(color)
		                                  .emoji(emoji)
		                                  .orders(orders)
		                                  .build();
	}
}
