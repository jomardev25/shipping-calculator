package com.jomardev25.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

@Configuration
public class OpenAPIConfig {

  @Bean
  OpenAPI myOpenAPI() throws IOException {
    ClassPathResource resource = new ClassPathResource("swagger.yaml");
    Path path = Paths.get(resource.getURI());
    String content = new String(Files.readAllBytes(path));

    SwaggerParseResult result = new OpenAPIV3Parser().readContents(content);

    if (result.getMessages().isEmpty() && result.getOpenAPI() != null) {
      return result.getOpenAPI();
    } else {
      throw new RuntimeException("Failed to parse OpenAPI definition: " + result.getMessages());
    }
  }

}
