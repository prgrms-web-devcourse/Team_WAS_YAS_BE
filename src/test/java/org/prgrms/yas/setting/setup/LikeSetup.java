package org.prgrms.yas.setting.setup;

import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.likes.domain.CommentLikes;
import org.prgrms.yas.domain.likes.domain.PostLikes;
import org.prgrms.yas.domain.likes.repository.CommentLikesRepository;
import org.prgrms.yas.domain.likes.repository.PostLikesRepository;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class LikeSetup {
	@Autowired
	private PostLikesRepository postLikesRepository;
	@Autowired
	private CommentLikesRepository commentLikesRepository;
	
	public PostLikes savePostLikes(User user, RoutinePost routinePost) {
		PostLikes postLikes = PostLikes.builder()
		                               .user(user)
		                               .routinePost(routinePost)
		                               .build();

		return postLikesRepository.save(postLikes);
	}
	
	public CommentLikes saveCommentLikes(User user, Comment comment) {
		CommentLikes commentLikes = CommentLikes.builder()
		                               .user(user)
		                               .comment(comment)
		                               .build();
		
		return commentLikesRepository.save(commentLikes);
	}
	
}