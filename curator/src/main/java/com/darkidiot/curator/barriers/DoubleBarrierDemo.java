package com.darkidiot.curator.barriers;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc: @see <a href="http://www.jianshu.com/p/70151fc0ef5d">source document</a>
 */
public class DoubleBarrierDemo {
    private static final int QTY = 5;
    private static final String PATH = "/barrier";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = Connection.getConnection();
        try {
            ExecutorService service = Executors.newFixedThreadPool(QTY);
            for (int i = 0; i < QTY; ++i) {
                final DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, PATH, QTY);
                final int index = i;
                Callable<Void> task = () -> {

                    Thread.sleep((long) (3 * Math.random()));
                    System.out.println("Client #" + index + " enters");
                    barrier.enter();
                    System.out.println("Client #" + index + " begins");
                    Thread.sleep((long) (3000 * Math.random()));
                    barrier.leave();
                    System.out.println("Client #" + index + " left");
                    return null;
                };
                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
