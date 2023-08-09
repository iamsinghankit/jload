package com.iamsinghankit.jload.core.internal;

import com.iamsinghankit.jload.core.Configuration;
import com.iamsinghankit.jload.core.Configuration.Host;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ankit Singh
 */
class RoundRobinLoadBalancer extends AbstractLoadBalancer {

    private final List<Host> hosts;
    private final AtomicInteger count;

    RoundRobinLoadBalancer(Configuration config) {
        super(config.retry());
        this.hosts = config.hosts();
        this.count = new AtomicInteger(0);
    }

    @Override
    protected Host tryHost() {
        Host host = hosts.get(count.get() % hosts.size());
        count.incrementAndGet();
        return host;
    }

}
