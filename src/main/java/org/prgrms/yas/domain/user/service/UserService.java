package org.prgrms.yas.domain.user.service;

import java.io.IOException;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserResponse;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.prgrms.yas.domain.user.dto.UserUpdateRequest;
import org.prgrms.yas.domain.user.exception.DuplicateUserException;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.domain.post.global.aws.S3Uploader;
import org.prgrms.yas.domain.post.global.error.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
	
	private static final String DIRECTORY = "static";
	
	private final S3Uploader s3Uploader;
	
	private final PasswordEncoder passwordEncoder;
	
	private final UserRepository userRepository;
	
	public UserService(
			S3Uploader s3Uploader, PasswordEncoder passwordEncoder, UserRepository userRepository
	) {
		this.s3Uploader = s3Uploader;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
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
	

	@Transactional(readOnly = true)
	public UserResponse findUser(Long id) {
		return findActiveUser(id).toResponse();
	}
	
	@Transactional
	public Long update(Long id, UserUpdateRequest userUpdateRequest, MultipartFile file)
			throws IOException {
		
		User user = findActiveUser(id);
		userUpdateRequest.setProfileImage(user.getProfileImage());
		if(!file.isEmpty()){
			userUpdateRequest.setProfileImage(s3Uploader.upload(
					file,
					DIRECTORY
			));
		}
		user.updateUserInfo(userUpdateRequest);
		
		return user.getId();
	}
	
	@Transactional
	public Long delete(Long id){
		userRepository.deleteById(findActiveUser(id).getId());
		return id;
	}
	
	private boolean isDuplicateUser(UserSignUpRequest userSignUpRequest) {
		if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
			throw new DuplicateUserException(ErrorCode.CONFLICT_VALUE_ERROR);
		}
		
		return false;
	}
	
	@Transactional(readOnly = true)
	public User findActiveUser(Long id) {
		return userRepository.findById(id)
		                     .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
	}
}
