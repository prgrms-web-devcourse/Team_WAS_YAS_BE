package org.prgrms.yas.configures;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI yasOpenAPI() {
		return new OpenAPI().info(new Info().title("YAS_WAS")
		                                    .description("YAS Application")
		                                    .version("v0.0.1")
		                                    .license(new License().name("Apache 2.0")
		                                                          .url("http://springdoc.org")))
		                    .externalDocs(new ExternalDocumentation().description("YAS Github")
		                                                             .url("https://github.com/prgrms-web-devcourse/Team_WAS_YAS_BE"));
	}
}
