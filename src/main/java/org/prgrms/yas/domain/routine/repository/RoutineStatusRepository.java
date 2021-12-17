package org.prgrms.yas.domain.routine.repository;

import java.util.List;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineStatusRepository extends JpaRepository<RoutineStatus, Long> {
	
	List<RoutineStatus> getByRoutine(Routine routine);
}
