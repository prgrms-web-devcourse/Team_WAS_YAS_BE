package org.prgrms.yas.domain.routine.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoutineStatusRepository extends JpaRepository<RoutineStatus, Long> {
	
	List<RoutineStatus> getByRoutine(Routine routine);
	
	//2021-12-21 12:33:25
	
	@Query("SELECT routineStatus from RoutineStatus routineStatus where substring(routineStatus.dateTime,1,7) = :yearMonth AND routineStatus.startTime is Not null ")
	List<RoutineStatus> getByDate(@Param("yearMonth")String yearMonth);
	
}