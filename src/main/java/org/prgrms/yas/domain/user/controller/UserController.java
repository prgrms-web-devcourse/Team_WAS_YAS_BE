package org.prgrms.yas.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserPasswordChangeRequest;
import org.prgrms.yas.domain.user.dto.UserPasswordRequest;
import org.prgrms.yas.domain.user.dto.UserResponse;
import org.prgrms.yas.domain.user.dto.UserSignInRequest;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.prgrms.yas.domain.user.dto.UserToken;
import org.prgrms.yas.domain.user.dto.UserUpdateRequest;
import org.prgrms.yas.domain.user.service.UserService;
import org.prgrms.yas.global.response.ApiResponse;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.prgrms.yas.jwt.JwtAuthenticationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Validated
@RestController
public class UserController {
	
	private final AuthenticationManager authenticationManager;
	
	private final UserService userService;
	
	public UserController(
			AuthenticationManager authenticationManager, UserService userService
	) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}
	
	@Operation(summary = "????????? JWT ?????? ?????? ????????????")
	@PostMapping("/users/login")
	public ResponseEntity<ApiResponse<UserToken>> signIn(
			@RequestBody UserSignInRequest userSignInRequest
	) {
		
		JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
				userSignInRequest.getEmail(),
				userSignInRequest.getPassword()
		);
		
		Authentication resultToken = authenticationManager.authenticate(authenticationToken);
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) resultToken;
		JwtAuthentication principal = (JwtAuthentication) jwtAuthenticationToken.getPrincipal();
		User user = (User) jwtAuthenticationToken.getDetails();
		userService.findActiveUser(user.getId());
		
		return ResponseEntity.ok(ApiResponse.of(new UserToken(
				user.getId(),
				principal.getToken(),
				user.getRoles()
				    .toString()
		)));
	}
	
	@Operation(summary = "???????????? ????????????")
	@PostMapping("/users")
	public ResponseEntity<ApiResponse<Long>> singUp(
			@Valid @RequestBody UserSignUpRequest userSignUpRequest
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.signUp(userSignUpRequest)));
	}
	
	@Operation(summary = "???????????? ????????????")
	@GetMapping("/users")
	public ResponseEntity<ApiResponse<UserResponse>> find(
			@ApiIgnore @AuthenticationPrincipal JwtAuthentication token
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.findUser(token.getId())));
	}
	
	@Operation(summary = "???????????? ????????????")
	@PutMapping(value = "/users",consumes = {"multipart/form-data"})
	public ResponseEntity<ApiResponse<Long>> update(
			@ApiIgnore @AuthenticationPrincipal JwtAuthentication token,
			@Valid @RequestPart UserUpdateRequest userUpdateRequest,
			@RequestPart(value = "file", required = false) MultipartFile file
	) throws IOException {
		
		return ResponseEntity.ok(ApiResponse.of(userService.update(
				token.getId(),
				userUpdateRequest,
				file
		)));
	}
	
	@Operation(summary = "???????????? ????????????")
	@DeleteMapping("/users")
	public ResponseEntity<ApiResponse<Long>> delete(
			@ApiIgnore @AuthenticationPrincipal JwtAuthentication token,
			@Valid @RequestBody UserPasswordRequest userPasswordRequest
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.delete(token.getId(),userPasswordRequest)));
	}
	
	@Operation(summary = "????????????(????????????) ????????????")
	@PutMapping("/users/password")
	public ResponseEntity<ApiResponse<Long>> updatePassword(
			@AuthenticationPrincipal JwtAuthentication token,
			@Valid @RequestBody UserPasswordChangeRequest userPasswordChangeRequest
	){
		return ResponseEntity.ok(ApiResponse.of(userService.updatePassword(
				token.getId(),
				userPasswordChangeRequest
		)));
	}
	
	@Operation(summary = "??????????????? ????????? ????????????")
	@GetMapping("/users/email")
	public ResponseEntity<ApiResponse<Boolean>> checkValidEmail(
			@Email @RequestParam(value="value") String email
	){
		return ResponseEntity.ok(ApiResponse.of(userService.isValidEmail(email)));
	}
}
