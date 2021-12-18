package org.prgrms.yas.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.comment.dto.CommentCreateRequest;
import org.prgrms.yas.domain.comment.dto.CommentUpdateRequest;
import org.prgrms.yas.domain.comment.service.CommentService;
import org.prgrms.yas.global.response.ApiResponse;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService commentService;
	
	@Operation(summary = "댓글 등록 컨트롤러")
	@PostMapping("/posts/{id}/comments")
	public ResponseEntity<ApiResponse<Long>> create(
			final @ApiIgnore @AuthenticationPrincipal JwtAuthentication token, final @PathVariable("id") Long postId,
			final @RequestBody CommentCreateRequest commentCreateRequest
	) {
		return ResponseEntity.ok(ApiResponse.of(commentService.saveComment(
				token.getId(),
				postId,
				commentCreateRequest
		)));
	}
	
	@Operation(summary = "댓글 수정 컨트롤러")
	@PatchMapping("/comments/{id}")
	public ResponseEntity<ApiResponse<Long>> update(
			final @PathVariable("id") Long commentId,
			final @RequestBody CommentUpdateRequest commentUpdateRequest
	) {
		return ResponseEntity.ok(ApiResponse.of(commentService.updateComment(
				commentId,
				commentUpdateRequest
		)));
	}
	
	@Operation(summary = "댓글 삭제 컨트롤러")
	@DeleteMapping("/comments/{id}")
	public ResponseEntity<ApiResponse<Long>> delete(
			final @PathVariable("id") Long commentId
	) {
		return ResponseEntity.ok(ApiResponse.of(commentService.deleteComment(commentId)));
	}
}
