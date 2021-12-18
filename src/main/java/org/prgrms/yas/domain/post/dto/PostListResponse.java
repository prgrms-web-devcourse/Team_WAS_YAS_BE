package org.prgrms.yas.domain.post.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.likes.dto.LikesResponse;
import org.prgrms.yas.domain.post.domain.RoutinePost;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListResponse {
	
	private Long postId;
	private String createdAt;
	private String updatedAt;
	private String content;
	private UserDto user;
	private PostRoutineDto routine;
	private List<LikesResponse> likesResponse;
	
	
	@Builder
	public PostListResponse(RoutinePost routinePost, List<LikesResponse> likesResponse) {
		this.postId = routinePost.getId();
		this.createdAt = routinePost.getCreatedAt()
		                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		this.updatedAt = routinePost.getUpdatedAt()
		                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		this.content = routinePost.getContent();
		this.user = new UserDto(routinePost.getRoutine()
		                                   .getUser());
		this.routine = new PostRoutineDto(routinePost.getRoutine());
		this.likesResponse = likesResponse;
	}
	
}
