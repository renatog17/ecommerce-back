package com.renato.projects.ecommerce.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.renato.projects.ecommerce.controller.dto.user.PostUserDTO;
import com.renato.projects.ecommerce.domain.User;
import com.renato.projects.ecommerce.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<?> postUser(@RequestBody @Valid PostUserDTO postUserDTO, UriComponentsBuilder uriComponentsBuilder){
		System.out.println("aqui");
		User user = userService.save(postUserDTO.toModel());
		URI uri = uriComponentsBuilder
				.path("/user/{id}")
				.buildAndExpand(user.getId())
				.toUri();
		return ResponseEntity.created(uri).body(user);
	}
}
