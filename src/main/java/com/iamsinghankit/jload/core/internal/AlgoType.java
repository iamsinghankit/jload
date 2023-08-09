package com.iamsinghankit.jload.core.internal;

import com.iamsinghankit.jload.JLoadException;
import com.iamsinghankit.jload.core.Configuration;

import java.util.function.Function;

/**
 * @author Ankit Singh
 */
public enum AlgoType {
    ROUND_ROBIN(new Lazy(RoundRobinLoadBalancer::new)),
    RANDOM(new Lazy(RandomLoadBalancer::new)),
    LEAST_CONNECTION(new Lazy(LeastConnectionLoadBalancer::new));

    private final Lazy lazyLoadBalancer;

    AlgoType(Lazy lazyLoadBalancer) {
        this.lazyLoadBalancer = lazyLoadBalancer;
    }

    public static AlgoType of(String value) {
        return switch (value) {
            case "round_robin" -> ROUND_ROBIN;
            case "random" -> RANDOM;
            case "least_connection" -> LEAST_CONNECTION;
            default -> throw new JLoadException("Invalid parameter value: " + value);
        };
    }

    public LoadBalancer loadBalancer(Configuration config) {
        return lazyLoadBalancer.get(config);
    }

    private static class Lazy {
        Function<Configuration, LoadBalancer> func;
        LoadBalancer loadBalancer;

        Lazy(Function<Configuration, LoadBalancer> func) {
            this.func = func;
        }

        public LoadBalancer get(Configuration configuration) {
            if (loadBalancer == null) {
                loadBalancer = func.apply(configuration);
            }
            return loadBalancer;
        }
    }
}
