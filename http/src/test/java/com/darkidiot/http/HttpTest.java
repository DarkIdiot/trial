package com.darkidiot.http;

import com.darkidiot.http.constant.*;
import com.darkidiot.http.model.Client;
import com.darkidiot.http.model.Request;
import com.darkidiot.http.model.Response;
import com.darkidiot.http.util.MessageDigestUtil;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http测试类
 * Date:2017/6/29
 * Author: 2016年 <a href="heqiao2@gome.com.cn">darkidiot</a>
 * Desc:
 */
public class HttpTest {

    //APP KEY
    private final static String APP_KEY = "23557637";
    // APP密钥
    private final static String APP_SECRET = "7f1cadd346f633ae013c572a71b39123";
    //API域名
    private final static String HOST = "ali-weather.showapi.com";
    //自定义参与签名Header前缀（可选,默认只有"X-Ca-"开头的参与到Header签名）
    private final static List<String> CUSTOM_HEADERS_TO_SIGN_PREFIX = new ArrayList<String>();

    private final static Gson gson = new Gson();

    /**
     * HTTP GET
     *
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
    }

    /**
     * HTTP POST 表单
     *
     * @throws Exception
     */
    @Test
    public void postForm() throws Exception {
    }

    /**
     * HTTP POST 字符串
     *
     * @throws Exception
     */
    @Test
    public void postString() throws Exception {
    }

    /**
     * HTTP POST 字节数组
     *
     * @throws Exception
     */
    @Test
    public void postBytes() throws Exception {
    }

    /**
     * HTTP PUT 字符串
     *
     * @throws Exception
     */
    @Test
    public void putString() throws Exception {
    }

    /**
     * HTTP PUT 字节数组
     *
     * @throws Exception
     */
    @Test
    public void putBytesBody() throws Exception {
    }

    /**
     * HTTP DELETE
     *
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
    }

}
