package org.prgrms.yas.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.prgrms.yas.domain.likes.dto.LikesResponse;
import org.prgrms.yas.domain.post.domain.RoutinePost;

@Getter
public class PostDetailResponse {
	
	private final Long postId;
	private final String createdAt;
	private final String updatedAt;
	private final String content;
	private final UserDto user;
	private final RoutineDto routine;
	private final List<CommentDto> comments;
	private List<LikesResponse> likes;
	
	@Builder
	public PostDetailResponse(RoutinePost routinePost, List<LikesResponse> likes) {
		this.postId = routinePost.getId();
		this.createdAt = routinePost.getCreatedAt().plusHours(9)
		                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
		this.updatedAt = routinePost.getUpdatedAt().plusHours(9)
		                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
		this.content = routinePost.getContent();
		this.user = new UserDto(routinePost.getRoutine()
		                                   .getUser());
		this.routine = new RoutineDto(routinePost.getRoutine());
		this.comments = routinePost.getComments()
		                           .stream()
		                           .filter(c -> !c.isDeleted())
		                           .map(CommentDto::new)
		                           .collect(Collectors.toList());
		this.likes = likes;
	}
}
