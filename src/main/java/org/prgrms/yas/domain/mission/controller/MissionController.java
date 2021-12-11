package org.prgrms.yas.domain.mission.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.dto.MissionCreateRequest;
import org.prgrms.yas.domain.mission.service.MissionService;
import org.prgrms.yas.domain.routine.dto.RoutineDeleteResponse;
import org.prgrms.yas.global.response.ApiResponse;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/routines")
@RequiredArgsConstructor
public class MissionController {
	
	private final MissionService missionService;
	
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
	
	@DeleteMapping("missions/{id}")
	public ResponseEntity<ApiResponse<Long>> delete(@PathVariable("id") Long missionId) {
		Long deleteMissionId = missionService.deleteMission(missionId);
		return ResponseEntity.ok(ApiResponse.of(deleteMissionId));
	}
}