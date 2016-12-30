package com.sf.wxc.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static Cache<String, CloseableHttpClient> loginClientCache = CacheBuilder.newBuilder().expireAfterWrite(120, TimeUnit.MINUTES).build();
    final static int BUFFER_SIZE = 1024;
    final static String androidUA = "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; MI 3W Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.0.0.488 U3/0.8.0 Mobile Safari/534.30";
    final static String PCUA = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static PoolingHttpClientConnectionManager cm;
    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";
    //private static HashMap<String, CloseableHttpClient> loginClientsMap = new HashMap<>();

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

    private static CloseableHttpClient getHttpClient(JSONObject loginInfo) {
        return getHttpClient(loginInfo,true);
    }

    /**
     * get client with login session.
     *
     * @param loginInfo
     * @return
     */
    public static CloseableHttpClient getHttpClient(JSONObject loginInfo, boolean useSocketProxy) {
        CloseableHttpClient ret = null;
        String loginurl = loginInfo.getString("loginurl");
        ret = loginClientCache.getIfPresent(loginurl);
        if (ret == null) {
            //create new client instance
            try {
                /*Authenticator.setDefault(new Authenticator(){
                    protected  PasswordAuthentication  getPasswordAuthentication(){
                        PasswordAuthentication p=new PasswordAuthentication("", "".toCharArray());
                        return p;
                    }
                });*/

                Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
                PoolingHttpClientConnectionManager connectionManager = null;
                if(useSocketProxy){
                    socketFactoryRegistry = RegistryBuilder
                            .<ConnectionSocketFactory>create()
                            .register("http", new MyPlainConnectionSocketFactory())
                            .register("https", new MyConnectionSocketFactory(SSLContexts.createSystemDefault()))
                            .build();
                    connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, new FakeDnsResolver());
                }else {
                    final SSLContext sslcontext = SSLContexts.createSystemDefault();
                    final X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();
                    socketFactoryRegistry = RegistryBuilder
                            .<ConnectionSocketFactory>create()
                        .register ( "http", PlainConnectionSocketFactory.INSTANCE )
                        .register ( "https", new SSLConnectionSocketFactory( sslcontext, hostnameVerifier ) )
                            .build();
                    connectionManager = new PoolingHttpClientConnectionManager (socketFactoryRegistry );
                }
                CookieStore cookieStore = new BasicCookieStore();
                // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
                        .setConnectionRequestTimeout(60000).build();
                // 设置默认跳转以及存储cookie
                /*ret = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                        .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
                        .setConnectionManager(connectionManager)
                        .setConnectionManagerShared(true)
                        .setDefaultCookieStore(cookieStore).build();*/
                ret = HttpClients.custom().setConnectionManager(connectionManager).build();
                JSONObject formdata = loginInfo.getJSONObject("formdata");
                JSONObject headers = loginInfo.getJSONObject("headers");

                Map<String, Object> headersMap = null;
                Map<String, Object> formdataMap = null;
                headersMap = objectMapper.readValue(headers.toString(), Map.class);
                formdataMap = objectMapper.readValue(formdata.toString(), Map.class);

                if(loginurl.contains("tuicool.com")){
                    HttpGet httpGet = new HttpGet(loginurl);
                    for (Map.Entry<String, Object> param : headersMap.entrySet()) {
                        logger.info(param.getKey()+" | "+param.getValue());
                        httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
                    }
                    CloseableHttpResponse response = null;
                    if(useSocketProxy)
                        response = ret.execute(httpGet,getTorProxyContext());
                    else
                        response = ret.execute(httpGet);
                    String responseText = EntityUtils.toString(response.getEntity());
                    Document doc = Jsoup.parse(responseText);
                    String token = doc.getElementsByAttributeValue("name","csrf-token").get(0).attr("content");
                    formdata.put("authenticity_token",token);
                }


                //login
                HttpPost httpPost = new HttpPost(loginurl);
                for (Map.Entry<String, Object> param : headersMap.entrySet()) {
                    httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
                }

                ArrayList<NameValuePair> pairs = covertParams2NVPS(formdataMap);
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
                if(useSocketProxy)
                    ret.execute(httpPost,getTorProxyContext());
                else
                    ret.execute(httpPost);
            } catch (Exception e) {
                //e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                logger.error(str);
                ret = null;
            }
        }
        if(ret!=null) loginClientCache.put(loginurl,ret);
        return ret;
    }

    static HttpClientContext getTorProxyContext(){
        InetSocketAddress socksaddr = new InetSocketAddress("127.0.0.1", 9050);
        HttpClientContext context = HttpClientContext.create();
        context.setAttribute("socks.address", socksaddr);
        return context;
    }

    static class FakeDnsResolver implements DnsResolver {
        @Override
        public InetAddress[] resolve(String host) throws UnknownHostException {
            // Return some fake DNS record for every request, we won't be using it
            return new InetAddress[] { InetAddress.getByAddress(new byte[] { 1, 1, 1, 1 }) };
        }
    }


    public static String httpGetRequest(String url) {
        return httpGetRequest(url, true, null,false);
    }

    /**
     * @param url
     * @return
     */
    public static String httpGetRequest(String url, boolean mobileUA, JSONObject loginInfo, boolean useSocketProxy) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet, mobileUA, loginInfo,useSocketProxy);
    }

    public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {
        return httpGetRequest(url, params, true, null,false);
    }

    public static String httpGetRequest(String url, Map<String, Object> params, boolean mobileUA, JSONObject loginInfo, boolean useSocketProxy) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet, mobileUA, loginInfo,useSocketProxy);
    }


    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        return httpGetRequest(url, headers, params, true, null,false);
    }

    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params, boolean mobileUA, JSONObject loginInfo, boolean useSocketProxy)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet, mobileUA, loginInfo,useSocketProxy);
    }

    public static String httpPostRequest(String url) {
        return httpPostRequest(url, true, null,false);
    }

    public static String httpPostRequest(String url, boolean mobileUA, JSONObject loginInfo, boolean useSocketProxy) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost, mobileUA, loginInfo,useSocketProxy);
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
            System.out.println("Response : " + ret);
            EntityUtils.consume(resEntity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return ret;
    }

    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        return httpPostRequest(url, params, true, null,false);
    }

    public static String httpPostRequest(String url, Map<String, Object> params, boolean mobileUA, JSONObject loginInfo, boolean useSocketProxy) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost, mobileUA, loginInfo,useSocketProxy);
    }

    public static String httpPostRequest(String url, String body) throws UnsupportedEncodingException {
        return httpPostRequest(url, body, null, true, null,false);
    }

    public static String httpPostRequest(String url, String body, Map<String, Object> headers, boolean mobileUA, JSONObject loginInfo, boolean useSocketProxy) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        httpPost.setEntity(new StringEntity(body, UTF_8));
        return getResult(httpPost, mobileUA, loginInfo, useSocketProxy);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params) throws UnsupportedEncodingException {
        return httpPostRequest(url, headers, params, true, null,false);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params, boolean mobileUA, JSONObject loginInfo, boolean useSocketProxy)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost, mobileUA, loginInfo,useSocketProxy);
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
    private static String getResult(HttpRequestBase request, boolean mobileUA, JSONObject loginInfo, boolean useSocketProxy) {
        if (mobileUA)
            request.setHeader("User-Agent", androidUA);
        else
            request.setHeader("User-Agent", PCUA);

        CloseableHttpClient httpClient = null;
        if (loginInfo != null) {
            httpClient = getHttpClient(loginInfo);//only get the client with login session.
        } else {
            httpClient = HttpClients.createDefault();
        }

//        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = null;
            if(useSocketProxy)
                response = httpClient.execute(request,getTorProxyContext());
            else
                response = httpClient.execute(request);

            System.out.println("response code: " + response.getStatusLine().getStatusCode());
            ;
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity);
                response.close();
                // httpClient.close();
                return result;
            }
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(loginInfo==null) {//don't close for login session.
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return EMPTY_STR;
    }

    public static String httpGetRedirectFinalUrl(String url) {
        return httpGetRedirectFinalUrl(url, true, null);
    }

    public static String httpGetRedirectFinalUrl(String url, boolean mobileUA, JSONObject loginInfo) {
        String ret = null;
        HttpGet httpGet = new HttpGet(url);
        if (mobileUA)
            httpGet.setHeader("User-Agent", androidUA);
        else
            httpGet.setHeader("User-Agent", PCUA);
//        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpContext context = new BasicHttpContext();
            CloseableHttpResponse response = httpClient.execute(httpGet, context);
            HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            HttpHost currentHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
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
            if (StringUtils.trimToEmpty(contentType.getValue()).contains("gif")) {
                filepath = FilenameUtils.getFullPath(filepath) + FilenameUtils.getBaseName(filepath) + ".gif";
            } else if (StringUtils.trimToEmpty(contentType.getValue()).contains("png")) {
                filepath = FilenameUtils.getFullPath(filepath) + FilenameUtils.getBaseName(filepath) + ".png";
            } else if (StringUtils.trimToEmpty(contentType.getValue()).contains("jpeg")) {
                filepath = FilenameUtils.getFullPath(filepath) + FilenameUtils.getBaseName(filepath) + ".jpeg";
            } else if (StringUtils.trimToEmpty(contentType.getValue()).contains("bmp")) {
                filepath = FilenameUtils.getFullPath(filepath) + FilenameUtils.getBaseName(filepath) + ".bmp";
            }
            InputStream is = entity.getContent();
            File file = new File(filepath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] buffer = new byte[BUFFER_SIZE];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
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