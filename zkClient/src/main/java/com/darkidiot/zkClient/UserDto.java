package com.darkidiot.zkClient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class UserDto implements Serializable {
    private String firstName;
    private String lastName;
    private String sex;
    private String post;
    private String hobby;
}
