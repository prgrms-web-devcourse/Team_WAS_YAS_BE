package org.prgrms.yas.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.comment.dto.CommentCreateRequest;
import org.prgrms.yas.domain.comment.dto.CommentUpdateRequest;
import org.prgrms.yas.domain.comment.exception.NotFoundCommentException;
import org.prgrms.yas.domain.comment.service.CommentService;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.global.response.ApiResponse;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  //댓글 등록
  @Operation(summary = "댓글 등록 컨트롤러")
  @PostMapping("/post/{id}/comment")
  public ResponseEntity<ApiResponse<Long>> create(
      final @AuthenticationPrincipal JwtAuthentication token, final @PathVariable Long postId,
      final @RequestBody CommentCreateRequest commentCreateRequest
  ) throws NotFoundUserException, NotFoundRoutineException {
    return ResponseEntity.ok(ApiResponse.of(commentService.saveComment(
        token.getId(),
        postId,
        commentCreateRequest
    )));
  }

  //댓글 수정
  @Operation(summary = "댓글 수정 컨트롤러")
  @PatchMapping("/comment/{commentId}/update")
  public ResponseEntity<ApiResponse<Long>> update(
      final @PathVariable Long commentId,
      final @RequestBody CommentUpdateRequest commentUpdateRequest
  ) throws NotFoundCommentException {
    return ResponseEntity.ok(ApiResponse.of(commentService.updateComment(
        commentId,
        commentUpdateRequest
    )));
  }

  //댓글 삭제
  @Operation(summary = "댓글 삭제 컨트롤러")
  @PatchMapping("/comment/{commentId}/delete")
  public ResponseEntity<ApiResponse<Long>> delete(
      final @PathVariable Long commentId
  ) throws NotFoundCommentException {
    return ResponseEntity.ok(ApiResponse.of(commentService.deleteComment(commentId)));
  }

}
