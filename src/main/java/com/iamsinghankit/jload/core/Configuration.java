package com.iamsinghankit.jload.core;

import com.iamsinghankit.jload.JLoadException;

import java.util.ArrayList;
import java.util.List;

public record Configuration(int port, boolean debug, int retry, List<Host> hosts, boolean help, String version,
                            boolean versionRequested) {

    public static Configuration INSTANCE;


    public static Configuration setup(String... args) {
        int port = 8080, retry = 3;
        boolean debug = false, help = false, versionRequested = false;
        List<Host> hosts = List.of(new Host("localhost", 9090));
        String version = "JLoad v" + Configuration.class.getPackage().getImplementationVersion();

        for (String arg : args) {
            String[] params = arg.split("=");
            switch (params[0]) {
                case "--port" -> port = getValue("--port", params);
                case "--debug" -> debug = true;
                case "--retry" -> retry = getValue("--retry", params);
                case "--hosts" -> hosts = Host.of(params);
                case "--help" -> help = true;
                case "--version" -> versionRequested = true;
                default -> throw new JLoadException("Invalid parameter: " + params[0]);
            }
        }
        INSTANCE = new Configuration(port, debug, retry, hosts, help, version, versionRequested);
        return INSTANCE;
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
