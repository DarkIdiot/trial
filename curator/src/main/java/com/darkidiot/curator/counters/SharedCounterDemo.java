package com.darkidiot.curator.counters;

import com.darkidiot.curator.common.Connection;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.SharedCountListener;
import org.apache.curator.framework.recipes.shared.SharedCountReader;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.CloseableUtils;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) for darkidiot
 * Date:2017/9/30
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc:
 */
public class SharedCounterDemo {
    private static final int QTY = 5;
    private static final String PATH = "/counter";

    public static void main(String[] args) throws IOException, Exception {
        final Random random = new Random();
        SharedCountListener listener = new SharedCountListener() {

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState state) {
                System.out.println("State changed: " + state.toString());
            }

            @Override
            public void countHasChanged(SharedCountReader sharedCount, int newCount) throws Exception {
                System.out.println("Counter's value is changed to " + newCount);
            }
        };
        CuratorFramework client = Connection.getConnection();

        SharedCount baseCount = new SharedCount(client, PATH, 0);
        baseCount.addListener(listener);
        baseCount.start();
        try {
            List<SharedCount> counts = Lists.newArrayList();
            ExecutorService service = Executors.newFixedThreadPool(QTY);
            for (int i = 0; i < QTY; ++i) {
                final SharedCount count = new SharedCount(client, PATH, 0);
                counts.add(count);
                Callable<Void> task = () -> {
                    count.start();
                    Thread.sleep(random.nextInt(10000));
                    System.out.println("Increment:" + count.trySetCount(count.getVersionedValue(), count.getCount() + random.nextInt(10)));
                    return null;
                };
                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);

            for (int i = 0; i < QTY; ++i) {
                CloseableUtils.closeQuietly(counts.get(i));
            }
        } finally {
            CloseableUtils.closeQuietly(baseCount);
            CloseableUtils.closeQuietly(client);

        }
        System.out.println("OK");
    }
}
