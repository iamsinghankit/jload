package com.iamsinghankit.jload.core;

import java.io.Closeable;

/**
 * @author Ankit Singh
 */
public interface ClientHandler extends Runnable, Closeable {

    void handle();

    String id();

}
