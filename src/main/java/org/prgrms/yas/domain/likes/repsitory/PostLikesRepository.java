package org.prgrms.yas.domain.likes.repsitory;

import org.prgrms.yas.domain.likes.domain.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
	
	@Modifying
	@Query(value = "insert ignore into post_likes (user_id, routine_post_id) "
			+ "values(?1,?2) ", nativeQuery = true)
	void savePostLikes(Long userId, Long RoutinePostId);
}
