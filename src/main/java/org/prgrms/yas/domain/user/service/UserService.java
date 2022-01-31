package org.prgrms.yas.domain.user.service;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserPasswordChangeRequest;
import org.prgrms.yas.domain.user.dto.UserPasswordRequest;
import org.prgrms.yas.domain.user.dto.UserResponse;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.prgrms.yas.domain.user.dto.UserUpdateRequest;
import org.prgrms.yas.domain.user.exception.DuplicateUserException;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.aws.S3Uploader;
import org.prgrms.yas.global.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String DIRECTORY = "static";
	
	private final S3Uploader s3Uploader;
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public UserService(
			S3Uploader s3Uploader, UserRepository userRepository, PasswordEncoder passwordEncoder
	) {
		this.s3Uploader = s3Uploader;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional(readOnly = true)
	public User signIn(String username, String credentials) {
		User user = userRepository.findByEmailAndIsDeletedFalse(username)
		                          .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		user.checkPassword(
				passwordEncoder,
				credentials
		);
		
		return user;
	}
	
	@Transactional
	public Long signUp(UserSignUpRequest userSignUpRequest) {
		if ((!isDuplicateUser(userSignUpRequest)) && !userSignUpRequest.isDifferentPassword()) {
			userSignUpRequest.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));
		}
		
		return userRepository.save(userSignUpRequest.toEntity())
		                     .getId();
	}
	
	@Transactional
	public User signUp(OAuth2User oAuth2User, String provider) {
		String providerId = oAuth2User.getName();
		return userRepository.findByProviderAndProviderId(
				                     provider,
				                     providerId
		                     )
		                     .map(user -> {
			                     logger.warn(
					                     "Already exists: {} for provider: {}, providerId: {}",
					                     user,
					                     provider,
					                     providerId
			                     );
			                     return user;
		                     })
		                     .orElseGet(() -> {
			                     Map<String, Object> attributes = oAuth2User.getAttributes();
			                     Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
			                     Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
			                     Map<String, Object> kakaoAccountProfile = (Map<String, Object>) kakaoAccount.get("profile");
													 
			                     String nickname = (String) properties.get("nickname");
													 String profileImage = (String) kakaoAccountProfile.get("profile_image_url");
													 
													 User user = User.builder()
													                 .name(nickname)
													                 .nickname(nickname)
													                 .provider(provider)
													                 .providerId(providerId)
													                 .profileImage(profileImage)
													                 .build();
			                     userRepository.save(user);
			                     return user;
		                     });
	}
	
	@Transactional(readOnly = true)
	public UserResponse findUser(Long id) {
		return findActiveUser(id).toResponse();
	}
	
	@Transactional(readOnly = true)
	public Optional<User> findUserByProviderAndProviderId(String provider, String providerId) {
		return userRepository.findByProviderAndProviderId(
				provider,
				providerId
		);
	}
	
	@Transactional
	public Long update(Long id, UserUpdateRequest userUpdateRequest, MultipartFile file)
			throws IOException {
		
		User user = findActiveUser(id);
		userUpdateRequest.setProfileImage(user.getProfileImage());
		if (!file.isEmpty()) {
			userUpdateRequest.setProfileImage(s3Uploader.upload(
					file,
					DIRECTORY
			));
		}
		user.updateUserInfo(userUpdateRequest);
		
		return user.getId();
	}
	
	@Transactional
	public Long updatePassword(Long id, UserPasswordChangeRequest userPasswordChangeRequest){
		User user = findActiveUser(id);
		user.checkPassword(passwordEncoder,
				userPasswordChangeRequest.getNowPassword());
		if(!userPasswordChangeRequest.isDifferentPassword()){
			user.updateUserPasswordInfo(passwordEncoder,
					userPasswordChangeRequest
			);
		}
		return user.getId();
	}
	
	@Transactional
	public Long delete(Long id, UserPasswordRequest userPasswordRequest) {
		User user = findActiveUser(id);
		user.checkPassword(passwordEncoder,userPasswordRequest.getPassword());
		userRepository.deleteById(user.getId());
		return id;
	}
	
	@Transactional(readOnly = true)
	public User findActiveUser(Long id) {
		return userRepository.findByIdAndIsDeletedFalse(id)
		                     .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
  }
	
	@Transactional(readOnly = true)
	public boolean isValidEmail(String email){
		checkArgument(isNotEmpty(email),"이메일은 작성해야 합니다.");
		return !userRepository.existsByEmailAndIsDeletedFalse(email);
	}
	
	private boolean isDuplicateUser(UserSignUpRequest userSignUpRequest) {
		if (userRepository.existsByEmailAndIsDeletedFalse(userSignUpRequest.getEmail())) {
			throw new DuplicateUserException(ErrorCode.CONFLICT_VALUE_ERROR);
		}
		return false;
	}
}
