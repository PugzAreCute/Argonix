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

import com.google.gson.Gson;
import com.pugzarecute.argonix.Init;
import com.pugzarecute.argonix.json.JenkinsMain;
import com.pugzarecute.argonix.json.JenkinsProject;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler.Abstract;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


public class CiInjectable extends Abstract{
    // Use -Djava.net.preferIPv6Addresses=true to use ipv6 over v4
    public static final String REMOTE_URL ="https://argon.pugzarecute.com/ci/api/json";
    public static final String API_ENDPOINT = "/api/json";


    @Override
    public boolean handle(Request request, Response response, Callback callback) throws Exception {
        response.getHeaders().add("X-Powered-By","Argonix(Jetty://)");
        response.getHeaders().add("Server","Argonix(Jetty://)");
        String context = Request.getPathInContext(request).substring(1);
        assert Init.WEBAPP_PATH != null;
        String file2load =  Init.WEBAPP_PATH.getFile();
        System.out.println(context);
        if(Objects.equals(context, "")) file2load +="projects/ci.html";
        else {
            file2load+="projects/";
            file2load+=context;
        }
        System.out.println(file2load);

        File file = new File(file2load);
        Gson gson = new Gson();
        JenkinsMain projects = gson.fromJson(new InputStreamReader(new URL(REMOTE_URL).openStream()), JenkinsMain.class);

        if(!file.exists()){
            for (JenkinsProject proj:
                    projects.getJobs()) {
                if(proj.getName().equalsIgnoreCase(context)){
                    System.out.println("Valid project");
                    break;
                }
            }
            return true;
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

        StringBuilder projectEntry = new StringBuilder();
        for(String x:Files.readAllLines(Path.of(Init.WEBAPP_PATH.getFile() + "util/ci_project.html"))){
            projectEntry.append(x);
        }


        int i = 0;
        StringBuilder content2Add=new StringBuilder();
        while (i < projects.getJobs().size()){
            content2Add.append(projectEntry.toString().replaceAll("<argonix[.]injector[.]project[.]title>",projects.getJobs().get(i).getName()).replaceAll("<argonix[.]injector[.]project[.]href>",Init.BASE+"projects/"+projects.getJobs().get(i).getName()));
            i++;
        }

        content = new StringBuilder(content.toString().replaceFirst("<argonix[.]injector[.]navbar>", navbar.toString()));
        content = new StringBuilder(content.toString().replaceFirst("<argonix[.]injector[.]projects>",content2Add.toString()));
        Content.Sink.write(response,true, content.toString(),callback);
        response.setStatus(200);
        return true;
    }
}
