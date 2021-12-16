package org.prgrms.yas.domain.likes.service;

import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.comment.exception.NotFoundCommentException;
import org.prgrms.yas.domain.comment.repository.CommentRepository;
import org.prgrms.yas.domain.likes.repository.CommentLikesRepository;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikesService {
	
	private final CommentLikesRepository commentLikesRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	
	public LikesService(
			CommentLikesRepository commentLikesRepository, UserRepository userRepository,
			CommentRepository commentRepository
	) {
		this.commentLikesRepository = commentLikesRepository;
		this.userRepository = userRepository;
		this.commentRepository = commentRepository;
	}
	
	@Transactional
	public void saveCommentLikes(Long userId, Long commentId) {
		commentLikesRepository.saveCommentLikes(userId,
				commentId);
	}
	
	@Transactional
	public Long deleteCommentLikes(Long userId, Long commentId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId)
		                          .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
		                                   .orElseThrow(() -> new NotFoundCommentException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		return commentLikesRepository.deleteByUserAndComment(user,comment);
	}
}
