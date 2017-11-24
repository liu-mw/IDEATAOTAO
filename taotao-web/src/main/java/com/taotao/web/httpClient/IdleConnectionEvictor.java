package com.taotao.web.httpClient;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author liu_mw
 * @date 2017/11/23 21:30
 */
public class IdleConnectionEvictor extends Thread {

    private final HttpClientConnectionManager connMgr;

    private volatile boolean shutdown;

    public IdleConnectionEvictor(HttpClientConnectionManager connMgr) {
        this.connMgr = connMgr;
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}