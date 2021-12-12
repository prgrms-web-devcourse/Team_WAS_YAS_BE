package org.prgrms.yas.domain.comment.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.comment.dto.CommentCreateRequest;
import org.prgrms.yas.domain.comment.repository.CommentRepository;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  public Long saveComment(final Long userId, final Long postId, final CommentCreateRequest createRequest)
      throws NotFoundUserException, NotFoundRoutineException {
    User user = userRepository.findById(userId)
                              .orElseThrow(()->new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
    RoutinePost routinePost = postRepository.findById(postId)
                                            .orElseThrow(()->new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
    return commentRepository.save(createRequest.toEntity(user, routinePost)).getId();
  }
}
