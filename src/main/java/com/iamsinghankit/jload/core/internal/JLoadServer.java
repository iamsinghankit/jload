package com.iamsinghankit.jload.core.internal;

import com.iamsinghankit.jload.core.ClientHandler;
import com.iamsinghankit.jload.core.Server;
import com.iamsinghankit.jload.logger.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.iamsinghankit.jload.core.Configuration.INSTANCE;
import static java.lang.String.valueOf;
import static java.lang.Thread.startVirtualThread;

/**
 * @author Ankit Singh
 */
public class JLoadServer implements Server {


    private final int port;
    private volatile boolean running;

    public JLoadServer(int port) {
        this.port = port;
    }

    public void start() {
        if (running) {
            Log.info("Server already running!");
            return;
        }
        try (ServerSocket server = new ServerSocket(port)) {
            Log.info("JLoad Server started on port: %s", valueOf(port));
            running = true;
            while (running) {
                handleClient(server.accept());
            }
        } catch (IOException ex) {
            close();
            Log.error(ex, "JLoad Server crashed: %s", ex.getMessage());
        }
    }

    private void handleClient(Socket socket) {
        startVirtualThread(() -> {
            try (ClientHandler client = new SimpleClientHandler(INSTANCE.algoType(), socket)) {
                Log.debug("Client Connected - %s", client.id());
                client.handle();
            } catch (IOException ex) {
                Log.error("Client disconnected %s", ex.getMessage());
            }
        });
    }

    @Override
    public void close() {
        running = true;
    }
}
