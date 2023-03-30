package com.pugzarecute.argonix;


import org.apache.logging.log4j.*;

public class Init {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger("Main");
        logger.info("Starting Aronix");
        ServerManager manager = new ServerManager();
        manager.init();
    }
}
