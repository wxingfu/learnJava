package com.weixf.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@Slf4j
public class HttpUtils {

    // 请求超时时间
    private final static Integer TIME_OUT = 1000000;
    // http连接池
    private static volatile PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    // 请求配置
    private static RequestConfig requestConfig;

    public static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        if (poolingHttpClientConnectionManager == null) {
            synchronized (HttpUtils.class) {
                if (poolingHttpClientConnectionManager == null) {
                    poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
                    // 连接池最大连接数
                    poolingHttpClientConnectionManager.setMaxTotal(1024);
                    // 每个路由最大连接数
                    poolingHttpClientConnectionManager.setDefaultMaxPerRoute(32);
                    // 配置请求的超时设置
                    requestConfig = RequestConfig.custom()
                            .setConnectionRequestTimeout(TIME_OUT)
                            .setConnectTimeout(TIME_OUT)
                            .setSocketTimeout(TIME_OUT)
                            .build();
                }
            }
        }
        return poolingHttpClientConnectionManager;
    }


    public static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(getPoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * 请求发送执行
     *
     * @param httpMethod 请求方法
     * @return 响应
     */
    public static String registerRequest(HttpUriRequest httpMethod) {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpMethod, HttpClientContext.create());
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.error("响应失败,statusCode: " + statusCode);
                httpMethod.abort();
            }
            log.debug("响应成功,statusCode: " + statusCode);
            String response = EntityUtils.toString(httpResponse.getEntity(), Consts.UTF_8);
            log.info("响应成功,response: " + response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("接口请求失败", e);
        }
    }

    /**
     * 参数处理
     *
     * @param params 参数
     * @return 处理后的参数
     */
    public static List<BasicNameValuePair> toPairs(Map<String, Object> params) {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairs.add(pair);
            }
        }
        return pairs;
    }

    /**
     * get url请求
     *
     * @param url    请求地址
     * @param params 参数
     * @return 响应
     */
    public static String get(String url, Map<String, Object> params) {
        HttpGet request = new HttpGet();
        try {
            String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(toPairs(params), Consts.UTF_8));
            request.setURI(new URI(url.concat("?").concat(paramsStr)));
            return registerRequest(request);
        } catch (Exception e) {
            log.error("请求失败", e);
        }
        return null;
    }

    /**
     * POST URL方式提交
     *
     * @param url    请求url
     * @param params 请求参数
     * @return 响应
     */
    public static String postFromUrl(String url, Map<String, Object> params) {
        HttpPost request = new HttpPost(url);
        request.setEntity(new UrlEncodedFormEntity(toPairs(params), Consts.UTF_8));
        try {
            return registerRequest(request);
        } catch (Exception e) {
            log.error("请求失败", e);
        }
        return null;
    }

    /**
     * POST JSON方式提交
     *
     * @param url    请求url
     * @param params json串
     * @return 响应
     */
    public static String postFromJson(String url, String params) {
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-type", "application/json");
        request.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
        try {
            return registerRequest(request);
        } catch (Exception e) {
            log.error("请求失败", e);
        }
        return null;
    }


    public static Map<String, Object> getParamMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class<?> classObj = obj.getClass();
        Field[] fields = classObj.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            Object o = invokeGetMethod(field.getName(), obj);
            if (o != null && !o.equals("")) {
                map.put(field.getName(), o);
            }
        }
        return map;
    }

    public static Object invokeGetMethod(String fieldName, Object object) {
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, object.getClass());
            Method method = pd.getReadMethod();
            return method.invoke(object);
        } catch (Exception e) {
            return null;
        }
    }
}

