package org.prgrms.yas.domain.post.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import net.bytebuddy.TypeCache.Sort;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<RoutinePost, Long> {
	
	Optional<RoutinePost> findByIdAndIsDeletedFalse(Long id);
	
	@Query("SELECT b FROM RoutinePost b ORDER BY b.createdAt DESC")
	List<RoutinePost> findByTitle();
	
	@Query(value = "SELECT b FROM RoutinePost b left join fetch b.postLikes ORDER BY b.postLikes.size DESC")
	List<RoutinePost> findAllByPostLikes();
	
	@Query(value = "SELECT b FROM RoutinePost b join fetch b.routine a where a.user.id = :userId")
	List<RoutinePost> getByUser(@Param(value = "userId") Long userId);
	
}
