package pe.com.bn.wsrestsate.config;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "pe.com.bn.wsrestsate.enpoints")
public class WebConfig implements WebMvcConfigurer{
	   @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        // Maneja los recursos estáticos en / (index.html y otros)
	        registry.addResourceHandler("/**")
	                .addResourceLocations("/");
	    }
	   
	   @Bean
	    public InternalResourceViewResolver viewResolver() {
	        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	        resolver.setPrefix("/WEB-INF/"); // Cambia esto si tu HTML está en otra carpeta
	        resolver.setSuffix(".html");
	        return resolver;
	    }
	
}
