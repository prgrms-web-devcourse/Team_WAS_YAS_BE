package org.prgrms.yas.global.scheduler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.comment.repository.CommentRepository;
import org.prgrms.yas.domain.likes.repository.CommentLikesRepository;
import org.prgrms.yas.domain.likes.repository.PostLikesRepository;
import org.prgrms.yas.domain.mission.repository.MissionRepository;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class ScheduledService {
	
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final CommentLikesRepository commentLikesRepository;
	private final PostLikesRepository postLikesRepository;
	private final PostRepository postRepository;
	private final RoutineRepository routineRepository;
	private final MissionRepository missionRepository;
	
	@Scheduled(cron = "0 0 0 1 * *")
	public void clearSoftDeleteData() {
		List<User> allByIsDeletedTrue = userRepository.findAllByIsDeletedTrue();
		
		for(User user : allByIsDeletedTrue){
			commentLikesRepository.deleteAllByUser(user);
			commentRepository.deleteAllByUser(user);
			postLikesRepository.deleteAllByUser(user);
			
			List<Routine> allByUser = routineRepository.findAllByUser(user);
			for(Routine routine : allByUser){
				postRepository.deleteAllByRoutine(routine);
				missionRepository.deleteAllByRoutine(routine);
			}
			routineRepository.deleteAllByUser(user);
		}
	}
}
