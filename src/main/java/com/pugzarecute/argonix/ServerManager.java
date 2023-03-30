package com.pugzarecute.argonix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.ResourceFactory;

public class ServerManager {
    void init(){
        Logger logger = LogManager.getLogger("ServerLauncher");
        Server server = new Server();

        HttpConfiguration config = new HttpConfiguration();
        config.setSendServerVersion(false);
        config.setSendXPoweredBy(false);

        ServerConnector connector = new ServerConnector(server,1,1,new HttpConnectionFactory(config));
        connector.setPort(9191);
        connector.setAcceptQueueSize(128);

        ResourceHandler staticHandler = new ResourceHandler();

        //TODO: Fix this magic
        staticHandler.setBaseResource(ResourceFactory.of(staticHandler).newResource(getClass().getResource("../../../webapp/")));
        staticHandler.setWelcomeFiles("index.html");

        server.addConnector(connector);
        server.setHandler(staticHandler);
        try {
            server.start();
            logger.info("Server started!");
        } catch (Exception e) {
            logger.fatal("Server failed");
            throw new RuntimeException(e);
        }
    }

}
