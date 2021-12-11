package org.prgrms.yas.domain.routine.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineCreateResponse;
import org.prgrms.yas.domain.routine.dto.RoutineDeleteResponse;
import org.prgrms.yas.domain.routine.dto.RoutineDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateResponse;
import org.prgrms.yas.domain.routine.service.RoutineService;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/routines")
@RequiredArgsConstructor
public class RoutineController {
	
	private final RoutineService routineService;
	
	@PostMapping
	public ResponseEntity<RoutineCreateResponse> create(
			@Valid @RequestBody RoutineCreateRequest routineCreateRequest,
			@AuthenticationPrincipal JwtAuthentication token
	
	) {
		RoutineCreateResponse routineCreateResponse = routineService.saveRoutine(
				token.getId(),
				routineCreateRequest
		);
		return ResponseEntity.ok(routineCreateResponse);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<RoutineDeleteResponse> delete(@PathVariable("id") Long id)
			throws NotFoundException {
		RoutineDeleteResponse routineDeleteResponse = routineService.deleteRoutine(id);
		return ResponseEntity.ok(routineDeleteResponse);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<RoutineUpdateResponse> update(
			@PathVariable("id") Long id, @Valid @RequestBody RoutineUpdateRequest routineUpdateRequest
	) throws NotFoundException {
		RoutineUpdateResponse routineUpdateResponse = routineService.updateRoutine(
				id,
				routineUpdateRequest
		);
		return ResponseEntity.ok(routineUpdateResponse);
	}
	
	@GetMapping
	public ResponseEntity<List<RoutineDetailResponse>> get(
			@AuthenticationPrincipal JwtAuthentication token
	) throws NotFoundException {
		List<RoutineDetailResponse> routineDetailResponses = routineService.findRoutines(token.getId());
		return ResponseEntity.ok(routineDetailResponses);
	}
}
