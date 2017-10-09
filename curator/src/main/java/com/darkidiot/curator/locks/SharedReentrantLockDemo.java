package com.darkidiot.curator.locks;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.utils.CloseableUtils;

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
public class SharedReentrantLockDemo {

    private static class SharedReentrantLock {

        private InterProcessMutex lock;
        private final String clientName;

        private SharedReentrantLock(CuratorFramework client, String lockPath, String clientName) {
            this.clientName = clientName;
            this.lock = new InterProcessMutex(client, lockPath);
        }

        private void lock(long time, TimeUnit unit) throws Exception {
            if (!lock.acquire(time, unit)) {
                throw new IllegalStateException(clientName + " could not acquire the lock");
            }
        }

        private void unlock() throws Exception {
            lock.release(); // always release the lock in a finally block
        }
    }

    private static final int QTY = 5;
    private static final int REPETITIONS = QTY * 5;
    private static final String PATH = "/locks";

    public static void main(String[] args) throws Exception {
        final FakeLimitedResource resource = new FakeLimitedResource();
        ExecutorService service = Executors.newFixedThreadPool(QTY);
        for (int i = 0; i < QTY; ++i) {
            final int index = i;
            Callable<Void> task = () -> {
                CuratorFramework client = Connection.getConnection();
                try {
                    String clientName = "Client#" + index;
                    final SharedReentrantLock lock = new SharedReentrantLock(client, PATH, clientName);
                    for (int j = 0; j < REPETITIONS; ++j) {
                        try {
                            lock.lock(10,TimeUnit.SECONDS);
                            doWork(clientName, resource);
                        }finally {
                            lock.unlock();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    CloseableUtils.closeQuietly(client);
                }
                return null;
            };
            service.submit(task);
        }
        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
    }


    private static void doWork(String clientName, FakeLimitedResource resource) throws Exception {
        System.out.println(clientName + " get the lock");
        resource.use(); //access resource exclusively
    }


}
