package org.prgrms.yas.domain.routine.service;

import static java.util.stream.Collectors.toList;
import static org.prgrms.yas.global.error.ErrorCode.NOT_FOUND_RESOURCE_ERROR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.prgrms.yas.domain.routine.domain.RoutineStatusImage;
import org.prgrms.yas.domain.routine.dto.RoutineStatusCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineStatusDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineStatusListResponse;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineStatusException;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineStatusImageException;
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
	private static final String DELETE_DIRECTORY = "static/review/";
	private final S3Uploader s3Uploader;
	private final RoutineStatusRepository routineStatusRepository;
	private final RoutineStatusImageRepository routineStatusImageRepository;
	private final int YEAR_MONTH = 7;
	private final int START_UUID = 65;
	private final int END_UUID = 130;
	private final int FILE_NAME = 133;
	
	
	@Transactional
	public Long updateRoutineStatus(
			RoutineStatusCreateRequest routineStatusCreateRequest, List<MultipartFile> files
	) throws IOException {
		RoutineStatus routineStatus = routineStatusRepository.findById(routineStatusCreateRequest.getRoutineStatusId())
		                                                     .orElseThrow(() -> new NotFoundRoutineStatusException(NOT_FOUND_RESOURCE_ERROR));
		//이미지 파일 추가
		if (files != null) {
			for (MultipartFile file : files) {
				routineStatusCreateRequest.setReviewImages(s3Uploader.upload(
						file,
						DIRECTORY
				));
			}
		}
		
		//이미지 파일 삭제
		if (routineStatusCreateRequest.getDeletedImages()
		                              .size() > 0) {
			for (Long deletedImage : routineStatusCreateRequest.getDeletedImages()) {
				RoutineStatusImage routineStatusImage = routineStatusImageRepository.findById(deletedImage)
				                                                                    .orElseThrow(() -> new NotFoundRoutineStatusImageException(NOT_FOUND_RESOURCE_ERROR));
				
				s3Uploader.delete(DELETE_DIRECTORY + routineStatusImage.getReviewImage()
				                                                       .substring(
						                                                       START_UUID,
						                                                       END_UUID
				                                                       ) + "@"
						+ routineStatusImage.getReviewImage()
						                    .substring(FILE_NAME));
				routineStatusImageRepository.delete(routineStatusImage);
			}
		}
		List<RoutineStatusImage> routineStatusImages = new ArrayList<>();
		if (files != null) {
			for (String image : routineStatusCreateRequest.getReviewImages()) {
				RoutineStatusImage routineStatusImage = RoutineStatusImage.builder()
				                                                          .routineStatus(routineStatus)
				                                                          .reviewImage(image)
				                                                          .build();
				
				RoutineStatusImage savedRoutineStatusImage = routineStatusImageRepository.save(routineStatusImage);
				routineStatusImages.add(savedRoutineStatusImage);
			}
		}
		routineStatus.createRoutineStatus(
				routineStatusCreateRequest,
				routineStatusImages
		);
		
		return routineStatusCreateRequest.getRoutineStatusId();
	}
	
	public RoutineStatus findRoutineStatus(Long routineId, String date) {
		List<RoutineStatus> routineStatuses = new ArrayList<>();
		routineStatuses = routineStatusRepository.getByDate(date);
		
		Predicate<RoutineStatus> isRoutine = routineStatus -> (routineStatus.getRoutine()
		                                                                    .getId()
		                                                                    .equals(routineId));
		
		return routineStatuses.stream()
		                      .filter(isRoutine)
		                      .collect(toList())
		                      .get(0);
	}
	
	public List<RoutineStatus> findRoutineStatusByDate(String date, Long userId) {
		List<RoutineStatus> routineStatuses = new ArrayList<>();
		if (date.length() == YEAR_MONTH) {
			routineStatuses = routineStatusRepository.getByDays(date);
		} else {
			routineStatuses = routineStatusRepository.getByDate(date);
		}
		Predicate<RoutineStatus> isUSer = routineStatus -> (routineStatus.getRoutine()
		                                                                 .getUser()
		                                                                 .getId()
		                                                                 .equals(userId));
		return routineStatuses.stream()
		                      .filter(isUSer)
		                      .collect(toList());
		
	}
	
	@Transactional
	public List<RoutineStatusListResponse> getRoutineStatuses(String date, Long userId) {
		List<RoutineStatus> routineStatusFourUser = findRoutineStatusByDate(
				date,
				userId
		);
		
		return routineStatusFourUser.stream()
		                            .map(RoutineStatus::toRoutineStatusListResponse)
		                            .collect(toList());
	}
	
	@Transactional
	public RoutineStatusDetailResponse getRoutineStatus(Long statusId) {
		RoutineStatus routineStatus = routineStatusRepository.findById(statusId)
		                                                     .orElseThrow(() -> new NotFoundRoutineStatusException(NOT_FOUND_RESOURCE_ERROR));
		
		List<Mission> missions = routineStatus.getRoutine()
		                                      .getMissions();
		return routineStatus.toRoutineStatusDetailResposne(missions);
	}
	
	public Long deleteRoutineStatus(Long id) {
		routineStatusRepository.deleteById(id);
		return id;
	}
}
