package com.iamsinghankit.jload.core;

import com.iamsinghankit.jload.JLoadException;
import com.iamsinghankit.jload.core.internal.AlgoType;

import java.util.ArrayList;
import java.util.List;

public record Configuration(int port, boolean debug, int retry, List<Host> hosts, boolean help, String version,
                            boolean versionRequested, AlgoType algoType) {


    public static Configuration setup(String... args) {
        int port = 8080, retry = 3;
        boolean debug = false, help = false, versionRequested = false;
        var hosts = List.of(new Host("localhost", 9090));
        String version = "JLoad v" + Configuration.class.getPackage().getImplementationVersion();
        AlgoType algoType = AlgoType.ROUND_ROBIN;

        for (String arg : args) {
            String[] params = arg.split("=");
            switch (params[0]) {
                case "--port", "-p" -> port = getValue("--port,-p", params);
                case "--debug", "-d" -> debug = true;
                case "--retry", "-r" -> retry = getValue("--retry,-r", params);
                case "--hosts" -> hosts = Host.of(params);
                case "--help", "-h" -> help = true;
                case "--algo", "-a" -> algoType = getAlgoType(params);
                case "--version", "-v" -> versionRequested = true;
                default -> throw new JLoadException("Invalid parameter: " + params[0]);
            }
        }
        return new Configuration(port, debug, retry, hosts, help, version, versionRequested, algoType);
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
