package com.darkidiot.mongo.dao;

import com.darkidiot.mongo.model.NewUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
public interface UserRepository extends MongoRepository<NewUser, String> {

    Page<NewUser> findByAgeGreaterThan(int age, Pageable page);

    @Query("{ 'firstName':{'$regex':?1,'$options':'i'}, age': {'$gte':?2,'$lte':?3}}")
    Page<NewUser> findByNameAndAgeRange(String name, double ageFrom, double ageTo, Pageable page);

    /**
     * value:指定查询的参数
     * fields:指定要返回的数据字段
     * @param name
     * @param ageFrom
     * @param ageTo
     * @param page
     * @return
     */
    @Query(value = "{ 'name':{'$regex':?1,'$options':'i'}, age':{'$gte':?2,'$lte':?3}}", fields = "{ 'name' : 1, 'age' : 1}")
    Page<NewUser> findByNameAndAgeRange2(String name, double ageFrom, double ageTo, Pageable page);

}

/*
    GreaterThan(大于)
    findByAgeGreaterThan(int age)
    {"age" : {"$gt" : age}}

    LessThan（小于）
    findByAgeLessThan(int age)
    {"age" : {"$lt" : age}}

    Between（在...之间）
    findByAgeBetween(int from, int to)
    {"age" : {"$gt" : from, "$lt" : to}}

    IsNotNull, NotNull（是否非空）
    findByFirstNameNotNull()
    {"age" : {"$ne" : null}}

    IsNull, Null（是否为空）
    findByFirstNameNull()
    {"age" : null}

    Like（模糊查询）
    findByFirstNameLike(String name)
    {"age" : age} ( age as regex)

    (No keyword) findByFirstName(String name)
    {"age" : name}

    Not（不包含）
    findByFirstNameNot(String name)
    {"age" : {"$ne" : name}}

    Near（查询地理位置相近的）
    findByLocationNear(Point point)
    {"location" : {"$near" : [x,y]}}

    Within（在地理位置范围内的）
    findByLocationWithin(Circle circle)
    {"location" : {"$within" : {"$center" : [ [x, y], distance]}}}

    Within（在地理位置范围内的）
    findByLocationWithin(Box box)
    {"location" : {"$within" : {"$box" : [ [x1, y1], x2, y2]}}}
*/
