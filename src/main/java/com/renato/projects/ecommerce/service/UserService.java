package com.renato.projects.ecommerce.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.renato.projects.ecommerce.domain.User;
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

	public UserService(UserRepository userRepository, PasswordEncoder encoder, IEmailTemplate<ConfirmEmailContext> emailTemplate, EmailService emailService) {
		super();
		this.encoder = encoder;
		this.userRepository = userRepository;
		this.emailTemplate = emailTemplate;
		this.emailService = emailService;
	}

	public User save(User user) {
		//salvar{
		user.setPassword(encoder.encode(user.getPassword()));
		user.setVerified(false);
		String token = UUID.randomUUID().toString();
		user.setVerificationToken(token);
		user.setTokenExpiry(Instant.now().plus(1, ChronoUnit.HOURS));
		userRepository.save(user);
		//}
		//enviar email{
		ConfirmEmailContext confirmEmailContext = new ConfirmEmailContext(token, user.getEmail(), "Sem nome");
		EmailData build = emailTemplate.build(confirmEmailContext);
		emailService.sendEmail(build);
		//}
		return user;
	}
}
