package com.darkidiot.http;

import com.darkidiot.http.constant.*;
import com.darkidiot.http.model.Client;
import com.darkidiot.http.model.Request;
import com.darkidiot.http.model.Response;
import com.darkidiot.http.util.MessageDigestUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https测试类
 * Date:2017/6/29
 * Author: 2016年 <a href="heqiao2@gome.com.cn">darkidiot</a>
 * Desc:
 */
@Slf4j
public class HttpsTest {
    //APP KEY
    private final static String APP_KEY = "23557637";
    // APP密钥
    private final static String APP_SECRET = "7f1cadd346f633ae013c572a71b39123";
    //API域名
    private final static String HOST = "ali-weather.showapi.com";
    //自定义参与签名Header前缀（可选,默认只有"X-Ca-"开头的参与到Header签名）
    private final static List<String> CUSTOM_HEADERS_TO_SIGN_PREFIX = new ArrayList<>();

    private final static Gson gson = new Gson();
    /**
     * HTTP GET
     *
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        //请求path
        String path = "/area-to-weather";

        Map<String, String> headers = new HashMap<>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        headers.put("a-header1", "header1Value");
        headers.put("b-header2", "header2Value");

        CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header1");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header2");

        Request request = new Request(Method.GET, HttpSchema.HTTP + HOST, path, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);

        //请求的query
        Map<String, String> querys = new HashMap<>();
        querys.put("a-query1", "query1Value");
        querys.put("b-query2", "query2Value");
        querys.put("areaid", "101291401");
        request.setQuerys(querys);

        //调用服务端
        Response response = Client.execute(request);

        log.info(gson.toJson(response));
    }

    /**
     * HTTP POST 表单
     *
     * @throws Exception
     */
    @Test
    public void postForm() throws Exception {
        //请求path
        String path = "/postform";

        Map<String, String> headers = new HashMap<>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        headers.put("a-header1", "header1Value");
        headers.put("b-header2", "header2Value");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header1");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header2");

        Request request = new Request(Method.POST_FORM, HttpSchema.HTTP + HOST, path, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);

        //请求的query
        Map<String, String> querys = new HashMap<>();
        querys.put("a-query1", "query1Value");
        querys.put("b-query2", "query2Value");
        request.setQuerys(querys);

        Map<String, String> bodys = new HashMap<>();
        bodys.put("a-body1", "body1Value");
        bodys.put("b-body2", "body2Value");
        request.setBodys(bodys);

        //调用服务端
        Response response = Client.execute(request);

        log.info(gson.toJson(response));
    }

    /**
     * HTTP POST 字符串
     *
     * @throws Exception
     */
    @Test
    public void postString() throws Exception {
        //请求path
        String path = "/poststring";
        //Body内容
        String body = "demo string body content";

        Map<String, String> headers = new HashMap<>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        //（可选）Body MD5,服务端会校验Body内容是否被篡改,建议Body非Form表单时添加此Header
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(body));
        //（POST/PUT请求必选）请求Body内容格式
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_TEXT);

        headers.put("a-header1", "header1Value");
        headers.put("b-header2", "header2Value");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header1");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header2");


        Request request = new Request(Method.POST_STRING, HttpSchema.HTTP + HOST, path, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);

        //请求的query
        Map<String, String> querys = new HashMap<>();
        querys.put("a-query1", "query1Value");
        querys.put("b-query2", "query2Value");
        request.setQuerys(querys);

        request.setStringBody(body);

        //调用服务端
        Response response = Client.execute(request);

        log.info(gson.toJson(response));
    }

    /**
     * HTTP POST 字节数组
     *
     * @throws Exception
     */
    @Test
    public void postBytes() throws Exception {
        //请求path
        String path = "/poststream";
        //Body内容
        byte[] bytesBody = "demo bytes body content".getBytes(Constants.ENCODING);

        Map<String, String> headers = new HashMap<>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        //（可选）Body MD5,服务端会校验Body内容是否被篡改,建议Body非Form表单时添加此Header
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(bytesBody));
        //（POST/PUT请求必选）请求Body内容格式
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_TEXT);

        headers.put("a-header1", "header1Value");
        headers.put("b-header2", "header2Value");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header1");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header2");

        Request request = new Request(Method.POST_BYTES, HttpSchema.HTTP + HOST, path, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);

        //请求的query
        Map<String, String> querys;
        querys = new HashMap<>();
        querys.put("a-query1", "query1Value");
        querys.put("b-query2", "query2Value");
        request.setQuerys(querys);

        request.setBytesBody(bytesBody);

        //调用服务端
        Response response = Client.execute(request);

        log.info(gson.toJson(response));
    }

    /**
     * HTTP PUT 字符串
     *
     * @throws Exception
     */
    @Test
    public void putString() throws Exception {
        //请求path
        String path = "/putstring";
        //Body内容
        String body = "demo string body content";

        Map<String, String> headers = new HashMap<>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        //（可选）Body MD5,服务端会校验Body内容是否被篡改,建议Body非Form表单时添加此Header
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(body));
        //（POST/PUT请求必选）请求Body内容格式
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_TEXT);

        headers.put("a-header1", "header1Value");
        headers.put("b-header2", "header2Value");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header1");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header2");

        Request request = new Request(Method.POST_STRING, HttpSchema.HTTP + HOST, path, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
        request.setStringBody(body);

        //调用服务端
        Response response = Client.execute(request);

        log.info(gson.toJson(response));
    }

    /**
     * HTTP PUT 字节数组
     *
     * @throws Exception
     */
    @Test
    public void putBytesBody() throws Exception {
        //请求path
        String path = "/putstream";
        //Body内容
        byte[] bytesBody = "demo bytes body content".getBytes(Constants.ENCODING);

        Map<String, String> headers = new HashMap<>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        //（可选）Body MD5,服务端会校验Body内容是否被篡改,建议Body非Form表单时添加此Header
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(bytesBody));
        //（POST/PUT请求必选）请求Body内容格式
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_TEXT);
        headers.put("a-header1", "header1Value");
        headers.put("b-header2", "header2Value");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header1");
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("a-header2");

        Request request = new Request(Method.PUT_BYTES, HttpSchema.HTTP + HOST, path, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
        request.setBytesBody(bytesBody);

        //调用服务端
        Response response = Client.execute(request);

        log.info(gson.toJson(response));
    }

    /**
     * HTTP DELETE
     *
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        //请求path
        String path = "/delete";

        Map<String, String> headers = new HashMap<>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");

        Request request = new Request(Method.DELETE, HttpSchema.HTTP + HOST, path, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);

        //调用服务端
        Response response = Client.execute(request);

        log.info(gson.toJson(response));
    }

}
