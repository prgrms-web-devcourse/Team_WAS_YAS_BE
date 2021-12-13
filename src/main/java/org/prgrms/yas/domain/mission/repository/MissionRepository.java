package org.prgrms.yas.domain.mission.repository;

import java.util.List;
import java.util.Optional;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
	
	Optional<List<Mission>> findByRoutineId(Long routineId);
}