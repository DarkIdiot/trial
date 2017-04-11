package com.darkidiot.zkClient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;

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
        String path = client.create("/root-node", user, CreateMode.EPHEMERAL);
        log.info("create node: {}", path);
    }

    @Test
    public void testGetData() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        Stat stat = new Stat();
        UserDto user = client.readData("/root-node", stat);
        log.info("get node data: {}", user.toString());
        log.info("get stat: {}", stat.toString());
    }

    @Test
    public void testGetChildren() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        List<String> children = client.getChildren("/root-node");
        log.info("get children: {}", children.toString());
    }

    @Test
    public void testExists() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        boolean exists = client.exists("/root-node");
        log.info("exists node: {}", exists);
    }

    @Test
    public void testDeleteNode() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        boolean exists = client.delete("/root-node");
        log.info("delete node: {}", exists);
    }

    @Test
    public void testDeleteRecursiveNode() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        boolean exists = client.deleteRecursive("/root-node");
        log.info("recursive delete node: {}", exists);
    }
}
