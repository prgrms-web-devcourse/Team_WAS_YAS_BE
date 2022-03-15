package org.prgrms.yas.domain.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.yas.domain.user.exception.NotSamePasswordException;
import org.prgrms.yas.domain.user.exception.NotValidPasswordException;

@ExtendWith(MockitoExtension.class)
public class UserDtoTest {

    @Test
    @DisplayName("변경할 비밀번호 객체 일치 테스트")
    void isDifferentPasswordTest() {
        // given
        UserPasswordChangeRequest userPasswordChangeRequest =
                new UserPasswordChangeRequest("test123!!", "test123@@", "test123@@");

        // when
        boolean result = userPasswordChangeRequest.isDifferentPassword();

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("일치하지 않은 비밀번호 예외처리 테스트")
    void isDifferentPasswordReturnNotSamePasswordExceptionTest() {
        // given
        UserPasswordChangeRequest userPasswordChangeRequest =
                new UserPasswordChangeRequest("test123!!", "test123!@", "test123@@");

        // when
        // then
        Assertions.assertThatThrownBy(() -> userPasswordChangeRequest.isDifferentPassword())
                .isInstanceOf(NotSamePasswordException.class);
    }

    @Test
    @DisplayName("회원가입 과정 비밀번호 일치 테스트")
    void isDifferentPasswordSignUpTest() {
        // given
        UserSignUpRequest userSignUpRequest =
                new UserSignUpRequest("email@email.com", "password123!", "password123!", "nickname", "name");

        // when
        boolean result = userSignUpRequest.isDifferentPassword();

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("회원가입 과정 비밀번호 일치 테스트")
    void isDifferentPasswordSignUpNotSamePasswordExceptionTest() {
        // given
        UserSignUpRequest userSignUpRequest =
                new UserSignUpRequest("email@email.com", "password123@", "password123!", "nickname", "name");

        // when
        // then
        Assertions.assertThatThrownBy(() -> userSignUpRequest.isDifferentPassword())
                .isInstanceOf(NotValidPasswordException.class);
    }
}
