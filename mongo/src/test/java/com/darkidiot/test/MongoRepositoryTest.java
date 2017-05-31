package com.darkidiot.test;

import com.darkidiot.mongo.StartApplication;
import com.darkidiot.mongo.dao.UserRepository;
import com.darkidiot.mongo.model.NewUser;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
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
    public void testInsert() {
        NewUser entity = new NewUser();
        entity.setFirstName("testFirstName");
        entity.setLastName("testLastName");
        userRepository.insert(entity);
        List<NewUser> users = userRepository.findAll();
        for (NewUser newUser : users) {
            log.info("UserModel:{}", new Gson().toJson(newUser));
        }
    }

    @Test
    public void testSave() {
        List<NewUser> entityList = Lists.newArrayList();
        NewUser entity1 = new NewUser();
        entity1.setAge(10);
        entity1.setFirstName("testFirstName1");
        entity1.setLastName("testLastName1");
        entityList.add(entity1);
        NewUser entity2 = new NewUser();
        entity2.setAge(20);
        entity2.setFirstName("testFirstName2");
        entity2.setLastName("testLastName2");
        entityList.add(entity2);
        userRepository.save(entityList);
    }

    @Test
    public void testFindAll() {
        List<NewUser> users = userRepository.findAll();
        for (NewUser newUser : users) {
            log.info("UserModel:{}", new Gson().toJson(newUser));
        }
    }

    @Test
    public void testFindByCriteria() {
        PageRequest pageRequest = new PageRequest(0, 10);
        Page<NewUser> userPage = userRepository.findByAgeGreaterThan(9, pageRequest);
        for (NewUser user : userPage.getContent()) {
            log.info("UserModel:{}",new Gson().toJson(user));
        }

    }

    @Test
    public void testUpdate() {
        NewUser user = userRepository.findOne("5926c46fda81871c5025208e");
        log.info("Before UserModel:{}",new Gson().toJson(user));
        user.setLastName("i am changed.");
        userRepository.save(user);

        NewUser newUser = userRepository.findOne("5926c46fda81871c5025208e");
        log.info("After UserModel:{}",new Gson().toJson(newUser));
    }

    @Test
    public void testDelete() {
        userRepository.delete("5926c46fda81871c5025208e");
    }

    @Test
    public void testDeleteAll() {
        userRepository.deleteAll();
    }


}
