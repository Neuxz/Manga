package de.herrlock.manga.http;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author HerrLock
 */
public final class ServerMain {
    private static final Logger logger = LogManager.getLogger();

    private final Server server;

    public static void main( final String... args ) throws ServletException, LifecycleException, IOException {
        logger.entry();
        ServerMain srvMain = new ServerMain();
        srvMain.start();
        srvMain.listenForStop();
    }

    public ServerMain() throws ServletException {
        this.server = new Server();
    }

    public void start() throws LifecycleException, IOException {
        logger.entry();
        this.server.start();
    }

    public void listenForStop() {
        logger.entry();
        this.server.listenForStop();
    }

    public void stop() throws LifecycleException {
        logger.entry();
        this.server.stop();
    }
}
