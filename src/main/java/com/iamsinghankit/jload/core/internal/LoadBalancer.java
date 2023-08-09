package com.iamsinghankit.jload.core.internal;

import java.net.Socket;

/**
 * @author Ankit Singh
 */
public interface LoadBalancer {

    Socket nextHost();
}
