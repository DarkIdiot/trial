package com.darkidiot.curator.locks;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
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
public class SharedReentrantReadWriteLockDemo {

    private enum Type {
        ReadLock, WriteLock
    }

    private static class SharedReentrantReadWriteLock {
        private final InterProcessReadWriteLock lock;
        private final InterProcessMutex readLock;
        private final InterProcessMutex writeLock;
        private final String clientName;

        SharedReentrantReadWriteLock(CuratorFramework client, String lockPath, String clientName) {
            this.clientName = clientName;
            lock = new InterProcessReadWriteLock(client, lockPath);
            readLock = lock.readLock();
            writeLock = lock.writeLock();
        }

        private void lock(long time, TimeUnit unit, Type type) throws Exception {
            if (Type.WriteLock == type) {
                if (!writeLock.acquire(time, unit)) {
                    throw new IllegalStateException(clientName + " can not acquire write lock.");
                }
                System.out.println(clientName + " acquired write lock.");
            }

            if (Type.ReadLock == type) {
                if (!readLock.acquire(time, unit)) {
                    throw new IllegalStateException(clientName + "  can not acquire read lock.");
                }
                System.out.println(clientName + " acquired read lock.");
            }
        }

        private void unlock(Type type) throws Exception {
            if (type == Type.WriteLock) {
                System.out.println(clientName + " releasing write lock.");
                writeLock.release();
            }
            if (type == Type.ReadLock) {
                System.out.println(clientName + " releasing read lock.");
                readLock.release();
            }

        }
    }

    private static void doWork(FakeLimitedResource.Operation operation, String clientName, FakeLimitedResource resource) throws Exception {
        System.out.println(clientName + " get the " + (operation == FakeLimitedResource.Operation.Read ? "read" : "write") + " lock.");
        resource.simulationDatabase(operation);
    }

    private static final int QTY = 5;
    private static final int REPETITIONS = QTY;
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
                    final SharedReentrantReadWriteLock lock = new SharedReentrantReadWriteLock(client, PATH, clientName);
                    for (int j = 0; j < REPETITIONS; ++j) {
                        try {
                            lock.lock(5, TimeUnit.SECONDS, Type.ReadLock);
                            doWork(FakeLimitedResource.Operation.Read, clientName, resource);
                        } finally {
                            lock.unlock(Type.ReadLock);
                        }
                        try {
                            lock.lock(5, TimeUnit.SECONDS, Type.WriteLock);
                            doWork(FakeLimitedResource.Operation.Write, clientName, resource);
                        } finally {
                            lock.unlock(Type.WriteLock);
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
