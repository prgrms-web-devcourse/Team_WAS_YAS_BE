package org.prgrms.yas.domain.comment.repository;

import java.util.List;
import java.util.Optional;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	Optional<Comment> findByIdAndIsDeletedFalse(Long id);
	
	List<Comment> findAllByRoutinePost(RoutinePost routinePost);
	
	void deleteAllByIsDeletedTrue();
 
	void deleteAllByUser(User user);
}
