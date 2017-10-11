package com.darkidiot.curator.caches;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.utils.CloseableUtils;

/**
 * Copyright (c) for darkidiot
 * Date:2017/9/30
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc: @see <a href="http://www.jianshu.com/p/70151fc0ef5d">source document</a>
 */
public class NodeCacheDemo {
    private static final String PATH = "/nodeCache";

    private static NodeCache buildNodeCache(final CuratorFramework client) throws Exception {

        NodeCache cache = new NodeCache(client, PATH);
        NodeCacheListener listener = () -> {
            ChildData data = cache.getCurrentData();
            String str;
            if (null != data) {
                str = "Node: " + new String(cache.getCurrentData().getData());
            } else {
                str = "node had been deleted.";
            }
            System.out.println(str);
        };
        cache.getListenable().addListener(listener);
        cache.start();
        return cache;
    }

    private static void simulationNodeOperation(CuratorFramework client) throws Exception {
        // set data
        client.setData().forPath(PATH, "01".getBytes());
        Thread.sleep(10);

        client.setData().forPath(PATH, "02".getBytes()); // node change trigger event.
        Thread.sleep(10);

        // delete node
        client.delete().deletingChildrenIfNeeded().forPath(PATH);
        Thread.sleep(10);
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = Connection.getConnection();

        client.create().creatingParentsIfNeeded().forPath(PATH);
        NodeCache cache = buildNodeCache(client);
        try {
            simulationNodeOperation(client);
            Thread.sleep(1000 * 2);
        } finally {
            CloseableUtils.closeQuietly(cache);
            CloseableUtils.closeQuietly(client);
        }
        System.out.println("OK!");
    }
}

