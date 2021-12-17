package org.prgrms.yas.domain.likes.repository;

import java.util.List;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.likes.domain.CommentLikes;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {
	@Modifying
	@Query(value = "insert ignore into comment_likes (user_id, comment_id) "
			+ "values(?1,?2) ", nativeQuery = true)
	void saveCommentLikes(Long userId, Long commentId);
	
	Long deleteByUserAndComment(User user, Comment comment);
	
	List<CommentLikes> findByComment(Long commentId);
}
