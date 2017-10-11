package com.darkidiot.curator.counters;

import com.darkidiot.curator.common.Connection;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/10
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc: @see <a href="http://www.jianshu.com/p/70151fc0ef5d">source document</a>
 */
public class DistributedAtomicLongDemo {
    private static final int QTY = 5;
    private static final String PATH = "/counter";

    public static void main(String[] args) throws Exception {
        List<DistributedAtomicLong> examples = Lists.newArrayList();
        CuratorFramework client = Connection.getConnection();
        try {
            ExecutorService service = Executors.newFixedThreadPool(QTY);
            for (int i = 0; i < QTY; ++i) {
                final DistributedAtomicLong count = new DistributedAtomicLong(client, PATH, new RetryNTimes(10, 100));

                examples.add(count);
                Callable<Void> task = () -> {
                    try {
                        AtomicValue<Long> value = count.increment();
                        System.out.println("succeed: " + value.succeeded());
                        if (value.succeeded())
                            System.out.println("Increment: from " + value.preValue() + " to " + value.postValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                };
                service.submit(task);
            }
            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        } finally {
            CloseableUtils.closeQuietly(client);
        }
        System.out.println("OK.");
    }
}
