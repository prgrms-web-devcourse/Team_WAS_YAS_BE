package org.prgrms.yas.domain.mission.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.dto.MissionDetailStatusResponse;
import org.prgrms.yas.domain.mission.dto.MissionStatusCreateResponse;
import org.prgrms.yas.domain.mission.dto.MissionStatusUpdateRequest;
import org.prgrms.yas.domain.mission.service.MissionStatusService;
import org.prgrms.yas.domain.post.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/routines/{id}/mission-status")
@RequiredArgsConstructor
public class MissionStatusController {
	
	private final MissionStatusService missionStatusService;
	
	@Operation(summary = "미션상태 등록 컨트롤러")
	@PostMapping
	public ResponseEntity<ApiResponse<MissionStatusCreateResponse>> create(
			@PathVariable("id") Long routineId
	) {
		return ResponseEntity.ok(ApiResponse.of(missionStatusService.saveMissionStatus(routineId)));
	}
	
	@Operation(summary = "미션상태 수정 컨트롤러")
	@PutMapping
	public ResponseEntity<ApiResponse<Long>> update(
			@Valid @RequestBody MissionStatusUpdateRequest missionStatusUpdateRequest,
			@PathVariable("id") Long routineId
	) {
		Long missionStatusId = missionStatusService.updateMissionStatus(
				routineId,
				missionStatusUpdateRequest
		);
		return ResponseEntity.ok(ApiResponse.of(missionStatusId));
	}
	
	@Operation(summary = "미션상태 조회 컨트롤러")
	@GetMapping
	public ResponseEntity<ApiResponse<List<MissionDetailStatusResponse>>> getMissionStatuses(
			@PathVariable("id") Long routineId
	) {
		List<MissionDetailStatusResponse> missionDetailStatusResponses = missionStatusService.getMissionStatuses(routineId);
		return ResponseEntity.ok(ApiResponse.of(missionDetailStatusResponses));
	}
}
