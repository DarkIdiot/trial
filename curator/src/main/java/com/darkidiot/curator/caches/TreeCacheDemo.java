package com.darkidiot.curator.caches;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.utils.CloseableUtils;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/9
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc:
 */
public class TreeCacheDemo {
    private static final String PATH = "/treeCache";

    private static TreeCache buildTreeCache(CuratorFramework client) throws Exception {
        TreeCache cache = new TreeCache(client, PATH);
        TreeCacheListener listener = (client1, event) ->
                System.out.println("Type: " + event.getType() +
                        " | Path: " + (null != event.getData() ? event.getData().getPath() : null) +
                        " | Data: " + (null != event.getData() ? new String(event.getData().getData()) : null));
        cache.getListenable().addListener(listener);
        cache.start();
        return cache;
    }

    private static void simulationNodeOperation(CuratorFramework client) throws Exception {
        client.setData().forPath(PATH, "01".getBytes());
        Thread.sleep(10);
        client.setData().forPath(PATH, "02".getBytes());
        Thread.sleep(10);
        client.delete().deletingChildrenIfNeeded().forPath(PATH);
        Thread.sleep(1000 * 2);
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = Connection.getConnection();

        client.create().creatingParentsIfNeeded().forPath(PATH);
        TreeCache cache = buildTreeCache(client);

        try {
            simulationNodeOperation(client);
        } finally {
            CloseableUtils.closeQuietly(cache);
            CloseableUtils.closeQuietly(client);
        }
        System.out.println("OK!");
    }
}
