package com.darkidiot.zkClient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.junit.Test;

import static com.darkidiot.zkClient.Constant.getConnectInfo;

/**
 * TestAdvancedZkClient测试类
 * Copyright (c) for darkidiot
 * Date:2017/4/10
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
public class TestAdvancedZkClient {

    @Test
    public void testWriteData() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        UserDto user = new UserDto("idiot", "dark", "man", "java developer", "programming");
        client.writeData("/root-node", user, -1);
    }

    @Test
    public void testSubscribeChildChanges() throws InterruptedException {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        client.subscribeChildChanges("/root-node", new ZkChildListener());
        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    public void testSubscribeDataChanges() throws InterruptedException {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        client.subscribeDataChanges("/root-node", new ZkDataListener());
        Thread.sleep(Integer.MAX_VALUE);
    }


}
