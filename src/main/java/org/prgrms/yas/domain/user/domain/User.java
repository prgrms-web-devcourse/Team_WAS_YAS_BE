package org.prgrms.yas.domain.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.prgrms.yas.domain.user.dto.UserPasswordRequest;
import org.prgrms.yas.domain.user.dto.UserResponse;
import org.prgrms.yas.domain.user.dto.UserUpdateRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@SQLDelete(sql = "UPDATE routine SET is_deleted = true WHERE id =?")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 20)
	private String name;
	
	@Column(length = 30)
	private String nickname;
	
	@Column(nullable = false)
	private String email;
	
	private String password;
	
	@Column(columnDefinition = "TEXT")
	private String profileImage;
	
	@Column(nullable = false, columnDefinition = "TINYINT default false")
	private boolean isDeleted;
	
	@Enumerated(EnumType.STRING)
	private Role roles;
	
	private String provider;
	
	private String providerId;
	
	@Builder
	private User(
			Long id, String name, String nickname, String email, String provider, String providerId,
			String password, String profileImage
	) {
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.profileImage = profileImage;
		this.roles = Role.ROLE_USER;
		this.provider = provider;
		this.providerId = providerId;
	}
	
	public UserResponse toResponse() {
		return UserResponse.builder()
		                   .userId(id)
		                   .name(name)
		                   .email(email)
		                   .nickname(nickname)
		                   .profileImage(profileImage)
		                   .build();
	}
	
	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));
		
		return grantedAuthorities;
	}
	
	public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
		if (!passwordEncoder.matches(
				credentials,
				password
		)) {
			throw new IllegalArgumentException("Bad credentials");
		}
	}
	
	public void updateUserInfo(UserUpdateRequest userUpdateRequest) {
		this.nickname = userUpdateRequest.getNickname();
		this.profileImage = userUpdateRequest.getProfileImage();
	}
	
	public void updateUserPasswordInfo(PasswordEncoder passwordEncoder, UserPasswordRequest userPasswordRequest){
		this.password = passwordEncoder.encode(userPasswordRequest.getNewPassword());
	}
}
