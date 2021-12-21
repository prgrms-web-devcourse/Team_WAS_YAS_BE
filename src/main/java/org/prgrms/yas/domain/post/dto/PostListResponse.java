package org.prgrms.yas.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
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
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private ZonedDateTime createdAt;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private ZonedDateTime updatedAt;
	private String content;
	private UserDto user;
	private PostRoutineDto routine;
	private List<LikesResponse> likesResponse;
	
	
	@Builder
	public PostListResponse(RoutinePost routinePost, List<LikesResponse> likesResponse) {
		this.postId = routinePost.getId();
		this.createdAt = routinePost.getCreatedAt();
		this.updatedAt = routinePost.getUpdatedAt();
		this.content = routinePost.getContent();
		this.user = new UserDto(routinePost.getRoutine()
		                                   .getUser());
		this.routine = new PostRoutineDto(routinePost.getRoutine());
		this.likesResponse = likesResponse;
	}
	
}
