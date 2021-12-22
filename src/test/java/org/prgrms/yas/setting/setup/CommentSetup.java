package org.prgrms.yas.setting.setup;

import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.comment.dto.CommentCreateRequest;
import org.prgrms.yas.domain.comment.dto.CommentUpdateRequest;
import org.prgrms.yas.domain.comment.repository.CommentRepository;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentSetup {
	
	@Autowired
	private CommentRepository commentRepository;
	
	public Comment saveComment(User user, RoutinePost routinePost) {
		Comment comment = Comment.builder()
		                         .content(buildCommentCreateRequest().getContent())
		                         .user(user)
		                         .routinePost(routinePost)
		                         .build();
		
		return commentRepository.save(comment);
	}
	
	public CommentCreateRequest buildCommentCreateRequest() {
		return CommentCreateRequest.builder()
		                           .content("comment")
		                           .build();
	}
	
	public CommentUpdateRequest buildCommentUpdateRequest() {
		return CommentUpdateRequest.builder()
		                           .content("comment_update")
		                           .build();
	}
}