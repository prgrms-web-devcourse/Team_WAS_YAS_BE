package org.prgrms.yas.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import javax.validation.Valid;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserResponse;
import org.prgrms.yas.domain.user.dto.UserSignInRequest;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.prgrms.yas.domain.user.dto.UserToken;
import org.prgrms.yas.domain.user.dto.UserUpdateRequest;
import org.prgrms.yas.domain.user.service.UserService;
import org.prgrms.yas.global.aws.S3Uploader;
import org.prgrms.yas.global.response.ApiResponse;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.prgrms.yas.jwt.JwtAuthenticationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class UserController {
	
	private static final String DIRECTORY = "static";
	
	private final AuthenticationManager authenticationManager;
	
	private final UserService userService;
	
	private final S3Uploader s3Uploader;
	
	public UserController(
			AuthenticationManager authenticationManager, UserService userService, S3Uploader s3Uploader
	) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.s3Uploader = s3Uploader;
	}
	
	@Operation(summary = "커스텀 로그인 JWT 토큰 발행 컨트롤러")
	@PostMapping("/users/login")
	public UserToken signIn(@RequestBody UserSignInRequest userSignInRequest) {
		
		JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
				userSignInRequest.getEmail(),
				userSignInRequest.getPassword()
		);
		
		Authentication resultToken = authenticationManager.authenticate(authenticationToken);
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) resultToken;
		JwtAuthentication principal = (JwtAuthentication) jwtAuthenticationToken.getPrincipal();
		User user = (User) jwtAuthenticationToken.getDetails();
		
		return new UserToken(
				user.getId(),
				principal.getToken(),
				principal.getUsername(),
				user.getRoles()
				    .toString()
		);
	}
	
	@Operation(summary = "회원가입 컨트롤러")
	@PostMapping("/users")
	public ResponseEntity<ApiResponse<Long>> singUp(
			@Valid @RequestBody UserSignUpRequest userSignUpRequest
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.signUp(userSignUpRequest)));
	}
	
	@Operation(summary = "회원조회 컨트롤러")
	@GetMapping("/users")
	public ResponseEntity<ApiResponse<UserResponse>> find(
			@ApiIgnore @AuthenticationPrincipal JwtAuthentication token
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.findUser(token.getId())));
	}
	
	@Operation(summary = "회원수정 컨트롤러")
	@PutMapping("/users")
	public ResponseEntity<ApiResponse<Long>> update(
			@ApiIgnore @AuthenticationPrincipal JwtAuthentication token,
			@Valid @RequestPart UserUpdateRequest userUpdateRequest,
			@RequestPart(required = false) MultipartFile file
	) throws IOException {
		
		userUpdateRequest.setProfileImage(s3Uploader.upload(
				file,
				DIRECTORY
		));
		
		return ResponseEntity.ok(ApiResponse.of(userService.update(
				token.getId(),
				userUpdateRequest
		)));
	}
	
	@Operation(summary = "회원삭제 컨트롤러")
	@DeleteMapping("/users")
	public ResponseEntity<ApiResponse<Long>> delete(
			@ApiIgnore @AuthenticationPrincipal JwtAuthentication token
	){
		return ResponseEntity.ok(ApiResponse.of(userService.delete(token.getId())));
	}
}
