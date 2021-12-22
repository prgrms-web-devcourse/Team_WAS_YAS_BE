package org.prgrms.yas.setting.setup;

import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.likes.domain.CommentLikes;
import org.prgrms.yas.domain.likes.domain.PostLikes;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.dto.PostCreateRequest;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostSetup {
	@Autowired
	private UserSetup userSetup;
	@Autowired
	private RoutineSetup routineSetup;
	@Autowired
	private MissionSetup missionSetup;
	@Autowired
	private CommentSetup commentSetup;
	@Autowired
	private LikeSetup likeSetup;
	@Autowired
	private PostRepository postRepository;

	public RoutinePost savePost(Routine routine) {
		RoutinePost routinePost = RoutinePost.builder()
		                                     .routine(routine)
		                                     .content(buildPostCreateRequest().getContent())
		                                     .build();

		return postRepository.save(routinePost);
	}
	
	public RoutinePost savePostDetail() {
		User user_post = userSetup.saveUser("test@test.com","$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCA", "name", "nickname");
		User user_comment = userSetup.saveUser("test1@test.com","$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCB", "name1", "nickname1");

		Routine routine = routineSetup.saveRoutine(user_post);
		Mission mission = missionSetup.saveMission(routine);
		RoutinePost routinePost = savePost(routine);
		Comment comment = commentSetup.saveComment(user_comment, routinePost);
		PostLikes postLikes = likeSetup.savePostLikes(user_comment, routinePost);
		CommentLikes commentLikes = likeSetup.saveCommentLikes(user_post, comment);
		
		return routinePost;
	}
	
	public PostCreateRequest buildPostCreateRequest() {
		return new PostCreateRequest("post");
	}
}
