package org.prgrms.yas.domain.likes.service;

import org.prgrms.yas.domain.likes.domain.PostLikes;
import org.prgrms.yas.domain.likes.repsitory.PostLikesRepository;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.exception.NotFoundRoutinePostException;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikesService {
	
	private final PostLikesRepository postLikesRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	
	public LikesService(
			PostLikesRepository postLikesRepository, UserRepository userRepository,
			PostRepository postRepository
	) {
		this.postLikesRepository = postLikesRepository;
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}
	
	@Transactional
	public void savePostLikes(Long userId, Long postId) {
		postLikesRepository.savePostLikes(
				userId,
				postId
		);
	}
	
	@Transactional
	public Long deletePostLikes(Long userId, Long postId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId)
		                          .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		RoutinePost routinePost = postRepository.findByIdAndIsDeletedFalse(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		return postLikesRepository.deleteByUserAndRoutinePost(user,
				routinePost);
	}
}
