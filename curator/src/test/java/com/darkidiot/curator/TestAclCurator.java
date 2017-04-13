package com.darkidiot.curator;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.curator.shaded.com.google.common.base.Throwables;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static com.darkidiot.curator.Constant.getConnectInfo;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/14
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
public class TestAclCurator {
    @Test
    public void testCreateNodeWithAcl() throws NoSuchAlgorithmException {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(getConnectInfo())
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        log.info("connect ok...");
        List<ACL> acls = Lists.newArrayList();
        //创建节点的时候要求权限验证
        //基于IP
        ACL aclIp = new ACL(ZooDefs.Perms.READ, new Id("ip", "112.74.219.174"));
        //基于用户名密码
        ACL aclDigest = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("darkidiot:1234")));
        acls.add(aclDigest);
        acls.add(aclIp);
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(acls).forPath("/darkidiot-acl", "joe".getBytes());
        } catch (Exception e) {
            log.error("client create error:{}", Throwables.getStackTraceAsString(e));
        }
    }


    @Test
    public void testGetNodeDataWithAcl() {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        List<AuthInfo> authInfoList = Lists.newArrayList();
        authInfoList.add(new AuthInfo("digest", "darkidiot:1234".getBytes()));
        authInfoList.add(new AuthInfo("digest", "root:123456".getBytes()));
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(getConnectInfo())
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
//                .authorization("digest", "darkidiot:abcd123".getBytes())
                .authorization(authInfoList)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        log.info("connect ok...");

        try {
            Stat stat = new Stat();
            byte[] data = client.getData().storingStatIn(stat).forPath("/darkidiot-acl");
            log.info("stat:{}", new Gson().toJson(stat));
            log.info("get node data:{}", Arrays.toString(data));
        } catch (Exception e) {
            log.error("client create error:{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testDeleteNode() {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(getConnectInfo())
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        log.info("connect ok...");

        try {
            client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(-1).forPath("/darkidiot-acl");
        } catch (Exception e) {
            log.error("client create error:{}", Throwables.getStackTraceAsString(e));
        }
    }
}
