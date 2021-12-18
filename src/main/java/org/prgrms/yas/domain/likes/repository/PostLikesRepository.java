package org.prgrms.yas.domain.likes.repository;

import java.util.List;
import org.prgrms.yas.domain.likes.domain.PostLikes;
import org.prgrms.yas.domain.likes.dto.LikesResponse;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
	
	@Modifying
	@Query(value = "insert ignore into post_likes (user_id, routine_post_id) "
			+ "values(?1,?2) ", nativeQuery = true)
	void savePostLikes(Long userId, Long routinePostId);
	
	Long deleteByUserAndRoutinePost(User user, RoutinePost routinePost);
	
	@Query("SELECT postLikes from PostLikes postLikes where postLikes.routinePost.id = :postId")
	List<LikesResponse> getByPost(@Param(value = "postId") Long postId);

}
