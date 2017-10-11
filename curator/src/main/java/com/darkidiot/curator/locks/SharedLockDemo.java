package com.darkidiot.curator.locks;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/9
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc: @see <a href="http://www.jianshu.com/p/70151fc0ef5d">source document</a>
 */
public class SharedLockDemo {


    private static class ShareLock {
        private InterProcessSemaphoreMutex lock;
        private final String clientName;

        public ShareLock(CuratorFramework client, String lockPath, String clientName) {
            this.clientName = clientName;
            this.lock = new InterProcessSemaphoreMutex(client, lockPath);
        }

        // not support reentrant
        private void lock(long time, TimeUnit unit) throws Exception {
            if (!lock.acquire(time, unit)) {
                throw new IllegalStateException(clientName + " 不能得到互斥锁");
            }
            System.out.println(clientName + " acquired the lock.");
        }

        private void unlock() throws Exception {
            System.out.println(clientName + " releasing the lock.");
            lock.release(); // always release the lock in a finally block
        }

    }


    private static void doWork(String clientName, FakeLimitedResource resource) throws Exception {
        System.out.println(clientName + " get the lock");
        resource.simulationExclusiveResource(); //access resource exclusively
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
                    final ShareLock lock = new ShareLock(client, PATH, clientName);
                    for (int j = 0; j < REPETITIONS; ++j) {
                        try {
                            lock.lock(5, TimeUnit.SECONDS);
                            doWork(clientName, resource);
                        } finally {
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
}
