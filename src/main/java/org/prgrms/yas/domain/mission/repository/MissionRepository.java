package org.prgrms.yas.domain.mission.repository;

import java.util.List;
import java.util.Optional;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
	
	Optional<List<Mission>> findByRoutineIdAndIsDeletedFalse(Long routineId);
	
	void deleteByIdAndIsDeletedFalse(Long missionId);
	
	Mission getByIdAndIsDeletedFalse(Long missionId);
	
	Optional<Mission> findByIdAndIsDeletedFalse(Long missionId);
	
	List<Mission> getByRoutineAndIsDeletedFalse(Routine routine);
	
	Optional<List<Mission>> findByRoutineId(Long routineId);
	
	void deleteAllByRoutine(Routine routine);
}
