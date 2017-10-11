package com.darkidiot.curator.queues;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.DistributedPriorityQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.CloseableUtils;

/**
 * Copyright (c) for darkidiot
 * Date:2017/10/10
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * Desc: @see <a href="http://www.jianshu.com/p/70151fc0ef5d">source document</a>
 */
public class DistributedPriorityQueueDemo {

    private static final String PATH = "/queue";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = Connection.getConnection();
        DistributedPriorityQueue<String> queue = null;
        try {
            client.getCuratorListenable().addListener((client1, event) -> System.out.println("CuratorEvent: " + event.getType().name()));

            QueueConsumer<String> consumer = createQueueConsumer();
            QueueBuilder<String> builder = QueueBuilder.builder(client, consumer, createQueueSerializer(), PATH);
            queue = builder.buildPriorityQueue(0);
            queue.start();

            for (int i = 0; i < 10; i++) {
                int priority = (int) (Math.random() * 100);
                System.out.println("test-" + i + " priority:" + priority);
                queue.put("test-" + i, priority);
                Thread.sleep((long) (50 * Math.random()));
            }

            Thread.sleep(10 * 1000);

        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
        }
    }

    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>() {

            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }

        };
    }

    private static QueueConsumer<String> createQueueConsumer() {
        return new QueueConsumer<String>() {

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("connection new state: " + newState.name());
            }

            @Override
            public void consumeMessage(String message) throws Exception {
                Thread.sleep(1000);
                System.out.println("consume one message: " + message);
            }
        };
    }
}
