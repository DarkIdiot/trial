package com.darkidiot.mongo.model;

import lombok.Data;
import org.bson.types.ObjectId;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Data
public class User {
    private ObjectId objectId;
    private String firstName;
    private String lastName;
    private Integer age;
}
