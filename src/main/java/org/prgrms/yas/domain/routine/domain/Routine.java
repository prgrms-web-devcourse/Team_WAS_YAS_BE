package org.prgrms.yas.domain.routine.domain;

import java.time.ZonedDateTime;
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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.dto.MissionDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineListResponse;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateResponse;
import org.prgrms.yas.domain.user.domain.User;

@Entity
@Table(name = "routine")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
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
	private ZonedDateTime startGoalTime;
	
	@Column(nullable = false)
	@ColumnDefault("0")
	private Long durationGoalTime;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "week", joinColumns = @JoinColumn(name = "id"))
	@Enumerated(EnumType.STRING)
	private List<Week> weeks = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "routine", cascade = CascadeType.REMOVE)
	private List<RoutineStatus> routineStatuses = new ArrayList<>();
	
	@OneToMany(mappedBy = "routine", orphanRemoval = true)
	private List<Mission> missions = new ArrayList<>();
	
	@Column(nullable = false, columnDefinition = "TINYINT default false")
	private boolean isDeleted;
	
	@Column(nullable = false, columnDefinition = "TINYINT default false")
	private boolean isPosted;
	
	public void deleteRoutine() {
		this.isDeleted = true;
	}
	
	public void updateIsPostedTrue() {
		this.isPosted = true;
	}
	
	public void updateIsPostedFalse() {
		this.isPosted = false;
	}
	
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
			User user, String name, Long durationGoalTime, ZonedDateTime startGoalTime, List<Week> weeks,
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
	
	public List<MissionDetailResponse> getMissionDetailResponse(List<Mission> missions) {
		return missions.stream()
		               .map(Mission::toMissionDetailResponse)
		               .collect(Collectors.toList());
	}
	
	public RoutineListResponse toRoutineListResponse() {
		return RoutineListResponse.builder()
		                          .routineId(id)
		                          .color(color)
		                          .durationGoalTime(durationGoalTime)
		                          .startGoalTime(startGoalTime)
		                          .isPosted(isPosted)
		                          .weeks(getStringWeeks(weeks))
		                          .routineCategory(getStringCategory(routineCategory))
		                          .emoji(emoji)
		                          .name(name)
		                          .build();
	}
	
	public RoutineDetailResponse toRoutineDetailResponse(List<Mission> missions) {
		return RoutineDetailResponse.builder()
		                            .name(name)
		                            .routineCategory(getStringCategory(routineCategory))
		                            .startGoalTime(startGoalTime)
		                            .durationGoalTime(durationGoalTime)
		                            .isPosted(isPosted)
		                            .weeks(getStringWeeks(weeks))
		                            .emoji(emoji)
		                            .color(color)
		                            .missionDetailResponses(getMissionDetailResponse(missions))
		                            .build();
	}
	
	public RoutineUpdateResponse toRoutineUpdateResponse() {
		return RoutineUpdateResponse.builder()
		                            .name(name)
		                            .routineId(id)
		                            .startGoalTime(startGoalTime)
		                            .durationGoalTime(durationGoalTime)
		                            .weeks(this.getStringWeeks(this.getWeeks()))
		                            .routineCategory(this.getStringCategory(this.getRoutineCategory()))
		                            .color(color)
		                            .emoji(emoji)
		                            .build();
	}
	
	public void addDurationGoalTime(Long durationGoalTime) {
		this.durationGoalTime += durationGoalTime;
	}
	
	public void minusDurationGoalTime(Long durationGoalTime) {
		this.durationGoalTime -= durationGoalTime;
	}
}
