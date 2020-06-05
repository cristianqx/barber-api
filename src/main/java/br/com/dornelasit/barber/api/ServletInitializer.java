/*
 * © 2020 Dit Labs - Copyright - Todos os direitos reservados.
 */
package br.com.dornelasit.barber.api;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BarberApiApplication.class);
	}

}
