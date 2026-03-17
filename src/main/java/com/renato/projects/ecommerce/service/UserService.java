package com.renato.projects.ecommerce.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.renato.projects.ecommerce.domain.Role;
import com.renato.projects.ecommerce.domain.enums.RoleName;
import com.renato.projects.ecommerce.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renato.projects.ecommerce.controller.dto.user.PostUserDTO;
import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.UserDetailsImpl;
import com.renato.projects.ecommerce.repository.UserRepository;
import com.renato.projects.ecommerce.service.email.EmailData;
import com.renato.projects.ecommerce.service.email.EmailService;
import com.renato.projects.ecommerce.service.email.context.ConfirmEmailContext;
import com.renato.projects.ecommerce.service.email.templates.IEmailTemplate;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	private final IEmailTemplate<ConfirmEmailContext> emailTemplate;
	private final EmailService emailService;
	private ClienteService clienteService;
	private RoleRepository roleRepository;

	public UserService(UserRepository userRepository, PasswordEncoder encoder, 
			IEmailTemplate<ConfirmEmailContext> emailTemplate, EmailService emailService,
			ClienteService clienteService, RoleRepository roleRepository) {
		super();
		this.encoder = encoder;
		this.userRepository = userRepository;
		this.emailTemplate = emailTemplate;
		this.emailService = emailService;
		this.clienteService = clienteService;
		this.roleRepository = roleRepository;
	}

	@Transactional
	public UserDetailsImpl save(PostUserDTO userDTO) {
		//salvar{
		
		UserDetailsImpl user = userDTO.toModel();
		
		user.setPassword(encoder.encode(user.getPassword()));
		user.setVerified(false);
		String token = UUID.randomUUID().toString();
		user.setVerificationToken(token);
		user.setTokenExpiry(Instant.now().plus(1, ChronoUnit.HOURS));

		Role roleCustomer = roleRepository
				.findByName(RoleName.ROLE_CUSTOMER)
				.orElseThrow(() -> new RuntimeException("Role not found"));

		user.setRoles(Set.of(roleCustomer));
		userRepository.save(user);

		
		Cliente cliente = userDTO.cliente().toModel();
		cliente.setUser(user);
		
		clienteService.salvarCliente(cliente);
		//}
		//enviar email{
		ConfirmEmailContext confirmEmailContext = new ConfirmEmailContext(token, user.getEmail(), "Sem nome");
		EmailData build = emailTemplate.build(confirmEmailContext);
		emailService.sendEmail(build);
		//}
		return user;
	}
}
