package org.prgrms.yas.domain.post.repository;

import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<RoutinePost, Long> {

}
