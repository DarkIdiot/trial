package com.darkidiot.curator.queues;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.apache.curator.utils.CloseableUtils;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc:
 */
public class SimpleDistributedQueueDemo {
    private static final String PATH = "/example/queue";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = Connection.getConnection();
        SimpleDistributedQueue queue;
        try {
            client.getCuratorListenable().addListener((client1, event) -> System.out.println("CuratorEvent: " + event.getType().name()));
            queue = new SimpleDistributedQueue(client, PATH);
            Producer producer = new Producer(queue);
            Consumer consumer = new Consumer(queue);
            new Thread(producer, "producer").start();
            new Thread(consumer, "consumer").start();
            Thread.sleep(20000);
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    private static class Producer implements Runnable {

        private SimpleDistributedQueue queue;

        Producer(SimpleDistributedQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    boolean flag = queue.offer(("zjc-" + i).getBytes());
                    if (flag) {
                        System.out.println("producing msg success : " + "zjc-" + i);
                    } else {
                        System.out.println("producing msg failure : " + "zjc-" + i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Consumer implements Runnable {

        private SimpleDistributedQueue queue;

        Consumer(SimpleDistributedQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                System.out.println("consuming msg: " + new String(queue.take(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
