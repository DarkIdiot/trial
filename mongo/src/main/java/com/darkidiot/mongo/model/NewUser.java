package com.darkidiot.mongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Data
public class NewUser {
    @Id
    private String uid;
    private String firstName;
    private String lastName;
    private Integer age;
}
