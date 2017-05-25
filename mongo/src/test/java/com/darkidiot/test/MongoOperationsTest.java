package com.darkidiot.test;

import com.darkidiot.mongo.StartApplication;
import com.darkidiot.mongo.model.User;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * mongo 测试类
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StartApplication.class)
public class MongoOperationsTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    public void testSave() {
        User user = new User(1002, "Joe", 341);
        mongoOperations.insert(user, "user");
        log.info("userModel:{}", new Gson().toJson(user));
    }


    @Test
    public void testQuery() {
        User user = mongoOperations.findOne(new Query(Criteria.where("name").regex("Joe")), User.class, "user");
        log.info("userModel:{}", new Gson().toJson(user));
    }


    @Test
    public void testUpdate() {
        mongoOperations.updateFirst(new Query(Criteria.where("name").regex("Joe")), Update.update("name", "darkidiot"), "user");
        // find it
        User user = mongoOperations.findOne(new Query(Criteria.where("objectId").is(1002)), User.class, "user");
        log.info("userModel:{}", new Gson().toJson(user));
    }

    @Test
    public void testDelete() {
        mongoOperations.remove(new Query(Criteria.where("objectId").is(1002)), User.class, "user");
    }

    @Test
    public void testOriginalOperation() {
        DBCollection dbCollection = mongoOperations.getCollection("test");
        log.info("size:{}",dbCollection.getCount());
        BasicDBObject doc = new BasicDBObject("name", "MongoDB").append("type", "database")
                .append("count", 1)
                .append("info", new BasicDBObject("x", 203).append("y", 102));
        dbCollection.insert(doc);

        DBObject myDoc = dbCollection.findOne();
        log.info("DBObject Model:{}",new Gson().toJson(myDoc));
    }
}