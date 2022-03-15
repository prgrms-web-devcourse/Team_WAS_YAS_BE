package org.prgrms.yas.domain.user.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserPasswordChangeRequest;
import org.prgrms.yas.domain.user.dto.UserPasswordRequest;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.prgrms.yas.domain.user.dto.UserUpdateRequest;
import org.prgrms.yas.domain.user.exception.DuplicateUserException;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.aws.S3Uploader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private S3Uploader s3Uploader;

    @BeforeEach
    void setUp() {
        userService = new UserService(s3Uploader, userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("회원 로그인 테스트")
    void signInTest() {
        // given
        User user = createUser();
        given(userRepository.findByEmailAndIsDeletedFalse(any())).willReturn(Optional.of(user));

        // when
        userService.signIn(user.getEmail(), user.getPassword());

        // then

        verify(userRepository).findByEmailAndIsDeletedFalse(user.getEmail());
        verify(user).checkPassword(passwordEncoder, user.getPassword());
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() {
//		// given
        User user = createUser();
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("email@email.com",
                "test1231!",
                "test1231!",
                "testNickname",
                "testName"
        );
        given(userRepository.save(any())).willReturn(user);

        // when
        Long aLong = userService.signUp(userSignUpRequest);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(aLong).isNotNull();
        assertThat(capturedUser.getEmail()).isEqualTo(userSignUpRequest.getEmail());
        assertThat(capturedUser.getName()).isEqualTo(userSignUpRequest.getName());
    }

    @Test
    @DisplayName("회원가입과정에서 중복 회원 예외 처리 테스트")
    void signUpDuplicateUserExceptionTest() {
        // given
        User user = createUser();
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("email@email.com",
                "test1231!",
                "test1231!",
                "testNickname",
                "testName"
        );
        given(userRepository.existsByEmailAndIsDeletedFalse(any())).willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> userService.signUp(userSignUpRequest))
                .isInstanceOf(DuplicateUserException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("삭제되지 않은 회원 조회 테스트")
    void findUserTest() {
        User user = createUser();
        given(userRepository.findByIdAndIsDeletedFalse(anyLong())).willReturn(Optional.of(user));
        userService.findActiveUser(anyLong());

        verify(userRepository).findByIdAndIsDeletedFalse(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 예외처리 테스트")
    void findUserExceptionTest() {
        // given
        given(userRepository.findByIdAndIsDeletedFalse(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> userService.findUser(anyLong()))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    @DisplayName("회원 프로필 사진 변경 테스트")
    void updateTest() throws IOException {
        // given
        User user = createUser();

        UserUpdateRequest userUpdateRequest = Mockito.mock(UserUpdateRequest.class);
        given(userUpdateRequest.getNickname()).willReturn("nickname");
        given(userUpdateRequest.getProfileImage()).willReturn("profileImage");

        MockMultipartFile mockMultipartFile = new MockMultipartFile("content", "C://", "multipart/mixed", "content".getBytes());
        given(userRepository.findByIdAndIsDeletedFalse(anyLong())).willReturn(Optional.of(user));

        // when
        userService.update(user.getId(), userUpdateRequest, mockMultipartFile);
        // then
        verify(userUpdateRequest, times(2)).setProfileImage(user.getProfileImage());
        verify(user).updateUserInfo(userUpdateRequest);
    }

    @Test
    @DisplayName("패스워드 변경 테스트")
    void updatePasswordTest() {
        // given
        User user = createUser();
        UserPasswordChangeRequest userPasswordChangeRequest = Mockito.mock(UserPasswordChangeRequest.class);

        given(userRepository.findByIdAndIsDeletedFalse(anyLong())).willReturn(Optional.of(user));
        given(userPasswordChangeRequest.isDifferentPassword()).willReturn(false);
        // when
        userService.updatePassword(user.getId(), userPasswordChangeRequest);

        // then
        verify(user).updateUserPasswordInfo(passwordEncoder, userPasswordChangeRequest);
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void deleteTest() {
        // given
        User user = createUser();
        UserPasswordRequest userPasswordRequest = new UserPasswordRequest("test123!!");
        given(userRepository.findByIdAndIsDeletedFalse(anyLong())).willReturn(Optional.of(user));

        // when
        Long id = userService.delete(user.getId(), userPasswordRequest);

        // then
        verify(user).updateUserDeleted();
        Assertions.assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("유효한 이메일 확인 테스트")
    void isValidEmailReturnTrueTest() {
        // given
        String mockEmail = "email@email.com";
        given(userRepository.existsByEmailAndIsDeletedFalse(mockEmail)).willReturn(false);

        // when
        boolean result = userService.isValidEmail(mockEmail);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유효한 이메일 확인 테스트")
    void isValidEmailReturnFalseTest() {
        // given
        String mockEmail = "email@email.com";
        given(userRepository.existsByEmailAndIsDeletedFalse(mockEmail)).willReturn(true);

        // when
        boolean result = userService.isValidEmail(mockEmail);

        // then
        Assertions.assertThat(result).isFalse();
    }

    private static User createUser() {
        User user = Mockito.mock(User.class);

        given(user.getId()).willReturn(1L);
        given(user.getPassword()).willReturn("test123!!");
        given(user.getNickname()).willReturn("testNickname");
        given(user.getEmail()).willReturn("test@test.com");
        given(user.getName()).willReturn("testName");

        return user;
    }
}
