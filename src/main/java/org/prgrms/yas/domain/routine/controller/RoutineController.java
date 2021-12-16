package org.prgrms.yas.domain.routine.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineListResponse;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateResponse;
import org.prgrms.yas.domain.routine.service.RoutineService;
import org.prgrms.yas.global.response.ApiResponse;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/routines")
@RequiredArgsConstructor
public class RoutineController {
	
	private final RoutineService routineService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Long>> create(
			@Valid @RequestBody RoutineCreateRequest routineCreateRequest,
			@AuthenticationPrincipal JwtAuthentication token
	
	) {
		Long routineId = routineService.saveRoutine(
				token.getId(),
				routineCreateRequest
		);
		return ResponseEntity.ok(ApiResponse.of(routineId));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Long>> delete(@PathVariable("id") Long id) {
		Long deletedRoutineId = routineService.deleteRoutine(id);
		return ResponseEntity.ok(ApiResponse.of(deletedRoutineId));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<RoutineUpdateResponse>> update(
			@PathVariable("id") Long id, @Valid @RequestBody RoutineUpdateRequest routineUpdateRequest
	) {
		RoutineUpdateResponse routineUpdateResponse = routineService.updateRoutine(
				id,
				routineUpdateRequest
		);
		return ResponseEntity.ok(ApiResponse.of(routineUpdateResponse));
	}
	
	
	@GetMapping("/{id}/missions")
	public ResponseEntity<ApiResponse<RoutineDetailResponse>> getMissions(
			@PathVariable("id") Long routineId
	) {
		RoutineDetailResponse routineDetailResponse = routineService.findMissions(routineId);
		return ResponseEntity.ok(ApiResponse.of(routineDetailResponse));
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<RoutineListResponse>>> getRoutines(
			@AuthenticationPrincipal JwtAuthentication token, @RequestParam Optional<String> status
	) {
		return ResponseEntity.ok(ApiResponse.of(status.map(biddingStatus -> routineService.findFinishRoutines(
				                                              token.getId(),
				                                              biddingStatus
		                                              ))
		                                              .orElse(routineService.findRoutines(token.getId()))));
	}
}
