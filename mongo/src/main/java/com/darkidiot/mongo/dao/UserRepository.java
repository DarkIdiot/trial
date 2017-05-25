package com.darkidiot.mongo.dao;

import com.darkidiot.mongo.model.NewUser;
import com.darkidiot.mongo.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
public interface UserRepository extends MongoRepository<NewUser, String> {
}
