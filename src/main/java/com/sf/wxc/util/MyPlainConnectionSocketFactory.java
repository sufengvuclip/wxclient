package com.sf.wxc.util;

import org.apache.http.HttpHost;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * Created by Administrator on 2016-12-29.
 */
public class MyPlainConnectionSocketFactory extends PlainConnectionSocketFactory {
    private static Logger logger = LoggerFactory.getLogger(MyPlainConnectionSocketFactory.class);
    @Override
    public Socket createSocket(HttpContext httpContext) throws IOException {
        InetSocketAddress socksaddr = (InetSocketAddress) httpContext.getAttribute("socks.address");
        logger.info("socksaddr {}",socksaddr==null?"null":socksaddr.getHostString());
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
        return new Socket(proxy);
    }

    @Override
    public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                InetSocketAddress localAddress, HttpContext context) throws IOException {
        // Convert address to unresolved
        InetSocketAddress unresolvedRemote = InetSocketAddress
                .createUnresolved(host.getHostName(), remoteAddress.getPort());
        return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);
    }
}
