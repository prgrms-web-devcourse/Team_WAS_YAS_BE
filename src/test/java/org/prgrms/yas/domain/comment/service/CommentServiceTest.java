package org.prgrms.yas.domain.comment.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.comment.dto.CommentCreateRequest;
import org.prgrms.yas.domain.comment.dto.CommentUpdateRequest;
import org.prgrms.yas.domain.comment.exception.NotFoundCommentException;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;
import org.prgrms.yas.domain.routine.domain.Week;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(value = false)
class CommentServiceTest {

  @Autowired
  private CommentService commentService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoutineRepository routineRepository;
  @Autowired
  private PostRepository postRepository;

  private Long userId;
  private Long postId;
  private Long commentId;
  private CommentCreateRequest commentCreateRequest;
  private CommentUpdateRequest commentUpdateRequest;

  @BeforeEach
  void setting() {
    //User save
    User user = User.builder()
                    .name("byunminji")
                    .nickname("min")
                    .email("bmj3601@daum.net")
                    .password("1234")
                    .build();

    userId = userRepository.save(user).getId();

    //Routine save
    List<Week> findWeek = new ArrayList<>();
    findWeek.add(Week.valueOf("MON"));
    findWeek.add(Week.valueOf("TUE"));

    List<RoutineCategory> findCategory = new ArrayList<>();
    findCategory.add(RoutineCategory.valueOf("EXERCISE"));

    Routine routine = Routine.builder()
                             .user(user)
                             .name("윤동하기")
                             .startGoalTime(LocalDateTime.now())
                             .durationGoalTime(12L)
                             .weeks(findWeek)
                             .routineCategory(findCategory)
                             .color("black")
                             .emoji(">_<")
                             .build();
    routineRepository.save(routine);

    //RoutinePost save
    RoutinePost routinePost = RoutinePost.builder()
                                         .routine(routine)
                                         .build();
    postId = postRepository.save(routinePost).getId();

    String content = "루틴 퍼갑니당~!";
    commentCreateRequest = CommentCreateRequest.builder()
                                        .content(content)
                                        .build();

    String updateContent = "루틴 퍼갑니당~! 감사합니다ㅎㅎ";
    commentUpdateRequest = CommentUpdateRequest.builder()
                                               .content(updateContent)
                                               .build();
  }

  @Test
  void commentTest() throws NotFoundUserException, NotFoundRoutineException, NotFoundCommentException {
    commentId = commentService.saveComment(userId, postId, commentCreateRequest);

    Long updatedCommentId = commentService.updateComment(commentId, commentUpdateRequest);
    Assertions.assertThat(updatedCommentId)
              .isEqualTo(commentId);

    Long deletedCommentId = commentService.deleteComment(commentId);
    Assertions.assertThat(deletedCommentId)
              .isEqualTo(commentId);
  }
}