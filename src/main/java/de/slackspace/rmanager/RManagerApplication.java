package de.slackspace.rmanager;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class RManagerApplication {

    public static void main(String[] args) {
    	Flyway flyway = new Flyway();
		flyway.setDataSource("jdbc:mysql://localhost:3306/rmanager", "root", "root");
		flyway.migrate();
    	
        SpringApplication.run(RManagerApplication.class, args);
    }
}
