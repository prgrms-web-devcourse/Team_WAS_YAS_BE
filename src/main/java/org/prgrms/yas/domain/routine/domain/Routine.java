package org.prgrms.yas.domain.routine.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.dto.MissionDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineListResponse;
import org.prgrms.yas.domain.user.domain.User;

@Entity
@Table(name = "routine")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE routine SET is_deleted = true WHERE id =?")
@DynamicInsert
public class Routine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 60)
	private String name;
	
	@Column(nullable = false, length = 60)
	private String color;
	
	@Column(nullable = false)
	private String emoji;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "routine_category", joinColumns = @JoinColumn(name = "id"))
	@Enumerated(EnumType.STRING)
	private List<RoutineCategory> routineCategory;
	
	@Column(nullable = false)
	private LocalDateTime startGoalTime;
	
	@Column(nullable = false)
	private Long durationGoalTime; // 초가 들어옴
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "week", joinColumns = @JoinColumn(name = "id"))
	@Enumerated(EnumType.STRING)
	private List<Week> weeks = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "routine")
	private List<RoutineStatus> routineStatuses = new ArrayList<>();
	
	@OneToMany(mappedBy = "routine", orphanRemoval = true)
	private List<Mission> missions = new ArrayList<>();
	
	@Column(nullable = false, columnDefinition = "TINYINT default false")
	private boolean isDeleted;
	
	public void addRoutineStatus(RoutineStatus routineCompletion) {
		this.routineStatuses.add(routineCompletion);
		routineCompletion.setRoutine(this);
	}
	
	public Routine addRoutineStatuses(List<RoutineStatus> routineStatuses) {
		routineStatuses.forEach(this::addRoutineStatus);
		return this;
	}
	
	public void addMission(Mission mission) {
		this.missions.add(mission);
		mission.setRoutine(this);
	}
	
	public Routine addMissions(List<Mission> missions) {
		missions.forEach(this::addMission);
		return this;
	}
	
	@Builder
	public Routine(
			User user, String name, Long durationGoalTime, LocalDateTime startGoalTime, List<Week> weeks,
			List<RoutineCategory> routineCategory, String color, String emoji
	) {
		this.user = user;
		this.name = name;
		this.durationGoalTime = durationGoalTime;
		this.startGoalTime = startGoalTime;
		this.weeks = weeks;
		this.routineCategory = routineCategory;
		this.color = color;
		this.emoji = emoji;
	}
	
	
	public List<String> getStringWeeks(List<Week> weeks) {
		List<String> result = new ArrayList<>();
		for (Week week : weeks) {
			result.add(week.toString());
		}
		return result;
	}
	
	public List<String> getStringCategory(List<RoutineCategory> routineCategory) {
		List<String> result = new ArrayList<>();
		for (RoutineCategory category : routineCategory) {
			result.add(category.toString());
		}
		return result;
	}
	
	public void updateRoutine(List<Week> weeks) {
		this.weeks = weeks;
	}
	
	public RoutineListResponse toRoutineListResponse() {
		return RoutineListResponse.builder()
		                          .routineId(id)
		                          .color(color)
		                          .durationGoalTime(durationGoalTime)
		                          .startGoalTime(startGoalTime)
		                          .weeks(getStringWeeks(weeks))
		                          .routineCategory(getStringCategory(routineCategory))
		                          .emoji(emoji)
		                          .name(name)
		                          .build();
	}
	
	public List<MissionDetailResponse> getMissionDetailResponse() {
		List<Mission> missions = this.getMissions();
		return missions.stream()
		               .map(Mission::toMissionDetailResponse)
		               .collect(Collectors.toList());
		
	}
	
	public RoutineDetailResponse toRoutineDetailResponse() {
		return RoutineDetailResponse.builder()
		                            .name(name)
		                            .routineCategory(getStringCategory(routineCategory))
		                            .weeks(getStringWeeks(weeks))
		                            .emoji(emoji)
		                            .color(color)
		                            .missionDetailResponses(getMissionDetailResponse())
		                            .build();
	}
}
