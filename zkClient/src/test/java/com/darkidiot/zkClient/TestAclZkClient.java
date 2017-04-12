package com.darkidiot.zkClient;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import sun.security.acl.AclImpl;

import java.security.acl.Acl;
import java.util.List;

import static com.darkidiot.zkClient.Constant.getConnectInfo;

/**
 * TestAclZkClient 测试类
 * Copyright (c) for darkidiot
 * Date:2017/4/12
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
public class TestAclZkClient {
    @Test
    public void testCreateNode() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        UserDto user = new UserDto("idiot", "dark", "man", "java developer", "programming");
        List<Acl> acls = Lists.newArrayList();
        acls.add(new AclImpl());
        String path = client.create("/root-node/new-node", user,acls, CreateMode.PERSISTENT);
        log.info("create node: {}", path);
    }

    @Test
    public void testGetData() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        client.addAuthInfo();
        String path = client.readData("/root-node/new-node");
        log.info("create node: {}", path);
    }
}
}
