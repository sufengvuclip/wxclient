package com.sf.wxc.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HttpClientUtil {
    private static PoolingHttpClientConnectionManager cm;
    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";
    final static int BUFFER_SIZE = 1024;
    final static String androidUA = "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; MI 3W Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.0.0.488 U3/0.8.0 Mobile Safari/534.30";
    final static String PCUA = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";

    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(50);// 整个连接池最大连接数
            cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    private static HashMap<String, CloseableHttpClient> loginClientsMap = new HashMap<>();

    /**
     * get client with login session.
     * @param loginInfo
     * @return
     */
    private static CloseableHttpClient getHttpClient(JSONObject loginInfo) {
        CloseableHttpClient ret = null;
        String key = loginInfo.getString("key");
        ret = loginClientsMap.get(key);
        if(ret == null){
            //create new client instance
            ret = HttpClients.createDefault();
            JSONObject formData = loginInfo.getJSONObject("formdata");

        }

        return ret;
    }


    public static String httpGetRequest(String url) {
        return httpGetRequest(url,true,null);
    }
    /**
     *
     * @param url
     * @return
     */
    public static String httpGetRequest(String url, boolean mobileUA, JSONObject loginInfo) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet,mobileUA, loginInfo);
    }

    public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {
        return httpGetRequest(url,params,true, null);
    }
    public static String httpGetRequest(String url, Map<String, Object> params, boolean mobileUA, JSONObject loginInfo) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet, mobileUA,loginInfo);
    }


    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        return httpGetRequest(url,headers,params,true, null);
    }
    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params, boolean mobileUA, JSONObject loginInfo)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet, mobileUA, loginInfo);
    }

    public static String httpPostRequest(String url) {
        return httpPostRequest(url,true, null);
    }

    public static String httpPostRequest(String url, boolean mobileUA, JSONObject loginInfo) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost,mobileUA, loginInfo);
    }

    public static String uploadFile(String url, String filepath, Map<String, String> params) {
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httpPost = null;
        String ret = null;
        try {
            httpPost = new HttpPost(url);
            File file = new File(filepath);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //others param for request
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = params.get(key);
                StringBody valueBody = new StringBody(value, ContentType.create("text/plain", UTF_8));
                builder.addPart(key, valueBody);
            }

            //file param for request
            String fileRequestParam = "media";
            FileBody fileBody = new FileBody(file, ContentType.create("multipart/form-data", UTF_8));
            builder.addPart(fileRequestParam, fileBody);

            HttpEntity reqEntity = builder.build();
            httpPost.setEntity(reqEntity);
            System.out.println("executing request " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpPost);

            System.out.println(response.getStatusLine());
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                System.out.println("Response content length: " + resEntity.getContentLength());
            }
            ret = IOUtils.toString(resEntity.getContent(), UTF_8);
            System.out.println("Response : "+ret);
            EntityUtils.consume(resEntity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return ret;
    }

    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        return httpPostRequest(url,params,true,null);
    }
    public static String httpPostRequest(String url, Map<String, Object> params, boolean mobileUA, JSONObject loginInfo) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost,mobileUA, loginInfo);
    }

    public static String httpPostRequest(String url, String body) throws UnsupportedEncodingException {
        return httpPostRequest(url,body,null,true,null);
    }
    public static String httpPostRequest(String url, String body, Map<String, Object> headers, boolean mobileUA, JSONObject loginInfo) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        if(headers!=null) {
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        httpPost.setEntity(new StringEntity(body, UTF_8));
        return getResult(httpPost, mobileUA,loginInfo);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params) throws UnsupportedEncodingException
    {
        return httpPostRequest(url,headers,params,true,null);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params, boolean mobileUA, JSONObject loginInfo)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost,mobileUA,loginInfo);
    }


    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }

        return pairs;
    }

    /**
     * 处理Http请求
     *
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request, boolean mobileUA, JSONObject loginInfo) {
        if(mobileUA)
            request.setHeader("User-Agent",androidUA);
        else
            request.setHeader("User-Agent",PCUA);

        CloseableHttpClient httpClient = null;
        if(loginInfo!=null){
            httpClient = getHttpClient(loginInfo);//only get the client with login session.
        }else
        {
            httpClient = HttpClients.createDefault();
        }

//        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            System.out.println("response code: "+response.getStatusLine().getStatusCode());;
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity);
                response.close();
                // httpClient.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return EMPTY_STR;
    }

    public static String httpGetRedirectFinalUrl(String url){
        return httpGetRedirectFinalUrl(url,true,null);
    }
    public static String httpGetRedirectFinalUrl(String url, boolean mobileUA, JSONObject loginInfo) {
        String ret = null;
        HttpGet httpGet = new HttpGet(url);
        if(mobileUA)
            httpGet.setHeader("User-Agent",androidUA);
        else
            httpGet.setHeader("User-Agent",PCUA);
//        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpContext context = new BasicHttpContext();
            CloseableHttpResponse response = httpClient.execute(httpGet,context);
            HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            HttpHost currentHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                ret = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static String download(String url, String filepath) {
        String ret = filepath;
        try {
            CloseableHttpClient client = getHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);

            HttpEntity entity = response.getEntity();
            org.apache.http.Header contentType = response.getFirstHeader("Content-Type");
            if(StringUtils.trimToEmpty(contentType.getValue()).contains("gif")){
                filepath = FilenameUtils.getFullPath(filepath)+ FilenameUtils.getBaseName(filepath)+".gif";
            }else if(StringUtils.trimToEmpty(contentType.getValue()).contains("png")){
                filepath = FilenameUtils.getFullPath(filepath)+ FilenameUtils.getBaseName(filepath)+".png";
            }else if(StringUtils.trimToEmpty(contentType.getValue()).contains("jpeg")){
                filepath = FilenameUtils.getFullPath(filepath)+ FilenameUtils.getBaseName(filepath)+".jpeg";
            }else if(StringUtils.trimToEmpty(contentType.getValue()).contains("bmp")){
                filepath = FilenameUtils.getFullPath(filepath)+ FilenameUtils.getBaseName(filepath)+".bmp";
            }
            InputStream is = entity.getContent();
            File file = new File(filepath);
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] buffer=new byte[BUFFER_SIZE];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer,0,ch);
            }
            is.close();
            fileout.flush();
            fileout.close();
            ret = filepath;
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }
        return ret;
    }

/*    public static void main(String[] args) {
        System.out.println(httpGetRequest("http://m.toutiao.com/i6365076442560070146/info"));
    }*/

}