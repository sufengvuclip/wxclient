package com.sf.wxc.util;

import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * Created by Administrator on 2016-12-29.
 */
public class MyPlainConnectionSocketFactory extends PlainConnectionSocketFactory {
    public MyPlainConnectionSocketFactory() {
    }

    @Override
    public Socket createSocket(HttpContext httpContext) throws IOException {
        InetSocketAddress socksaddr = (InetSocketAddress) httpContext.getAttribute("socks.address");
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
        return new Socket(proxy);
    }
}
