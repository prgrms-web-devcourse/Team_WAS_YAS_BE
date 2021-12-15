package org.prgrms.yas.domain.post.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.comment.exception.NotFoundCommentException;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.dto.PostDetailResponse;
import org.prgrms.yas.domain.post.exception.NotFoundRoutinePostException;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class PostService {
  private final RoutineRepository routineRepository;
  private final PostRepository postRepository;

  public Long savePost(final Long routineId) {
    Routine routine = routineRepository.findById(routineId)
                                       .orElseThrow(()->new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
    RoutinePost routinePost = RoutinePost.builder()
                                  .routine(routine)
                                  .build();
    return postRepository.save(routinePost).getId();
  }

  public Long deletePost(final Long postId) {
    RoutinePost routinePost = postRepository.findById(postId)
                                            .orElseThrow(()->new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
    routinePost.deletePost();
    return routinePost.getId();
  }

  public PostDetailResponse findOne(final Long postId) {
    RoutinePost routinePost = postRepository.findById(postId)
                                            .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
    return new PostDetailResponse(routinePost);
  }
}
