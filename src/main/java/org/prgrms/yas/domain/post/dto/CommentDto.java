package org.prgrms.yas.domain.post.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.likes.dto.LikesDto;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {
	
	private Long commentId;
	private UserDto user;
	private String createdAt;
	private String updatedAt;
	private String content;
	private List<LikesDto> likes;
	
	@Builder
	public CommentDto(Comment comment) {
		this.commentId = comment.getId();
		this.user = new UserDto(comment.getUser());
		this.createdAt = comment.getCreatedAt()
		                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		this.updatedAt = comment.getCreatedAt()
		                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		this.content = comment.getContent();
		this.likes = comment.getCommentLikes()
		                    .stream()
		                    .map(LikesDto::new)
		                    .collect(Collectors.toList());
	}
}
