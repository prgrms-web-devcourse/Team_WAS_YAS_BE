package org.prgrms.yas.domain.user.repository;

import java.util.List;
import java.util.Optional;
import org.prgrms.yas.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByProviderAndProviderId(String provider, String providerId);
	
	Optional<User> findByIdAndIsDeletedFalse(Long id);
	
	void deleteAllByIsDeletedTrue();
	
	boolean existsByEmailAndIsDeletedFalse(String email);
	
	Optional<User> findByEmailAndIsDeletedFalse(String email);
	
	List<User> findAllByIsDeletedTrue();
}
