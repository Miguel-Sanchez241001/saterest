package pe.com.bn.wsrestsate;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@ComponentScan(basePackages = "pe.com.bn.wsrestsate")
@PropertySource("classpath:satews.properties")
public class AppInicializer  {
	   @Bean
	    public ObjectMapper objectMapper() {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); // Formato personalizado
	        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Evitar timestamps
	        return mapper;
	    }
    
 
}
