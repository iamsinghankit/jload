package com.iamsinghankit.jload.core;

import com.iamsinghankit.jload.JLoadException;
import com.iamsinghankit.jload.core.internal.AlgoType;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import static com.iamsinghankit.jload.core.Configuration.Host.LOCAL_HOST;
import static com.iamsinghankit.jload.core.internal.AlgoType.ROUND_ROBIN;

@Builder
public record Configuration(int port, boolean debug, int retry, List<Host> hosts, boolean help, String version,
                            boolean versionRequested, AlgoType algoType) {


    public static Configuration setup(String... args) {
        ConfigurationBuilder b = Configuration.builder()
                .port(8080)
                .retry(2)
                .algoType(ROUND_ROBIN)
                .hosts(List.of(LOCAL_HOST))
                .version("JLoad v" + Configuration.class.getPackage().getImplementationVersion());

        for (String arg : args) {
            String[] params = arg.split("=");
            switch (params[0]) {
                case "--port", "-p" -> b.port = getValue("--port,-p", params);
                case "--debug", "-d" -> b.debug = true;
                case "--retry", "-r" -> b.retry = getValue("--retry,-r", params);
                case "--hosts" -> b.hosts = Host.of(params);
                case "--help", "-h" -> b.help = true;
                case "--algo", "-a" -> b.algoType = getAlgoType(params);
                case "--version", "-v" -> b.versionRequested = true;
                default -> throw new JLoadException("Invalid parameter: " + params[0]);
            }
        }
        return b.build();
    }

    private static AlgoType getAlgoType(String[] params) {
        if (params.length != 2) throw new JLoadException("Invalid parameter --algo,-a");
        return AlgoType.of(params[1]);
    }

    private static int getValue(String param, String[] params) {
        if (params.length != 2) throw new JLoadException("Invalid parameter " + param);
        return Integer.parseInt(params[1]);
    }

    public record Host(String url, int port) {
        final static Host LOCAL_HOST = new Host("localhost", 9090);

        static List<Host> of(String[] addresses) {
            if (addresses.length != 2) {
                throw new JLoadException("Invalid parameter --hosts");
            }

            String[] addressArr = addresses[1].split(",");
            List<Host> hosts = new ArrayList<>();
            for (var address : addressArr) {
                if (!address.contains(":")) {
                    throw new JLoadException("Invalid parameter --hosts=" + address);
                }
                String[] addr = address.split(":");
                hosts.add(new Host(addr[0], Integer.parseInt(addr[1])));
            }
            return hosts;
        }
    }
}
