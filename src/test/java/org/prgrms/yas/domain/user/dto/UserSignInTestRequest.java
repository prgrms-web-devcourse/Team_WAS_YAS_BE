package org.prgrms.yas.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserSignInTestRequest {
	private String email;

	private String password;
	
	public UserSignInTestRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
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
}
