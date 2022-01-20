package org.prgrms.yas.domain.routine.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.routine.dto.RoutineStatusCreateRequest;
import org.prgrms.yas.domain.routine.service.RoutineStatusService;
import org.prgrms.yas.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/routines")
@RequiredArgsConstructor
public class RoutineStatusController {
	
	private final RoutineStatusService routineStatusService;
	
	@Operation(summary = "루틴진행 추가 컨트롤러")
	@PutMapping(value = "/routineStatus", consumes = {"multipart/form-data"})
	public ResponseEntity<ApiResponse<Long>> updateRoutineStatus(
			@Valid @RequestPart RoutineStatusCreateRequest routineStatusCreateRequest,
			@RequestPart(value = "file", required = false) List<MultipartFile> files
	) throws IOException {
		return ResponseEntity.ok(ApiResponse.of(routineStatusService.updateRoutineStatus(
				routineStatusCreateRequest,
				files
		)));
	}
}
