package org.prgrms.yas.domain.mission.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.dto.MissionStatusCreateRequest;
import org.prgrms.yas.domain.mission.dto.MissionStatusUpdateRequest;
import org.prgrms.yas.domain.mission.service.MissionStatusService;
import org.prgrms.yas.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mission/{id}/mission-status")
@RequiredArgsConstructor
public class MissionStatusController {
	
	private final MissionStatusService missionStatusService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Long>> create(
			@Valid @RequestBody MissionStatusCreateRequest missionStatusCreateRequest,
			@PathVariable("id") Long missionId
	) {
		Long missionStatusId = missionStatusService.saveMissionStatus(
				missionId,
				missionStatusCreateRequest
		);
		return ResponseEntity.ok(ApiResponse.of(missionStatusId));
	}
	
	@PatchMapping
	public ResponseEntity<ApiResponse<Long>> update(
			@Valid @RequestBody MissionStatusUpdateRequest missionStatusUpdateRequest,
			@PathVariable("id") Long missionId
	) {
		Long missionStatusId = missionStatusService.updateMissionStatus(missionId,
				missionStatusUpdateRequest);
		return ResponseEntity.ok(ApiResponse.of(missionStatusId));
	}
}
