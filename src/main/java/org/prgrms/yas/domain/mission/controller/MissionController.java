package org.prgrms.yas.domain.mission.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.dto.MissionCreateRequest;
import org.prgrms.yas.domain.mission.dto.MissionDetailResponse;
import org.prgrms.yas.domain.mission.dto.MissionUpdateRequest;
import org.prgrms.yas.domain.mission.service.MissionService;
import org.prgrms.yas.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/routines")
@RequiredArgsConstructor
public class MissionController {
	
	private final MissionService missionService;
	
	@Operation(summary = "미션 생성 컨트롤러")
	@PostMapping("/{id}/missions")
	public ResponseEntity<ApiResponse<Long>> create(
			@Valid @RequestBody MissionCreateRequest missionCreateRequest,
			@PathVariable("id") Long routineId
	) {
		Long missionId = missionService.saveMission(
				routineId,
				missionCreateRequest
		);
		return ResponseEntity.ok(ApiResponse.of(missionId));
	}
	
	@Operation(summary = "미션 삭제 컨트롤러")
	@DeleteMapping("/{routineId}/missions/{id}")
	public ResponseEntity<ApiResponse<Long>> delete(
			@PathVariable("routineId") Long routineId, @PathVariable("id") Long missionId
	) {
		Long deleteMissionId = missionService.deleteMission(routineId,
				missionId);
		return ResponseEntity.ok(ApiResponse.of(deleteMissionId));
	}
	
	@Operation(summary = "루틴 수정 컨트롤러")
	@PutMapping("/{id}/missions")
	public ResponseEntity<ApiResponse<List<MissionDetailResponse>>> update(
			@Valid @RequestBody MissionUpdateRequest missionUpdateRequest,
			@PathVariable("id") Long routineId
	) {
		List<MissionDetailResponse> responses = missionService.updateMission(
				routineId,
				missionUpdateRequest
		);
		return ResponseEntity.ok(ApiResponse.of(responses));
	}
}