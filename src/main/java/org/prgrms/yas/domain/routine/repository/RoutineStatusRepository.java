package org.prgrms.yas.domain.routine.repository;

import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineStatusRepository extends JpaRepository<RoutineStatus, Long> {
}
