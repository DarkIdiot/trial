package com.darkidiot.curator;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.curator.shaded.com.google.common.base.Throwables;
import org.junit.Test;

import java.util.Arrays;

import static com.darkidiot.curator.Constant.getConnectInfo;

/**
 * Copyright (c) for darkidiot
 * Date:2017/4/14
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
public class TestListenerCurator {

    @Test
    public void testNodeListener() throws Exception {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(getConnectInfo())
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        log.info("connect ok...");

        final NodeCache nodeCache = new NodeCache(client, "/darkidiot");
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                ChildData currentData = nodeCache.getCurrentData();
                byte[] data = currentData.getData();
                log.info("get the node data:{}", Arrays.toString(data));
            }
        });

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            log.error("Thread sleep err:{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testChildrenListener() throws Exception {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(getConnectInfo())
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        log.info("connect ok...");

        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/darkidiot", true);
        pathChildrenCache.start();
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        log.info("{} has been added.", new Gson().toJson(event.getData()));
                        break;
                    case CHILD_REMOVED:
                        log.info("{} has been removed.", new Gson().toJson(event.getData()));
                        break;
                    case CHILD_UPDATED:
                        log.info("{} has been updated.", new Gson().toJson(event.getData()));
                        break;
                }
            }
        });

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            log.error("Thread sleep err:{}", Throwables.getStackTraceAsString(e));
        }
    }

}
