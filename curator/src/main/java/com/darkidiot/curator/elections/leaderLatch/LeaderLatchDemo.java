package com.darkidiot.curator.elections.leaderLatch;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * Copyright (c) for darkidiot
 * Date:2017/9/28
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc:
 */
public class LeaderLatchDemo {

    private static final String PATH = "/leader";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = Connection.getConnection();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(PATH, "client#1".getBytes());
        client.getData().forPath(PATH);

    }

}
