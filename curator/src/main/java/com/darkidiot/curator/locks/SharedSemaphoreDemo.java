package com.darkidiot.curator.locks;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.utils.CloseableUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/10
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc: @see <a href="http://www.jianshu.com/p/70151fc0ef5d">source document</a>
 */
public class SharedSemaphoreDemo {

    private static final int MAX_LEASE = 10;
    private static final String PATH = "/locks";

    private static InterProcessSemaphoreV2 semaphore;

    public static void main(String[] args) throws Exception {
        FakeLimitedResource resource = new FakeLimitedResource();
        CuratorFramework client = Connection.getConnection();

        // init semaphore
        semaphore = new InterProcessSemaphoreV2(client, PATH, MAX_LEASE);

        try {
            Collection<Lease> leases = semaphore.acquire(5);
            resource.simulationShareResource(5);
            System.out.println("get " + leases.size() + " leases");

            Lease lease = semaphore.acquire();
            resource.simulationShareResource(1);
            System.out.println("get another lease");

            System.out.println("return one lease");
            semaphore.returnLease(lease);

            Collection<Lease> leases2 = semaphore.acquire(5, 10, TimeUnit.SECONDS);
            resource.simulationShareResource(5);
            System.out.println("Should timeout and acquire return " + leases2);

            System.out.println("return another 5 leases");
            semaphore.returnAll(leases);
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
