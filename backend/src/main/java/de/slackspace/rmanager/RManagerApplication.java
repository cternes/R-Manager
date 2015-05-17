package de.slackspace.rmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Swagger URI /v2/api-docs
//@EnableSwagger2
public class RManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RManagerApplication.class, args);
    }
}
