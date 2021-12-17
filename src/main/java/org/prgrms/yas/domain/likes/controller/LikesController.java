package org.prgrms.yas.domain.likes.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.prgrms.yas.domain.likes.service.LikesService;
import org.prgrms.yas.domain.post.global.response.ApiResponse;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikesController {
	
	private final LikesService likesService;
	
	public LikesController(LikesService likesService) {
		this.likesService = likesService;
	}
	
	@Operation(summary = "댓글 좋아요 생성 컨트롤러")
	@PostMapping("/posts/comments/{id}/likes")
	public ResponseEntity<Void> saveCommentLikes(
			@AuthenticationPrincipal JwtAuthentication token, @PathVariable Long id
	) {
		likesService.saveCommentLikes(token.getId(),
				id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@Operation(summary = "댓글 좋아요 삭제 컨트롤러")
	@DeleteMapping("/posts/comments/{id}/likes")
	public ResponseEntity<ApiResponse<Long>> deleteCommentLikes(
			@AuthenticationPrincipal JwtAuthentication token, @PathVariable Long id
	) {
		return ResponseEntity.ok(ApiResponse.of(likesService.deleteCommentLikes(
				token.getId(),
				id
		)));
	}
	
	@Operation(summary = "게시물 좋아요 생성 컨트롤러")
	@PostMapping("/posts/{id}/likes")
	public ResponseEntity<Void> savePostLikes(
			@AuthenticationPrincipal JwtAuthentication token, @PathVariable Long id
	) {
		likesService.savePostLikes(token.getId(),
				id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@Operation(summary = "게시물 좋아요 삭제 컨트롤러")
	@DeleteMapping("/posts/{id}/likes")
	public ResponseEntity<ApiResponse<Long>> deletePostLikes(
			@AuthenticationPrincipal JwtAuthentication token, @PathVariable Long id
	) {
		return ResponseEntity.ok(ApiResponse.of(likesService.deletePostLikes(token.getId(),
				id)));
	}
}
