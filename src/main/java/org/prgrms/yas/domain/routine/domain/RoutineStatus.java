package org.prgrms.yas.domain.routine.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.hibernate.annotations.ColumnDefault;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.routine.dto.RoutineStatusCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineStatusDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineStatusImageDto;
import org.prgrms.yas.domain.routine.dto.RoutineStatusListResponse;

@Entity
@Table(name = "routine_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private ZonedDateTime startTime;
	
	private ZonedDateTime endTime;
	
	private ZonedDateTime dateTime;
	
	@ColumnDefault("0")
	private Integer emoji;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@Builder
	public RoutineStatus(
			ZonedDateTime startTime, ZonedDateTime endTime, ZonedDateTime dateTime, Integer emoji,
			String content, List<RoutineStatusImage> routineStatusImages, Long userDurationTime,
			Routine routine
	) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.dateTime = dateTime;
		this.emoji = emoji;
		this.content = content;
		this.routineStatusImages = routineStatusImages;
		this.userDurationTime = userDurationTime;
		this.routine = routine;
	}
	
	@OneToMany(mappedBy = "routineStatus")
	private List<RoutineStatusImage> routineStatusImages = new ArrayList<>();
	
	@ColumnDefault("-1")
	private Long userDurationTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_id")
	private Routine routine;
	
	public void setRoutine(Routine routine) {
		if (Objects.nonNull(this.routine)) {
			this.routine.getRoutineStatuses()
			            .remove(this);
		}
		this.routine = routine;
	}
	
	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}
	
	public void setEndTimeIsNull() {
		this.endTime = null;
	}
	
	public void setUserDurationTime(Long userDurationTime) {
		this.userDurationTime = userDurationTime;
	}
	
	public void createRoutineStatus(
			RoutineStatusCreateRequest routineStatusCreateRequest,
			List<RoutineStatusImage> routineStatusImages
	) {
		this.emoji = routineStatusCreateRequest.getEmoji();
		this.content = routineStatusCreateRequest.getContent();
		this.routineStatusImages = routineStatusImages;
	}
	
	public void addRoutineStatusImage(RoutineStatusImage routineStatusImage) {
		this.routineStatusImages.add(routineStatusImage);
		routineStatusImage.setRoutineStatus(this);
	}
	
	public RoutineStatus addRoutineStatusImages(List<RoutineStatusImage> routineStatusImages) {
		routineStatusImages.forEach(this::addRoutineStatusImage);
		return this;
	}
	
	public RoutineStatusListResponse toRoutineStatusListResponse() {
		return RoutineStatusListResponse.builder()
		                                .routineListResponse(routine.toRoutineListResponse())
		                                .routineStatusId(id)
		                                .dateTime(dateTime)
		                                .build();
	}
	
	public List<RoutineStatusImageDto> toRoutineStatusImageDtos() {
		List<RoutineStatusImageDto> routineStatusImageDtos = new ArrayList<>();
		for (RoutineStatusImage routineStatusImage : this.getRoutineStatusImages()) {
			routineStatusImageDtos.add(RoutineStatusImageDto.builder()
			                                                .routineStatusImageId(routineStatusImage.getId())
			                                                .imageUrl(routineStatusImage.getReviewImage())
			                                                .build());
		}
		return routineStatusImageDtos;
	}
	
	public RoutineStatusDetailResponse toRoutineStatusDetailResposne(List<Mission> missions) {
		return RoutineStatusDetailResponse.builder()
		                                  .routineStatusImage(toRoutineStatusImageDtos())
		                                  .routineStatusId(id)
		                                  .dateTime(dateTime)
		                                  .routineDetailResponse(routine.toRoutineDetailResponse(missions))
		                                  .build();
	}
}
