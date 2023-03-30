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

package com.pugzarecute.argonix.hanlders;

import com.pugzarecute.argonix.Init;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler.Abstract;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import java.io.File;

public class Injectable extends Abstract{
    @Override
    public boolean handle(Request request, Response response, Callback callback) throws Exception {
        String context = Request.getPathInContext(request).substring(1);
        assert Init.WEBAPP_PATH != null;
        String file2load =  Init.WEBAPP_PATH.getFile();
        if(Objects.equals(context, "")) file2load +="index.html";
        else file2load+=context;
        System.out.println(file2load);

        File file = new File(file2load);

        if(!file.exists()){
            response.setStatus(404);
            Content.Sink.write(response,true, "404",callback);
            return false;
        }
        StringBuilder content= new StringBuilder();
        for (String s:
             Files.readAllLines(file.toPath())) {
            content.append(s);
        }
        StringBuilder navbar = new StringBuilder();
        for (String s:Files.readAllLines(Path.of(Init.WEBAPP_PATH.getFile()+"util/navbar.html"))){
            navbar.append(s);
        }
        content = new StringBuilder(content.toString().replaceFirst("<argonix[.]injector[.]navbar>", navbar.toString()));

        Content.Sink.write(response,true, content.toString(),callback);
        response.setStatus(200);
        return true;
    }
}
