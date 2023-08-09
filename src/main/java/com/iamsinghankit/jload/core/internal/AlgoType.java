package com.iamsinghankit.jload.core.internal;

import com.iamsinghankit.jload.JLoadException;

import java.util.function.Supplier;

import static com.iamsinghankit.jload.core.Configuration.INSTANCE;

/**
 * @author Ankit Singh
 */
public enum AlgoType {
    ROUND(new Lazy(() -> new RoundRobinLoadBalancer(INSTANCE.hosts(), INSTANCE.retry()))),
    RANDOM(new Lazy(() -> new RandomLoadBalancer(INSTANCE.hosts(), INSTANCE.retry())));

    private final Lazy lazyLoadBalancer;

    AlgoType(Lazy lazyLoadBalancer) {
        this.lazyLoadBalancer = lazyLoadBalancer;
    }

    public static AlgoType of(String value) {
        return switch (value) {
            case "round" -> ROUND;
            case "random" -> RANDOM;
            default -> throw new JLoadException("Invalid parameter value: " + value);
        };
    }

    public LoadBalancer loadBalancer() {
        return lazyLoadBalancer.get();
    }

    private static class Lazy {
        Supplier<LoadBalancer> supplier;
        LoadBalancer loadBalancer;

        Lazy(Supplier<LoadBalancer> supplier) {
            this.supplier = supplier;
        }

        public LoadBalancer get() {
            if (loadBalancer == null) {
                loadBalancer = supplier.get();
            }
            return loadBalancer;
        }
    }
}
