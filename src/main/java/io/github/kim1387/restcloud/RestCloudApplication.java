package io.github.kim1387.restcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(excludeName = {"io.github.kim1387.restcloud.storage"})
public class RestCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestCloudApplication.class, args);
    }

}
