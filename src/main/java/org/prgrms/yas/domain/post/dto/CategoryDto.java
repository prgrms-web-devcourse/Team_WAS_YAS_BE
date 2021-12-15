package org.prgrms.yas.domain.post.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;
import org.prgrms.yas.domain.user.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDto {
	public static String categoryToString(RoutineCategory routineCategory){
		return routineCategory.getRoutineCategory();
	}
}
