package com.darkidiot.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@SpringBootApplication
public class StartApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(StartApplication.class);
        application.run(args);
    }

//    @Bean
//    public MongoOperations getOperations() {
//        return new MongoTemplate(new SimpleMongoDbFactory(new Mongo("127.0.0.1",27017), "test"));
//    }
}
