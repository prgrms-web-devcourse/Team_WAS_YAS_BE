package org.prgrms.yas.domain.mission.repository;

import org.prgrms.yas.domain.mission.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {

}