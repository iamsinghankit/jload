package com.iamsinghankit.jload.core.internal;

import com.iamsinghankit.jload.core.Configuration.Host;
import com.iamsinghankit.jload.logger.Log;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.valueOf;

/**
 * @author Ankit Singh
 */
class RoundRobinLoadBalancer implements LoadBalancer {

    private final List<Host> hosts;
    private final int retry;
    private final AtomicInteger count;

    RoundRobinLoadBalancer(List<Host> hosts, int retry) {
        this.hosts = hosts;
        this.retry = retry;
        this.count = new AtomicInteger(0);
    }


    @Override
    public Socket nextHost() {
        int retry0 = retry;
        while (retry0 > 0) {
            Host host = nextAddress();
            Socket socket = connect(host);
            if (socket == null || socket.isClosed()) retry0--;
            else return socket;
        }
        throw new RuntimeException("Client not available");
    }

    private Socket connect(Host host) {
        try {
            return new Socket(host.url(), host.port());
        } catch (Exception ex) {
            Log.error("Couldn't connect to host on %s, port %s, due to %s", host.url(), valueOf(host.port()), ex.getMessage());
            return null;
        }
    }


    private Host nextAddress() {
        Host host = hosts.get(count.get() % hosts.size());
        count.incrementAndGet();
        return host;

    }
}
