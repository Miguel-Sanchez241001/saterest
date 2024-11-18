package pe.com.bn.wsrestsate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pe.com.bn.wsrestsate.enpoints")) // Ajusta al paquete de tus controladores
                .paths(PathSelectors.any())
                .build()
               
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "API de ejemplo",                      // Título
                "Documentación de la API REST",        // Descripción
                "1.0",                                 // Versión
                "Términos del servicio",               // Términos del servicio
                new Contact("Tu Nombre", "URL", "email@example.com"), // Contacto
                "Licencia",                            // Licencia
                "URL de la licencia",                  // URL de la licencia
                Collections.emptyList());              // Extensiones
    }

    // Configuración opcional para la interfaz de Swagger
    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .displayRequestDuration(true) // Muestra la duración de las solicitudes
                .build();
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Habilita CORS para todos los endpoints
                        .allowedOrigins("*") // Cambia "*" por los dominios permitidos
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }
}
