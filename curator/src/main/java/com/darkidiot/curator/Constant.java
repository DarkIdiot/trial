package com.darkidiot.curator;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/10
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
public abstract class Constant {
//    final static String ip = "192.168.2.14";
    final static String ip = "127.0.0.1";
    final static String port = "2181";
    final static String splitter = ":";
    static String getConnetInfo() {
        return ip+splitter+port;
    }
}
