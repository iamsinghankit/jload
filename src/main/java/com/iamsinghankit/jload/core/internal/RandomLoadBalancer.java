package com.iamsinghankit.jload.core.internal;

import com.iamsinghankit.jload.core.Configuration.Host;

import java.util.List;
import java.util.Random;

/**
 * @author Ankit Singh
 */
 class RandomLoadBalancer extends AbstractLoadBalancer {
    private final List<Host> hosts;
    private final Random random;

    RandomLoadBalancer(List<Host> hosts, int retry) {
        super(retry);
        this.hosts = hosts;
        this.random = new Random();
    }

    @Override
    public Host tryHost() {
        return hosts.get(random.nextInt(hosts.size()));
    }
}
