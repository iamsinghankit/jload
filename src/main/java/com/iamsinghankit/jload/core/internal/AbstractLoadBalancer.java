package com.iamsinghankit.jload.core.internal;

import com.iamsinghankit.jload.core.Configuration;
import com.iamsinghankit.jload.core.Configuration.Host;
import com.iamsinghankit.jload.logger.Log;

import java.net.Socket;

import static java.lang.String.valueOf;

/**
 * @author Ankit Singh
 */
abstract class AbstractLoadBalancer implements LoadBalancer {

    private final int retry;

    AbstractLoadBalancer(int retry) {
        this.retry = retry;
    }

    @Override
    public Socket nextHost() {
        int retry0 = retry;
        while (retry0 > 0) {
            Host host = tryHost();
            Socket socket = connect(host);
            if (socket == null || socket.isClosed()) retry0--;
            else return socket;
        }
        throw new RuntimeException("Client not available");
    }

    protected abstract Host tryHost();

    protected Socket connect(Host host) {
        try {
            return new Socket(host.url(), host.port());
        } catch (Exception ex) {
            Log.error("Couldn't connect to host on %s, port %s, due to %s", host.url(), valueOf(host.port()), ex.getMessage());
            return null;
        }
    }
}
