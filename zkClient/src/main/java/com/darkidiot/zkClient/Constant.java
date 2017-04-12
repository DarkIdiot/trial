package com.darkidiot.zkClient;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/10
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
abstract class Constant {
    private final static String ip = "localhost";
    private final static String port = "2181";
    private final static String splitter = ":";

    static String getConnectInfo() {
        return ip + splitter + port;
    }
}
