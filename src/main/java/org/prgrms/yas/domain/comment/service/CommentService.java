package org.prgrms.yas.domain.comment.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.comment.dto.CommentCreateRequest;
import org.prgrms.yas.domain.comment.dto.CommentUpdateRequest;
import org.prgrms.yas.domain.comment.exception.NotFoundCommentException;
import org.prgrms.yas.domain.comment.repository.CommentRepository;
import org.prgrms.yas.domain.likes.repository.CommentLikesRepository;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {
	
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final CommentLikesRepository commentLikesRepository;
	
	public Long saveComment(
			final Long userId, final Long postId, final CommentCreateRequest commentCreateRequest
	) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		RoutinePost routinePost = postRepository.findById(postId)
		                                        .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		return commentRepository.save(commentCreateRequest.toEntity(
				                        user,
				                        routinePost
		                        ))
		                        .getId();
	}
	
	public Long updateComment(
			final Long commentId, final CommentUpdateRequest commentUpdateRequest
	) {
		
		Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
		                                   .orElseThrow(() -> new NotFoundCommentException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		comment.updateComment(commentUpdateRequest);
		return comment.getId();
	}
	
	public Long deleteComment(final Long commentId) {
		Comment comment = commentRepository.findById(commentId)
		                                   .orElseThrow(() -> new NotFoundCommentException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		comment.deleteComment();
		return comment.getId();
	}
}
