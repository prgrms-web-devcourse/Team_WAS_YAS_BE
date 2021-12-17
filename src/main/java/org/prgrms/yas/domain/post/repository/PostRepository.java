package org.prgrms.yas.domain.post.repository;

import java.util.Optional;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<RoutinePost, Long> {
	boolean existsByRoutineId(Long routineId);
}
