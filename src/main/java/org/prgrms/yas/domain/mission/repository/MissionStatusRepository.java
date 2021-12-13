package org.prgrms.yas.domain.mission.repository;

import org.prgrms.yas.domain.mission.domain.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionStatusRepository extends JpaRepository<MissionStatus, Long> {

}
