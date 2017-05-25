package com.darkidiot.test;

import com.darkidiot.mongo.StartApplication;
import com.darkidiot.mongo.dao.UserRepository;
import com.darkidiot.mongo.model.NewUser;
import com.darkidiot.mongo.model.User;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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
public class MongoRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeClass
    public static void setUp() {
    }

    @AfterClass
    public static void tearDown() {
    }

    @Test
    public void testFind() {
        User user = new User();
        user.setFirstName("darkidiot");
        List<NewUser> users = userRepository.findAll();
        for (NewUser newUser : users) {
            log.info("UserModel:{}", new Gson().toJson(newUser));
        }
        Assert.assertNotNull(user);
    }

}
