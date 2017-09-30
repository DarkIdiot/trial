package com.darkidiot.curator.elections.leaderLatch;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.utils.CloseableUtils;

/**
 * Copyright (c) for darkidiot
 * Date:2017/9/28
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc:
 */
public class LeaderLatchDemo {

    private static final String PATH = "/leader";

    private static void simulationClient(final CuratorFramework client, final String clientId, final Integer timeToCrash) {
        final LeaderLatch leaderLatch = new LeaderLatch(client, PATH, clientId);
        try {

            leaderLatch.addListener(new LeaderLatchListener() {
                @Override
                public void isLeader() {
                    System.out.println(leaderLatch.getId() + ":I am leader. I am doing jobs!");
                }

                @Override
                public void notLeader() {
                    System.out.println(leaderLatch.getId() + ":I am not leader. I will do nothing!");
                }
            });
            leaderLatch.start();
            Thread.sleep(timeToCrash);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
            CloseableUtils.closeQuietly(leaderLatch);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String id4client1 = "client#1";
        String id4client2 = "client#2";
        Thread thread1 = new Thread(() -> simulationClient(Connection.getConnection(), id4client1, 10000));
        Thread thread2 = new Thread(() -> simulationClient(Connection.getConnection(), id4client2, 5000));
        thread1.setName(id4client1);
        thread2.setName(id4client2);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

}
