package com.darkidiot.curator.queues;

import com.darkidiot.curator.common.Connection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
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
public class DistributedQueueDemo {
    private static final String PATH = "/queue";

    public static void main(String[] args) throws Exception {
        CuratorFramework clientA = Connection.getConnection();
        CuratorFramework clientB = Connection.getConnection();

        QueueBuilder<String> builderA = QueueBuilder.builder(clientA, createQueueConsumer("A"), createQueueSerializer(), PATH);
        DistributedQueue<String> queueA = builderA.buildQueue();
        queueA.start();

        DistributedQueue<String> queueB;
        QueueBuilder<String> builderB = QueueBuilder.builder(clientB, createQueueConsumer("B"), createQueueSerializer(), PATH);
        queueB = builderB.buildQueue();
        queueB.start();
        for (int i = 0; i < 20; i++) {
            queueA.put(" test-A-" + i);
            Thread.sleep(10);
            queueB.put(" test-B-" + i);
        }
        Thread.sleep(1000 * 10);// 等待消息消费完成
        CloseableUtils.closeQuietly(queueB);
        CloseableUtils.closeQuietly(queueA);
        CloseableUtils.closeQuietly(clientB);
        CloseableUtils.closeQuietly(clientA);
        System.out.println("OK!");
    }

    /**
     * 队列消息序列化实现类
     */
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

    /**
     * 定义队列消费者
     */
    private static QueueConsumer<String> createQueueConsumer(final String name) {
        return new QueueConsumer<String>() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("connection state changed to -> " + newState.name());
            }

            @Override
            public void consumeMessage(String message) throws Exception {
                System.out.println("consume msg(" + name + "): " + message);
            }
        };
    }
}
