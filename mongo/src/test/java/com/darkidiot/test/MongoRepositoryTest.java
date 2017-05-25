package com.darkidiot.test;

import com.darkidiot.mongo.model.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * mongo 测试类
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
public class MongoRepositoryTest {

    private static ApplicationContext ctx;

    @BeforeClass
    public static void setUp() {
        ctx = new GenericXmlApplicationContext("classpath*:application-mongo.xml");
    }

    @Test
    public void testAdd() {

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        User user = new User();
        user.setFirstName("darkidiot");
        Query logQuery = new Query(Criteria.where("firstName").is("darkidiot"));
        user = mongoOperation.findOne(logQuery, User.class, "user");
        Assert.assertNotNull(user);
    }

}
