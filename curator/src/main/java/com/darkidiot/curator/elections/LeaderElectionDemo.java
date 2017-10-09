package com.darkidiot.curator.elections;

import com.darkidiot.curator.common.Connection;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/9
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc:
 */
public class LeaderElectionDemo {

    private final static String PATH = "/leader";
    private static final int CLIENT_QTY = 10;


    private static LeaderSelectorAdapter simulationClient(CuratorFramework client, String clientId) throws IOException {
        LeaderSelectorAdapter selectorAdapter = new LeaderSelectorAdapter(client, PATH, clientId);
        selectorAdapter.start();
        return selectorAdapter;
    }

    public static void main(String[] args) throws Exception {
        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderSelectorAdapter> examples = Lists.newArrayList();
        try {
            for (int i = 0; i < CLIENT_QTY; i++) {
                CuratorFramework client = Connection.getConnection();
                clients.add(client);
                String clientId = "Client #" + i;
                examples.add(simulationClient(client, clientId));
            }
            System.out.println("Press enter \\n return to quit\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {
            System.out.println("Shutting down...");
            examples.forEach(CloseableUtils::closeQuietly);
            clients.forEach(CloseableUtils::closeQuietly);
        }
    }


    private static class LeaderSelectorAdapter extends LeaderSelectorListenerAdapter implements Closeable {
        private final String name;
        private final LeaderSelector leaderSelector;
        private final AtomicInteger leaderCount = new AtomicInteger();

        LeaderSelectorAdapter(CuratorFramework client, String path, String name) {
            this.name = name;
            leaderSelector = new LeaderSelector(client, path, this);
            leaderSelector.autoRequeue();
        }

        public void start() throws IOException {
            leaderSelector.start();
        }

        @Override
        public void close() throws IOException {
            leaderSelector.close();
        }

        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
            final int waitSeconds = (int) (5 * Math.random()) + 1;
            System.out.println(name + " is now the leader. Waiting " + waitSeconds + " seconds...");
            System.out.println(name + " has been leader " + leaderCount.getAndIncrement() + " time(s) before.");
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
            } catch (InterruptedException e) {
                System.err.println(name + " was interrupted.");
                Thread.currentThread().interrupt();
            } finally {
                System.out.println(name + " relinquishing leadership.\n");
            }
        }
    }

}

