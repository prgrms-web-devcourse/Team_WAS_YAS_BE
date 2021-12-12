package org.prgrms.yas.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.comment.dto.CommentCreateRequest;
import org.prgrms.yas.domain.comment.service.CommentService;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.global.response.ApiResponse;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/posts")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;

  //댓글 등록
  @PostMapping("/{id}/comments")
  public ResponseEntity<ApiResponse<Long>> create(
      final @AuthenticationPrincipal JwtAuthentication token,
      final @PathVariable("id") Long postId,
      final @RequestBody CommentCreateRequest createRequest)
      throws NotFoundUserException, NotFoundRoutineException {
      return ResponseEntity.ok(ApiResponse.of(commentService.saveComment(token.getId(), postId, createRequest)));
  }
}
