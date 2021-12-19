package org.prgrms.yas.domain.post.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.likes.dto.LikesResponse;
import org.prgrms.yas.domain.likes.repository.PostLikesRepository;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.dto.PostCreateRequest;
import org.prgrms.yas.domain.post.dto.PostDetailResponse;
import org.prgrms.yas.domain.post.dto.PostListResponse;
import org.prgrms.yas.domain.post.exception.DuplicatePostException;
import org.prgrms.yas.domain.post.exception.NotFoundRoutinePostException;
import org.prgrms.yas.domain.post.exception.WrongUserException;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;
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
	private final PostLikesRepository postLikesRepository;
	
	public Long savePost(
			final Long userId, final Long routineId, PostCreateRequest postCreateRequest
	) {
		Routine routine = routineRepository.findByIdAndIsDeletedFalse(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		RoutinePost routinePost = RoutinePost.builder()
		                                     .routine(routine)
		                                     .content(postCreateRequest.getContent())
		                                     .build();
		
		if (!userValid(
				userId,
				routine.getUser()
				       .getId()
		) && !routineValid(routineId)) {
			postRepository.save(routinePost);
		}
		return routinePost.getId();
	}
	
	public Long deletePost(final Long userId, final Long postId) {
		RoutinePost routinePost = postRepository.findById(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		if (!userValid(
				userId,
				routinePost.getRoutine()
				           .getUser()
				           .getId()
		)) {
			postRepository.deleteById(postId);
		}
		return postId;
	}
	
	public PostDetailResponse findOne(final Long postId) {
		RoutinePost routinePost = postRepository.findById(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		List<LikesResponse> likes = postLikesRepository.getByPost(postId);
		return new PostDetailResponse(
				routinePost,
				likes
		);
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
	
	
	public List<RoutineListResponse> findAll(Long id) {
		List<Routine> notPostAll = routineRepository.findRoutinesNotPosted(id);
		return notPostAll.stream()
		                 .map(Routine::toRoutineListResponse)
		                 .collect(toList());
	}
	
	public List<PostListResponse> findAllPost() {
		List<RoutinePost> routinePosts = postRepository.findByTitle();
		return getPostListResponses(routinePosts);
	}
	
	public List<PostListResponse> findAllPostWithCategory(String category) {
		List<RoutinePost> routinePosts = postRepository.findByTitle();
		return checkCategory(
				category,
				routinePosts
		);
	}
	
	public List<PostListResponse> findAllLikes() {
		List<RoutinePost> routinePosts = postRepository.findAllByPostLikes();
		return getPostListResponses(routinePosts);
	}
	
	public List<PostListResponse> findAllLikesWithCategory(String category) {
		List<RoutinePost> routinePosts = postRepository.findAllByPostLikes();
		return checkCategory(
				category,
				routinePosts
		);
	}
	
	public List<PostListResponse> findAllMyPost(Long userId) {
		List<RoutinePost> routinePosts = postRepository.getByUser(userId);
		return getPostListResponses(routinePosts);
	}
	
	public List<PostListResponse> findAllMyPostWithCategory(Long userId, String category) {
		List<RoutinePost> routinePosts = postRepository.getByUser(userId);
		return checkCategory(
				category,
				routinePosts
		);
	}
	
	private List<PostListResponse> getPostListResponses(List<RoutinePost> routinePosts) {
		List<PostListResponse> postListResponse = new ArrayList<>();
		for (RoutinePost routinePost : routinePosts) {
			List<LikesResponse> postLikes = new ArrayList<>();
			postLikes = postLikesRepository.getByPost(routinePost.getId());
			postListResponse.add(new PostListResponse(
					routinePost,
					postLikes
			));
		}
		return postListResponse;
	}
	
	private List<PostListResponse> checkCategory(String category, List<RoutinePost> routinePosts) {
		Predicate<RoutineCategory> categoryPredicate = enumCategory -> (enumCategory.valueOf(category)
				== enumCategory);
		routinePosts = routinePosts.stream()
		                           .filter(routinePost -> {
			                           long cnt = routinePost.getRoutine()
			                                                 .getRoutineCategory()
			                                                 .stream()
			                                                 .filter(categoryPredicate)
			                                                 .count();
			                           return cnt != 0;
		                           })
		                           .collect(Collectors.toList());
		
		return getPostListResponses(routinePosts);
	}
}
