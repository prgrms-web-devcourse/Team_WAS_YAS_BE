package org.prgrms.yas.domain.routine.repository;

import java.util.List;
import java.util.Optional;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
	
	List<Routine> getByUser(User user);
	
	Optional<Routine> findByIdAndIsDeletedFalse(Long id);

	@Query(
			value =
					"select * from routine r "
			+"left outer join routine_post p "
			+"on r.id = p.routine_id "
			+"where p.routine_id is null and r.is_deleted = false and r.user_id = ?1",
			nativeQuery = true)
	List<Routine> findRoutinesNotPosted(Long id);

}
