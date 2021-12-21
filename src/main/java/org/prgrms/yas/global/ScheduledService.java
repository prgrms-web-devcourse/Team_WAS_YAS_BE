package org.prgrms.yas.global;

import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.comment.repository.CommentRepository;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class ScheduledService {
	
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	
	@Scheduled(cron = "0 0 0 L * ?")
	public void clearSoftDeleteData() {
		userRepository.deleteAllByIsDeletedFalse();
		commentRepository.deleteAllByIsDeletedFalse();
	}
}
