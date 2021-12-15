package org.prgrms.yas.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.post.dto.PostDetailResponse;
import org.prgrms.yas.domain.post.service.PostService;
import org.prgrms.yas.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    
    @Operation(summary = "게시글 등록")
    @PostMapping("/routines/{id}/posts")
    public ResponseEntity<ApiResponse<Long>> create(final @PathVariable("id") Long routineId)
    {
        return ResponseEntity.ok(ApiResponse.of(postService.savePost(routineId)));
    }
    
    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<Long>> delete(final @PathVariable("id") Long postId)
    {
        return ResponseEntity.ok(ApiResponse.of(postService.deletePost(postId)));
    }
    
    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailResponse> findOne(final @PathVariable("id") Long postId)
    {
        return ResponseEntity.ok(postService.findOne(postId));
    }
}
