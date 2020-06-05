/*
 * © 2020 Dit Labs - Copyright - Todos os direitos reservados.
 */
package br.com.dornelasit.barber.api.swagger;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value( "${app.version}" )
	private String appVersion;
	
	@Value( "${server.servlet.context-path}" )
	private String contextPath;
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()     
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())    
          .build()
          .apiInfo(apiInfo());                                           
    }
	
	@Bean
    public FilterRegistrationBean<RedirectFilter> filterRegistrationBean(){
        FilterRegistrationBean<RedirectFilter> filterRegistrationBean = new FilterRegistrationBean<RedirectFilter>();
        filterRegistrationBean.setFilter(new RedirectFilter(contextPath, contextPath + "/swagger-ui.html"));
        return filterRegistrationBean;
    }
	
	private ApiInfo apiInfo() {
        return new ApiInfo(
          "Barber Api", 
          "Projeto de Sistema para barbearia.", 
          appVersion, 
          "Todos os direitos reservados - Dornelas It", 
          new Contact("Dit Lab", "www.dornelasit.com.br", "lab@dornelasit.com.br"), 
          "Informações de licença deverá ser requisitadas por contato.", "www.dornelasit.com.br/termos-de-uso", Collections.emptyList());
    }	
}
