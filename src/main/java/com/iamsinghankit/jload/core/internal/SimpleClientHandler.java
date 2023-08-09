package com.iamsinghankit.jload.core.internal;

import com.iamsinghankit.jload.core.ClientHandler;
import com.iamsinghankit.jload.logger.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HexFormat;

import static com.iamsinghankit.jload.core.Configuration.INSTANCE;
import static java.lang.Thread.startVirtualThread;

/**
 * @author Ankit Singh
 */
class SimpleClientHandler implements ClientHandler {
    private final Socket socket;
    private final String id;
    private final LoadBalancer loadBalancer;

    public SimpleClientHandler(AlgoType algoType, Socket socket) {
        this.loadBalancer = algoType.loadBalancer();
        this.socket = socket;
        this.id = generateId();
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void handle() {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            Socket host = loadBalancer.nextHost();
            Log.info("[%s] Forwarding request to: %s ", id, socket.toString());
            var hostInput = host.getInputStream();
            var hostOutput = host.getOutputStream();
            Thread ipt = startVirtualThread(() -> transferTo(input, hostOutput));
            Thread opt = startVirtualThread(() -> transferTo(hostInput, output));
            ipt.join();
            opt.join();
        } catch (Exception ex) {
            Log.error("Couldn't complete the request, due to %s", ex.getMessage());
        }
    }

    private void transferTo(InputStream in, OutputStream out) {
        try {
            in.transferTo(out);
        } catch (IOException ex) {
            Log.error("Writing failed, %s", ex.getMessage());
        }
    }


    @Override
    public void run() {
        handle();
    }

    private String generateId() {
        return HexFormat.of().toHexDigits(this.hashCode());
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
