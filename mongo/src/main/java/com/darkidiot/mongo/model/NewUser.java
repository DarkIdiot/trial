package com.darkidiot.mongo.model;

import org.springframework.data.annotation.Id;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
public class NewUser {
    @Id
    private String uid;
    private String firstName;
    private String lastName;
    private Integer age;
}
