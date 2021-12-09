package org.prgrms.yas.domain.routine.repository;

import java.util.List;
import java.util.Optional;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
  List<Routine> getByUser(User user);
}
