package com.iamsinghankit.jload;

import com.iamsinghankit.jload.core.Configuration;
import com.iamsinghankit.jload.core.Server;
import com.iamsinghankit.jload.core.internal.JLoadServer;
import com.iamsinghankit.jload.logger.Log;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * @author Ankit Singh
 */
public class JLoadApp {
    private static final String HELP_MSG = """
            JLoad is a simple TCP layer 4 highly performant load balancer.
            usage: jload [options...]
            --port=,-p=    port on which jload will listen for connections.
                           default=8080
            --debug,-d     enable debugging.
                           default=false
            --retry=,-r=   no, of retry attempt before treating as failure.
                           default=3
            --hosts=       list of hosts with port no for jload to balance,
                           for more than one host use separator comma, other than comma
                           value will be ignored.
                           default=localhost:9090
            --algo=,-a=    select algorithm for load balancing,
                           possible values:[round_robin,random,least_connection].
                           default=round_robin
            --help,-h      print this help message.
            --version,-v   print current version.
            """;

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        Configuration config;
        try {
            config = Configuration.setup(args);
            Log.setupLog(config.debug());

            if (config.help()) {
                out.println(HELP_MSG);
                return;
            }
            if (config.versionRequested()) {
                out.println(config.version());
                return;
            }
        } catch (JLoadException ex) {
            err.println("Failed to start jload, " + ex.getMessage());
            out.println(HELP_MSG);
            return;
        }
        Server jload = new JLoadServer(config);
        jload.start();
    }


}
