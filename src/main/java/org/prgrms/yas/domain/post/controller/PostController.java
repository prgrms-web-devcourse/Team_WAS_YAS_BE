package org.prgrms.yas.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.post.dto.PostDetailResponse;
import org.prgrms.yas.domain.post.dto.PostListResponse;
import org.prgrms.yas.domain.post.service.PostService;
import org.prgrms.yas.domain.routine.dto.RoutineListResponse;
import org.prgrms.yas.global.response.ApiResponse;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class PostController {
	
	private final PostService postService;
	
	@Operation(summary = "게시글 등록")
	@PostMapping("/routines/{id}/posts")
	public ResponseEntity<ApiResponse<Long>> create(
			final @ApiIgnore @AuthenticationPrincipal JwtAuthentication token,
			final @PathVariable("id") Long routineId
	) {
		return ResponseEntity.ok(ApiResponse.of(postService.savePost(
				token.getId(),
				routineId
		)));
	}
	
	@Operation(summary = "게시글 삭제")
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<ApiResponse<Long>> delete(
			final @ApiIgnore @AuthenticationPrincipal JwtAuthentication token,
			final @PathVariable("id") Long postId
	) {
		return ResponseEntity.ok(ApiResponse.of(postService.deletePost(
				token.getId(),
				postId
		)));
	}
	
	@Operation(summary = "게시글 단건 조회")
	@GetMapping("/posts/{id}")
	public ResponseEntity<ApiResponse<PostDetailResponse>> findOne(
			final @PathVariable("id") Long postId
	) {
		return ResponseEntity.ok(ApiResponse.of(postService.findOne(postId)));
	}
	
	@Operation(summary = "루틴 조회(게시글 등록되지 않은 루틴)")
	@GetMapping("routines/posts")
	public ResponseEntity<ApiResponse<List<RoutineListResponse>>> findAll(
			@AuthenticationPrincipal JwtAuthentication token
	) {
		return ResponseEntity.ok(ApiResponse.of(postService.findAll(token.getId())));
	}
	
	@Operation(summary = "게시글 전체 조회(최신글 순)")
	@GetMapping("/posts")
	public ResponseEntity<ApiResponse<List<PostListResponse>>> findAllPosts(
			@RequestParam Optional<String> category
	) {
		return ResponseEntity.ok(ApiResponse.of(category.map(postService::findAllPostWithCategory)
		                                                .orElse(postService.findAllPost())));
	}
	
	@Operation(summary = "게시글 전체 조회(좋아요 순)")
	@GetMapping("/posts/popular")
	public ResponseEntity<ApiResponse<List<PostListResponse>>> findAllPopularPosts(
			@RequestParam Optional<String> category
	) {
		return ResponseEntity.ok(ApiResponse.of(category.map(postService::findAllLikesWithCategory)
		                                                .orElse(postService.findAllLikes())));
	}
	
	@Operation(summary = "게시글 전체 조회(내가 쓴 게시글)")
	@GetMapping("/posts/myPost")
	public ResponseEntity<ApiResponse<List<PostListResponse>>> findAllMyPost(
			@RequestParam Optional<String> category, @AuthenticationPrincipal JwtAuthentication token
	) {
		return ResponseEntity.ok(ApiResponse.of(category.map(biddingCategory -> postService.findAllMyPostWithCategory(
				                                                token.getId(),
				                                                biddingCategory
		                                                ))
		                                                .orElse(postService.findAllMyPost(token.getId()))));
	}
}
