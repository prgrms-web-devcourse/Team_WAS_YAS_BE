package org.prgrms.yas.domain.post.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.dto.PostDetailResponse;
import org.prgrms.yas.domain.post.exception.NotFoundRoutinePostException;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.dto.RoutineListResponse;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class PostService {
	
	private final RoutineRepository routineRepository;
	private final PostRepository postRepository;
	
	public Long savePost(final Long routineId) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		RoutinePost routinePost = RoutinePost.builder()
		                                     .routine(routine)
		                                     .build();
		return postRepository.save(routinePost)
		                     .getId();
	}
	
	public Long deletePost(final Long postId) {
		RoutinePost routinePost = postRepository.findById(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		routinePost.deletePost();
		return routinePost.getId();
	}
	
	@Transactional(readOnly = true)
	public PostDetailResponse findOne(final Long postId) {
		RoutinePost routinePost = postRepository.findById(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		return new PostDetailResponse(routinePost);
	}
	
	@Transactional(readOnly = true)
	public List<RoutineListResponse> findAll(Long id) {
		List<Routine> notPostAll = routineRepository.findRoutinesNotPosted(id);
		return notPostAll.stream()
		                 .map(Routine::toRoutineListResponse)
		                 .collect(toList());
	}
}
