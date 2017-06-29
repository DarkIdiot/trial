package com.darkidiot.zkClient;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
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
    public void testCreateNode() throws NoSuchAlgorithmException {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        UserDto user = new UserDto("idiot", "dark", "man", "java devper", "programming");
        List<ACL> acls = Lists.newArrayList();
        //创建节点的时候要求权限验证
        //基于IP
        ACL aclIp = new ACL(ZooDefs.Perms.READ, new Id("ip", "112.74.219.174"));
        //基于用户名密码
        ACL aclDigest = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("darkidiot:abcd123")));
        acls.add(aclDigest);
        acls.add(aclIp);
        String path = client.create("/root-node/new-node", user, acls, CreateMode.PERSISTENT);
        log.info("create node: {}", path);
    }

    @Test
    public void testGetData() {
        ZkClient client = new ZkClient(getConnectInfo(), 5000, 5000, new SerializableSerializer());
        log.info("connect ok...");
        client.addAuthInfo("digest", "darkidiot:abcd123".getBytes());
        UserDto user = client.readData("/root-node/new-node");
        log.info("get data: {}", user);
    }
}
