package com.darkidiot.curator.locks;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.utils.CloseableUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/10
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc: @see <a href="http://www.jianshu.com/p/70151fc0ef5d">source document</a>
 */
public class MultiSharedLockDemo {

    private static final String PATH1 = "/locks1";
    private static final String PATH2 = "/locks2";

    public static void main(String[] args) throws Exception {
        FakeLimitedResource resource = new FakeLimitedResource();
        CuratorFramework client = Connection.getConnection();
        try {

            InterProcessLock lock1 = new InterProcessMutex(client, PATH1);
            InterProcessLock lock2 = new InterProcessSemaphoreMutex(client, PATH2);

            InterProcessMultiLock lock = new InterProcessMultiLock(Arrays.asList(lock1, lock2));

            if (!lock.acquire(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException("could not acquire the lock");
            }
            System.out.println("has got all lock");

            System.out.println("has got lock1: " + lock1.isAcquiredInThisProcess());
            System.out.println("has got lock2: " + lock2.isAcquiredInThisProcess());

            try {
                resource.simulationExclusiveResource(); //access resource exclusively
            } finally {
                System.out.println("releasing the lock");
                lock.release(); // always release the lock in a finally block
            }
            System.out.println("has got lock1: " + lock1.isAcquiredInThisProcess());
            System.out.println("has got lock2: " + lock2.isAcquiredInThisProcess());
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

}
