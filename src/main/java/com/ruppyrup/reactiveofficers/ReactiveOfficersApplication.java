package com.ruppyrup.reactiveofficers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@EnableMongoAuditing
@SpringBootApplication
public class ReactiveOfficersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveOfficersApplication.class, args);
    }

}
