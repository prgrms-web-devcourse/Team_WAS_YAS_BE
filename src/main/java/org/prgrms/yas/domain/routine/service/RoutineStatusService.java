package org.prgrms.yas.domain.routine.service;

import static org.prgrms.yas.global.error.ErrorCode.NOT_FOUND_RESOURCE_ERROR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.prgrms.yas.domain.routine.domain.RoutineStatusImage;
import org.prgrms.yas.domain.routine.dto.RoutineStatusCreateRequest;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineStatusException;
import org.prgrms.yas.domain.routine.repository.RoutineStatusImageRepository;
import org.prgrms.yas.domain.routine.repository.RoutineStatusRepository;
import org.prgrms.yas.global.aws.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RoutineStatusService {
	
	private static final String DIRECTORY = "static/review";
	private final S3Uploader s3Uploader;
	private final RoutineStatusRepository routineStatusRepository;
	private final RoutineStatusImageRepository routineStatusImageRepository;
	
	@Transactional
	public Long createRoutineStatus(
			RoutineStatusCreateRequest routineStatusCreateRequest, List<MultipartFile> files
	) throws IOException {
		RoutineStatus routineStatus = routineStatusRepository.findById(routineStatusCreateRequest.getRoutineStatusId())
		                                                     .orElseThrow(() -> new NotFoundRoutineStatusException(NOT_FOUND_RESOURCE_ERROR));
		if (files.size() > 0) {
			for (MultipartFile file : files) {
				routineStatusCreateRequest.setReviewImages(s3Uploader.upload(
						file,
						DIRECTORY
				));
			}
		}
		
		List<RoutineStatusImage> routineStatusImages = new ArrayList<>();
		for (String image : routineStatusCreateRequest.getReviewImages()) {
			RoutineStatusImage routineStatusImage = RoutineStatusImage.builder()
			                                                          .routineStatus(routineStatus)
			                                                          .reviewImage(image)
			                                                          .build();
			
			RoutineStatusImage savedRoutineStatusImage = routineStatusImageRepository.save(routineStatusImage);
			routineStatusImages.add(savedRoutineStatusImage);
		}
		routineStatus.createRoutineStatus(routineStatusCreateRequest,
				routineStatusImages);
		
		return routineStatusCreateRequest.getRoutineStatusId();
	}
}
