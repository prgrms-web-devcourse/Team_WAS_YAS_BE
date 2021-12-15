package org.prgrms.yas.domain.mission.repository;

import java.util.List;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.domain.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionStatusRepository extends JpaRepository<MissionStatus, Long> {
	
	List<MissionStatus> getByMission(Mission mission);
	
}
