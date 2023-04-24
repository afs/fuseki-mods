/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.fuseki.mod_server;

import java.net.URLEncoder;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;

import org.apache.jena.atlas.lib.FileOps;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.sys.FusekiModules;
import org.apache.jena.fuseki.mod.admin.FMod_Admin;
import org.apache.jena.fuseki.mod.ui.FMod_UI;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.http.HttpOp;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sys.JenaSystem;

public class DevFusekiModServer {

    static { FusekiLogging.setLogging(); }

    public static void main(String[] args) {

//        JenaSystem.DEBUG_INIT = true;
//        //System.setProperty("fuseki.loglogging", "true");

        //System.setProperty("fuseki.logLoading", "true");

        JenaSystem.init();
        //Done by InitFuseki
        //FusekiModulesLoaded.load();
        //FusekiModules fmods = FusekiModulesLoaded.loaded();
        //FusekiModules fmods = FusekiModules.empty;

        // Order matters
        FusekiModules fmods = FusekiModules.create(FMod_UI.get(), FMod_Admin.get());

        System.setProperty("FUSEKI_BASE", "run");
        FileOps.clearAll("run");

        FusekiServer server = FusekiServer.create()
                .setModules(fmods)
                .port(4040)
                //.add("/ds", DatasetGraphFactory.createTxnMem())
                .start();


        String URL = "http://localhost:"+server.getPort()+"/ds";

        String name = URLEncoder.encode("/ds", StandardCharsets.UTF_8);
        //http://localhost:4040/$/datasets
        //dbName=%2Fds&dbType=mem
        HttpOp.httpPost("http://localhost:4040/$/datasets",
                        WebContent.contentTypeHTMLForm,
                        BodyPublishers.ofString("dbName="+name+"&dbType=mem", StandardCharsets.UTF_8));



        //AuthEnv.get().registerUsernamePassword(URI.create(URL), "user1", "passwd1");

        //HttpOp.httpPost(url);

        try {
            server.join();
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
