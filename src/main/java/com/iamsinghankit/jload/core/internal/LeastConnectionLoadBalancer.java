package com.iamsinghankit.jload.core.internal;

import com.iamsinghankit.jload.core.Configuration.Host;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ankit Singh
 */
class LeastConnectionLoadBalancer extends AbstractLoadBalancer {
    private final List<Host> hosts;
    private final ConcurrentHashMap<Host, Long> hostCache;

    LeastConnectionLoadBalancer(List<Host> hosts, int retry) {
        super(retry);
        this.hosts = hosts;
        this.hostCache = new ConcurrentHashMap<>(hosts.size() + 2);
        populateCache();
    }

    private void populateCache() {
        for (var entry : hosts) {
            hostCache.put(entry, 0L);
        }
    }

    @SuppressWarnings("ConstantValue")
    @Override
    protected Host tryHost() {
        Long min = null;
        Host host = null;
        for (var entry : hostCache.entrySet()) {
            if (min == null && host == null) {
                min = entry.getValue();
                host = entry.getKey();
            }
            Long value = entry.getValue();
            if (value < min) {
                min = value;
                host = entry.getKey();
            }
        }
        hostCache.compute(host, (k, v) -> v + 1);
        return host;
    }
}
