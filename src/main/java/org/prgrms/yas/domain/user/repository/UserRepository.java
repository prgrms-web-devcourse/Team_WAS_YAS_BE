package org.prgrms.yas.domain.user.repository;

import java.util.Optional;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByProviderAndProviderId(String provider, String providerId);
	
	boolean existsByEmail(String email);
	
	Optional<User> findByIdAndIsDeletedFalse(Long id);
}
