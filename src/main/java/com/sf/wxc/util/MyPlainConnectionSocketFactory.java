package com.sf.wxc.util;

import org.apache.http.HttpHost;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * Created by Administrator on 2016-12-29.
 */
public class MyPlainConnectionSocketFactory implements ConnectionSocketFactory {
    public MyPlainConnectionSocketFactory() {
    }

    @Override
    public Socket createSocket(HttpContext httpContext) throws IOException {
        InetSocketAddress socksaddr = (InetSocketAddress) httpContext.getAttribute("socks.address");
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
        return new Socket(proxy);
    }

    @Override
    public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
        Socket sock = socket != null?socket:this.createSocket(context);
        if(localAddress != null) {
            sock.bind(localAddress);
        }

        try {
            sock.connect(remoteAddress, connectTimeout);
            return sock;
        } catch (IOException var11) {
            try {
                sock.close();
            } catch (IOException var10) {
                ;
            }

            throw var11;
        }
    }
}
