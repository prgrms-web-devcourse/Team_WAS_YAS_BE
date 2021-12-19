package org.prgrms.yas.domain.likes.service;

import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.comment.exception.NotFoundCommentException;
import org.prgrms.yas.domain.comment.repository.CommentRepository;
import org.prgrms.yas.domain.likes.repository.CommentLikesRepository;
import org.prgrms.yas.domain.likes.repository.PostLikesRepository;
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
	
	private final CommentLikesRepository commentLikesRepository;
	private final PostLikesRepository postLikesRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	
	public LikesService(
			CommentLikesRepository commentLikesRepository, PostLikesRepository postLikesRepository,
			UserRepository userRepository, CommentRepository commentRepository,
			PostRepository postRepository
	) {
		this.postLikesRepository = postLikesRepository;
		this.commentLikesRepository = commentLikesRepository;
		this.userRepository = userRepository;
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
	}
	
	@Transactional
	public void saveCommentLikes(Long userId, Long commentId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId)
		                          .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
		                                   .orElseThrow(() -> new NotFoundCommentException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		if (!isDuplicateCommentLikes(
				user,
				comment
		)) {
			commentLikesRepository.saveCommentLikes(
					userId,
					commentId
			);
		}
	}
	
	@Transactional
	public Long deleteCommentLikes(Long userId, Long commentId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId)
		                          .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
		                                   .orElseThrow(() -> new NotFoundCommentException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		return commentLikesRepository.deleteByUserAndComment(
				user,
				comment
		);
	}
	
	@Transactional
	public void savePostLikes(Long userId, Long postId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId)
		                          .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		RoutinePost routinePost = postRepository.findByIdAndIsDeletedFalse(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		if (!isDuplicatePostLikes(
				user,
				routinePost
		)) {
			postLikesRepository.savePostLikes(
					user,
					routinePost
			);
		}
	}
	
	@Transactional
	public Long deletePostLikes(Long userId, Long postId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId)
		                          .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		RoutinePost routinePost = postRepository.findByIdAndIsDeletedFalse(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		return postLikesRepository.deleteByUserAndRoutinePost(
				user,
				routinePost
		);
	}
	
	private boolean isDuplicatePostLikes(User user, RoutinePost routinePost) {
		return postLikesRepository.existsByUserAndRoutinePost(
				user,
				routinePost
		);
	}
	
	private boolean isDuplicateCommentLikes(User user, Comment comment) {
		return commentLikesRepository.existsByUserAndComment(
				user,
				comment
		);
	}
}
