package org.prgrms.yas.domain.mission.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.dto.MissionCreateRequest;
import org.prgrms.yas.domain.mission.dto.MissionCreateResponse;
import org.prgrms.yas.domain.mission.service.MissionService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/routines/{id}/missions")
@RequiredArgsConstructor
public class MissionController {
	
	private final MissionService missionService;
	
	@PostMapping
	public ResponseEntity<MissionCreateResponse> create(
			@Valid @RequestBody MissionCreateRequest missionCreateRequest,
			@PathVariable("id") Long routineId
	) throws NotFoundException {
		MissionCreateResponse missionCreateResponse = missionService.saveMission(
				routineId,
				missionCreateRequest
		);
		return ResponseEntity.ok(missionCreateResponse);
	}
}
