package io.pivotal.hacker.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HackerTrackerApplication {

    public static void main(String[] args) {
//        NewsController newsController = new NewsController();
//        newsController.setTitlesAndIds();

        SpringApplication.run(HackerTrackerApplication.class, args);

    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
