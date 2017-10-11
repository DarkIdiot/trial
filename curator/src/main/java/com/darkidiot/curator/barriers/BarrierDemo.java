package com.darkidiot.curator.barriers;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) for darkidiot
 * Date:2017/9/30
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc: @see <a href="http://www.jianshu.com/p/70151fc0ef5d">source document</a>
 */
public class BarrierDemo {
    private static final int QTY = 5;
    private static final String PATH = "/barrier";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = Connection.getConnection();
        try {
            ExecutorService service = Executors.newFixedThreadPool(QTY);
            DistributedBarrier controlBarrier = new DistributedBarrier(client, PATH);
            controlBarrier.setBarrier();

            for (int i = 0; i < QTY; ++i) {
                final DistributedBarrier barrier = new DistributedBarrier(client, PATH);
                final int index = i;
                Callable<Void> task = () -> {
                    Thread.sleep((long) (3 * Math.random()));
                    System.out.println("Client #" + index + " waits on Barrier");
                    barrier.waitOnBarrier();
                    System.out.println("Client #" + index + " begins");
                    return null;
                };
                service.submit(task);
            }
            Thread.sleep(10000);
            System.out.println("all Barrier instances should wait the condition");
            controlBarrier.removeBarrier();
            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
