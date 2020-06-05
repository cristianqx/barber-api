/*
 * © 2020 Dit Labs - Copyright - Todos os direitos reservados.
 */
package br.com.dornelasit.barber.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BarberApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarberApiApplication.class, args);
	}

}
