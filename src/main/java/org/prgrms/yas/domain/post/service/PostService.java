package org.prgrms.yas.domain.post.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.dto.PostDetailResponse;
import org.prgrms.yas.domain.post.exception.DuplicatePostException;
import org.prgrms.yas.domain.post.exception.NotFoundRoutinePostException;
import org.prgrms.yas.domain.post.exception.WrongUserException;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.dto.RoutineListResponse;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class PostService {
	
	private final RoutineRepository routineRepository;
	private final PostRepository postRepository;
	
	public Long savePost(final Long userId, final Long routineId) {
		Routine routine = routineRepository.findByIdAndIsDeletedFalse(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		RoutinePost routinePost = RoutinePost.builder()
		                                     .routine(routine)
		                                     .build();
		if (!userValid(userId, routine.getUser().getId()) && !routineValid(routineId)) {
			postRepository.save(routinePost);
		}
		return routinePost.getId();
	}
	
	public Long deletePost(final Long userId, final Long postId) {
		RoutinePost routinePost = postRepository.findById(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		if(!userValid(userId, routinePost.getRoutine().getUser().getId())){
			postRepository.deleteById(postId);
		}
		return routinePost.getId();
	}
	
	public PostDetailResponse findOne(final Long postId) {
		RoutinePost routinePost = postRepository.findById(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		return new PostDetailResponse(routinePost);
	}
	
	public boolean userValid(final Long userId, final Long compareUserId) {
		if (!userId.equals(compareUserId)) {
			throw new WrongUserException(ErrorCode.INVALID_INPUT_ERROR);
		}
		return false;
	}
	
	public boolean routineValid(final Long routineId) {
		if (postRepository.existsByRoutineId(routineId)) {
			throw new DuplicatePostException(ErrorCode.CONFLICT_VALUE_ERROR);
		}
		return false;
	}
	
}