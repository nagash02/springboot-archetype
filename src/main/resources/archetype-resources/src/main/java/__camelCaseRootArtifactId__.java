package cl.trabajando.api_ejemplo;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cl.trabajando.api_ejemplo.config.GlobalConfiguration;
import cl.trabajando.api_ejemplo.config.JwtConfiguration;
import cl.trabajando.api_ejemplo.config.MongoConfiguration;
import cl.trabajando.api_ejemplo.config.SecurityConfiguration;
import cl.trabajando.api_ejemplo.config.SwaggerConfiguracion;

@SpringBootApplication
public class ApiEjemploApplication {

    private static Logger logger = LogManager.getLogger(ApiEjemploApplication.class);
    
    public static void main(String[] args) {
	Class<?>[] configClasses = { GlobalConfiguration.class, SecurityConfiguration.class,
		SwaggerConfiguracion.class, JwtConfiguration.class,MongoConfiguration.class };
	SpringApplication.run(configClasses, args);
	logger.info("UP AND RUNNING");
    }

}
