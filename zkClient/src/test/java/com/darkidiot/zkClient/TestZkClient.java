package com.darkidiot.zkClient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import static com.darkidiot.zkClient.Constant.getConnectInfo;

/**
 * zkClient测试类
 * Copyright (c) for darkidiot
 * Date:2017/4/10
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
public class TestZkClient {

    @Test
    public void testCreateNode() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        UserDto user = new UserDto("idiot", "dark", "man", "java developer", "programming");
        String path = client.create("/root-node", user, CreateMode.PERSISTENT);
        log.info("create node: {}", path);
    }

    @Test
    public void testGetData() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        Stat stat = new Stat();
        UserDto user = client.readData("/root-node",stat);
        log.info("get node data: {}",user.toString());
        log.info("get stat: {}",stat.toString());
    }
}
