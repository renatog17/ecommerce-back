package com.renato.projects.ecommerce.controller.dto.user;

import com.renato.projects.ecommerce.domain.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUserDTO(
		@NotBlank(message = "Email is mandatory.")
		@Email(message = "Email must follow a valid email format")
		String email, 
		@NotBlank(message = "Password is mandatory")
		@Size(min = 8, max = 16)
		String password) {

	public User toModel() {
		return new User(email, password);
	}
}
