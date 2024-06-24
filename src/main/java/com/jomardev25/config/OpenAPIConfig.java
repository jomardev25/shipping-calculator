package com.jomardev25.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

  @Bean
  OpenAPI myOpenAPI() {
   
    var contact = new Contact().email("jomarignacio25@gmail.com").name("Jomar Ignacio");
    var mitLicense = new License().name("MIT License").url("https://github.com/jomardev25");
    var info = new Info()
        .title("Shpping Cost Calculator")
        .version("1.0")
        .contact(contact)
        .description("This API exposes endpoints Shpping Cost Calculator.").termsOfService("https://github.com/jomardev25")
        .license(mitLicense);

    return new OpenAPI().info(info);
  }
	

}
