package io.pivotal.hacker.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HackerTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HackerTrackerApplication.class, args);
        JsonController jsonController = new JsonController();
        jsonController.getTop500();
    }
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
