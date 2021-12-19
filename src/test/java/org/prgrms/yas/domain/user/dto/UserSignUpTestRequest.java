package org.prgrms.yas.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserSignUpTestRequest {
	
	private String email;
	private String password;
	private String checkPassword;
	private String nickname;
	private String name;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getCheckPassword() {
		return checkPassword;
	}
	
	public void setCheckPassword(String checkPassword) {
		this.checkPassword = checkPassword;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public UserSignUpTestRequest(
			String email, String password, String checkPassword, String nickname, String name
	) {
		this.email = email;
		this.password = password;
		this.checkPassword = checkPassword;
		this.nickname = nickname;
		this.name = name;
	}
}
