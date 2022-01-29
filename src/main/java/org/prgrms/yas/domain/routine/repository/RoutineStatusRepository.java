package org.prgrms.yas.domain.routine.repository;

import java.util.List;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoutineStatusRepository extends JpaRepository<RoutineStatus, Long> {
	
	List<RoutineStatus> getByRoutine(Routine routine);
	
	@Query("SELECT routineStatus from RoutineStatus routineStatus where substring(routineStatus.dateTime,1,7) = :date AND routineStatus.startTime is Not null ")
	List<RoutineStatus> getByDays(@Param("date") String date);
	
	@Query("SELECT routineStatus from RoutineStatus routineStatus where substring(routineStatus.dateTime,1,10) = :date AND routineStatus.startTime is Not null ")
	List<RoutineStatus> getByDate(@Param("date") String date);
}