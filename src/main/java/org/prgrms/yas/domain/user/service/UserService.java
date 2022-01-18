package org.prgrms.yas.domain.user.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserPasswordRequest;
import org.prgrms.yas.domain.user.dto.UserEmailRequest;
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
		User user = userRepository.findByEmail(username)
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
													 Map<String,Object> accounts = (Map<String, Object>) attributes.get("kakao_account");
													 
													 User user = User.builder()
													                 .name((String) properties.get("nickname"))
													                 .provider(provider)
													                 .providerId(providerId)
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
	public Long updatePassword(Long id, UserPasswordRequest userPasswordRequest){
		User user = findActiveUser(id);
		user.checkPassword(passwordEncoder,userPasswordRequest.getNowPassword());
		if(!userPasswordRequest.isDifferentPassword()){
			user.updateUserPasswordInfo(passwordEncoder, userPasswordRequest);
		}
		return user.getId();
	}
	
	@Transactional
	public Long delete(Long id) {
		userRepository.deleteById(findActiveUser(id).getId());
		return id;
	}
	
	@Transactional(readOnly = true)
	public User findActiveUser(Long id) {
		return userRepository.findById(id)
		                     .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
  }
  
  @Transactional(readOnly = true)
	public boolean isValidEmail(UserEmailRequest userEmailRequest){
		return !userRepository.existsByEmail(userEmailRequest.getEmail());
	}
	
  @Transactional(readOnly = true)
	private boolean isDuplicateUser(UserSignUpRequest userSignUpRequest) {
		if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
			throw new DuplicateUserException(ErrorCode.CONFLICT_VALUE_ERROR);
		}
		return false;
	}
}
