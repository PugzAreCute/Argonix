/*
 * Argonix: Argon Webserver
 * Copyright (C) 2023  PugzAreCute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.pugzarecute.argonix;

import com.pugzarecute.argonix.hanlders.Injectable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.*;

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

        Injectable staticHandler = new Injectable();

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
