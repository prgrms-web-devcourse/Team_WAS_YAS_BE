package org.prgrms.yas.domain.routine.repository;

import java.util.Optional;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

}
