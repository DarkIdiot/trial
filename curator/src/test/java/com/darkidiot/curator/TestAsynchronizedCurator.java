package com.darkidiot.curator;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.curator.shaded.com.google.common.base.Throwables;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.darkidiot.curator.Constant.getConnectInfo;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/14
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
public class TestAsynchronizedCurator {

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

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        Object ctx = new Object();
        log.info("logged the ctx:{}", ctx.hashCode());
        try {
            client.checkExists().inBackground(new BackgroundCallback() {
                public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                    log.info("logged the ctx:{}", curatorEvent.getContext().hashCode());
                    Stat stat = curatorEvent.getStat();
                    log.info("stat:{}", new Gson().toJson(stat));
                    String path = curatorEvent.getPath();
                    log.info("path:{}", path);
                    byte[] data = curatorEvent.getData();
                    log.info("data:{}", Arrays.toString(data));
                    List<ACL> aclList = curatorEvent.getACLList();
                    log.info("aclList:{}", aclList);
                    List<String> children = curatorEvent.getChildren();
                    log.info("children:{}", children);
                    String name = curatorEvent.getName();
                    log.info("name:{}", name);
                    int resultCode = curatorEvent.getResultCode();
                    log.info("resultCode:{}", resultCode);
                    CuratorEventType type = curatorEvent.getType();
                    log.info("type:{}", type);
                    WatchedEvent watchedEvent = curatorEvent.getWatchedEvent();
                    log.info("watchedEvent:{}", watchedEvent);

                }
            }, ctx, singleThreadExecutor).forPath("/darkidiot");
        } catch (Exception e) {
            log.error("client create error:{}", Throwables.getStackTraceAsString(e));
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            log.error("Thread sleep err:{}", Throwables.getStackTraceAsString(e));
        }
    }
}
