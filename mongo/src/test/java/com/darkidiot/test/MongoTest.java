package com.darkidiot.test;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * mongo 原生测试类
 * Copyright (c) for darkidiot
 * Date:2017/4/11
 * Author: <a href="darkidiot@icloud.com">darkidiot</a>
 * School: CUIT
 * Desc:
 */
@Slf4j
public class MongoTest {

    private static MongoDatabase mongoDatabase;
    private String COLLECTION_NAME = "test";

    @BeforeClass
    public static void setUp() {
        // 连接到 mongodb 服务
        ServerAddress serverAddress = new ServerAddress("60.211.179.34", 27017);
        MongoCredential mongoCredential = MongoCredential.createMongoCRCredential("admin", "newxcloud", "Mongo@2015".toCharArray());

        MongoClient mongoClient = new MongoClient(serverAddress, Lists.newArrayList(mongoCredential));

        // 连接到数据库
        mongoDatabase = mongoClient.getDatabase("newxcloud");
        log.info("connect to database successfully");
    }

    @AfterClass
    public static void tearDown() {
    }

    @Test
    public void testConnectNoAuth() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("60.211.179.34", 27017);

            // 连接到数据库
            mongoDatabase = mongoClient.getDatabase("test");
            log.info("connect to database successfully");
        } catch (Exception e) {
            log.error("connect error, cause by {}.", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testConnectAuth() {
        try {
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            //ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress("60.211.179.34", 27017);
            List<ServerAddress> addresses = Lists.newArrayList();
            addresses.add(serverAddress);

            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
            MongoCredential credential = MongoCredential.createScramSha1Credential("admin", COLLECTION_NAME, "darkidiot".toCharArray());
            List<MongoCredential> credentials = Lists.newArrayList();
            credentials.add(credential);

            //通过连接认证获取MongoDB连接
            MongoClient mongoClient = new MongoClient(addresses, credentials);

            //连接到数据库
            mongoDatabase = mongoClient.getDatabase(COLLECTION_NAME);
            log.info("Connect to database successfully");
        } catch (Exception e) {
            log.error("connect error, cause by {}.", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testCreateCollection() {
        try {
            mongoDatabase.createCollection(COLLECTION_NAME);
            log.info("create collection successfully");
        } catch (Exception e) {
            log.error("create collection error, cause by {}.", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testSelectCollection() {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_NAME);
            log.info("select collection successfully.");
        } catch (Exception e) {
            log.error("select collection error, cause by {}.", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testSaveDocument() {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_NAME);
        /*
         * 1. 创建文档 org.bson.Document 参数为key-value的格式
         * 2. 创建文档集合List<Document>
         * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)
         **/
            List<Document> documents = Lists.newArrayList();
            Document document = new Document("title", "MongoDB").
                    append("description", "database").
                    append("likes", 100).
                    append("by", "darkidiot");
            documents.add(document);
            collection.insertMany(documents);
            log.info("save document successfully.");
        } catch (Exception e) {
            log.error("save document error, cause by {}.", Throwables.getStackTraceAsString(e));
        }

    }

    @Test
    public void testFindDocument() {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_NAME);
            FindIterable<Document> documents = collection.find();
            for (Document next : documents) {
                log.info("document:{}", new Gson().toJsonTree(next));
            }
        } catch (Exception e) {
            log.error("find documents error, cause by {}.", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testUpdateCollection() {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_NAME);

            //更新文档   将文档中likes=100的文档修改为likes=200
            collection.updateMany(Filters.eq("likes", 100), new Document("$set", new Document("likes", 200)));
            log.info("update collection successfully.");
        } catch (Exception e) {
            log.error("update collection error, cause by {}.", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testDeleteOne() {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_NAME);

            //删除符合条件的第一个文档
            collection.deleteOne(Filters.eq("likes", 200));
            log.info("delete one document successfully.");
        } catch (Exception e) {
            log.error("delete one document error, cause by {}.", Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testDeleteMany() {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_NAME);

            //删除所有符合条件的文档
            collection.deleteMany(Filters.eq("likes", 200));
            log.info("delete document successfully.");
        } catch (Exception e) {
            log.error("delete document error, cause by {}.", Throwables.getStackTraceAsString(e));
        }
    }

}
