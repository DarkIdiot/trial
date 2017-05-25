package com.darkidiot.test;

import com.darkidiot.mongo.model.User;
import com.mongodb.Mongo;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * mongo 测试类
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
public class MongoTemplateTest {
    private static MongoOperations mongoOperations;

    private static Mongo mongo() {
        return new Mongo("127.0.0.1", 27017);
    }

    private static MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), "test");
    }

    private static MongoTemplate mongoTemplateNew() {
        return new MongoTemplate(new SimpleMongoDbFactory(new Mongo(), "database"));
    }


    @BeforeClass
    public static void setUp() {
        mongoOperations = mongoTemplate();
    }

    @Test
    public void testQuery1() {
        User user = new User("Joe", 34);

        // Insert is used to initially store the object into the database.
        mongoOperations.insert(user);
        log.info("Insert: " + user);

        // Find
        user = mongoOperations.findById(user.getObjectId(), User.class);
        log.info("Found: " + user);

        // Update
        mongoOperations.updateFirst(query(where("name").is("Joe")), update("age", 35), User.class);
        user = mongoOperations.findOne(query(where("name").is("Joe")), User.class);
        log.info("Updated: " + user);

        // Delete
        mongoOperations.remove(user);

        // Check that deletion worked
        List<User> people = mongoOperations.findAll(User.class);
        log.info("Number of people = : " + people.size());


        mongoOperations.dropCollection(User.class);
    }


}
