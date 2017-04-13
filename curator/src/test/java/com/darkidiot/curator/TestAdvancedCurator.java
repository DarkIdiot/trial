package com.darkidiot.curator;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.curator.shaded.com.google.common.base.Throwables;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.Arrays;

import static com.darkidiot.curator.Constant.getConnectInfo;

/**
 * TestAdvancedCurator 测试类
 * Copyright (c) for darkidiot
 * Date:2017/4/14
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
public class TestAdvancedCurator {

    @Test
    public void testSetData() {
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
            Stat stat = new Stat();
            byte[] data = client.getData().storingStatIn(stat).forPath("/darkidiot");
            log.info("get data:{}", Arrays.toString(data));
            log.info("stat:{}",new Gson().toJson(stat));
            client.setData().withVersion(stat.getVersion()).forPath("/darkidiot", "joe".getBytes());
        } catch (Exception e) {
            log.error("client create error:{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testNodeExists() {
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
            Stat stat = client.checkExists().forPath("/darkidiot");
            log.info("stat:{}",new Gson().toJson(stat));
        } catch (Exception e) {
            log.error("client create error:{}", Throwables.getStackTraceAsString(e));
        }
    }
}
