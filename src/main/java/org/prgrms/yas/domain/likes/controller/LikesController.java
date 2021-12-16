package org.prgrms.yas.domain.likes.controller;

import org.prgrms.yas.domain.likes.service.LikesService;
import org.prgrms.yas.global.response.ApiResponse;
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
	
	@PostMapping("/posts/{id}/likes")
	public ResponseEntity<Void> saveLikes(
			@AuthenticationPrincipal JwtAuthentication token,
			@PathVariable Long id
			){
		likesService.savePostLikes(token.getId(),id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/posts/{id}/likes")
	public ResponseEntity<ApiResponse<Long>> deleteLikes(
			@AuthenticationPrincipal JwtAuthentication token,
			@PathVariable Long id
	){
		return ResponseEntity.ok(ApiResponse.of(likesService.deletePostLikes(token.getId(),id)));
	}
}
