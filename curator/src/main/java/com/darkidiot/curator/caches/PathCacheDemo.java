package com.darkidiot.curator.caches;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.CloseableUtils;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/9
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc: @see <a href="http://www.jianshu.com/p/70151fc0ef5d">source document</a>
 */
public class PathCacheDemo {

    private static final String PATH = "/pathCache";

    private static PathChildrenCache buildPatchCache(CuratorFramework client) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start();
        PathChildrenCacheListener cacheListener = (client1, event) -> {
            String str = "Type: " + event.getType();
            if (null != event.getData()) {
                str += " | Data: " + event.getData().getPath() + " = " + new String(event.getData().getData());
            }
            System.out.println(str);
        };
        cache.getListenable().addListener(cacheListener);
        return cache;
    }

    /**
     * Thread.sleep(10)可以注释掉，但是注释后事件监听的触发次数会不全，这可能与PathCache的实现原理有关，不能太过频繁的触发事件！
     */
    private static void simulationPathOperation(CuratorFramework client) throws Exception {
        // create child
        client.create().creatingParentsIfNeeded().forPath(PATH + "/test01", "01".getBytes());
        Thread.sleep(10);
        client.create().creatingParentsIfNeeded().forPath(PATH + "/test02", "02".getBytes());
        Thread.sleep(10);

        // set data
        client.setData().forPath(PATH + "/test01", "01_V2".getBytes());
        Thread.sleep(10);

        // delete child
        client.delete().forPath(PATH + "/test01");
        Thread.sleep(10);
        client.delete().forPath(PATH + "/test02");
        Thread.sleep(10);
    }


    public static void main(String[] args) throws Exception {
        CuratorFramework client = Connection.getConnection();
        PathChildrenCache cache = buildPatchCache(client);
        try {
            // Asynchronized operation
            simulationPathOperation(client);

            for (ChildData data : cache.getCurrentData()) {
                System.out.println("getCurrentData:" + data.getPath() + " = " + new String(data.getData()));
            }
            Thread.sleep(1000 * 2);
        } finally {
            CloseableUtils.closeQuietly(cache);
            CloseableUtils.closeQuietly(client);
        }
        System.out.println("OK!");
    }
}
