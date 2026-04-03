package com.renato.projects.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;


@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(EcommerceApplication.class);

		// Aqui você define o ApplicationStartup customizado
		app.setApplicationStartup(new BufferingApplicationStartup(2048));

		// Inicia a aplicação
		app.run(args);
	}
}