package org.prgrms.yas.domain.routine.repository;

import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.prgrms.yas.domain.routine.domain.RoutineStatusImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineStatusImageRepository extends JpaRepository<RoutineStatusImage, Long> {
	void deleteAllByRoutineStatus(RoutineStatus routineStatus);
}
