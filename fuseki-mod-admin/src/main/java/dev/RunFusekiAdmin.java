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

package dev;

import org.apache.jena.atlas.lib.FileOps;
import org.apache.jena.cmd.ArgDecl;
import org.apache.jena.cmd.ArgModuleGeneral;
import org.apache.jena.cmd.CmdArgModule;
import org.apache.jena.cmd.CmdGeneral;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.http.HttpOp;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.exec.QueryExec;
import org.apache.jena.sparql.exec.RowSet;
import org.apache.jena.sparql.exec.RowSetOps;
import org.apache.jena.sparql.exec.UpdateExec;
import org.apache.jena.sparql.exec.http.Params;

public class RunFusekiAdmin {
    // [ ] Fuseki main+admin

    // Modules add command line args?

    // [ ] Startup - inject commands. ArgModuleAdmin.
    // Future - disk builds a configuration.
    // [ ] Localhost - put in ActionCtl.
    // [ ] Split "datasets" into read and write functions. Or test
    // [ ] AdminAllowed:
    //     "--admin user=password" or "--admin local"
    //     "--adminArea run/"

    // [ ] Modules and server startup / command line.
    //     ArgModule!

    public class ArgModuleAdmin implements ArgModuleGeneral {
        // Add a static of "extra command"

        private ArgDecl argAdmin = new ArgDecl(true, "admin");
        private ArgDecl argAdminArea = new ArgDecl(true, "adminArea", "adminBase");

        public ArgModuleAdmin() {
        }

        @Override
        public void processArgs(CmdArgModule cmdLine) {
            String admin = cmdLine.getValue(argAdmin);
            if ( admin != null ) {
                if ( admin.equals("localhost") ) {}
                else {
                    String pwFile = admin;
                }
            }

            String directory = cmdLine.getValue(argAdminArea);
        }

        @Override
        public void registerWith(CmdGeneral cmdLine) {
            cmdLine.add(argAdmin, "--admin=[UserPasswordFile|localhost]", "Enable the admin module");
            cmdLine.add(argAdminArea, "--adminBase=DIR", "Admin state directory");
        }

    }

    public static void main(String ... a) {
        try {
            main2();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        finally {
            System.exit(0);
        }
    }

    public static void main2(String ... a) {

        FusekiLogging.setLogging();
        FileOps.clearAll("run");

        //FusekiModules.add(new FMod_Admin());
        DatasetGraph dsg = DatasetGraphFactory.createTxnMem();
        FusekiServer server = FusekiServer.create()
                .add("/ds", dsg)
                .port(3030)

//                // Run direct - less refresh issues.
//
//                .staticFileBase("src/main/resources/org.apache.jena.fuseki.ui")
//
//                // Required functions.
//                .addServlet("/$/datasets", new ActionDatasets())
//                .addServlet("/$/server", new ActionServerStatus())
//                .enablePing(true)
//                .enableStats(true)
//                // Not required but helpful.
//                .enableCompact(true)
//                .enableMetrics(true)
//                .enableTasks(true)
//                // sparqler
//                .addServlet("/$/validate/query",  new QueryValidator())
//                .addServlet("/$/validate/update", new UpdateValidator())
//                .addServlet("/$/validate/iri",    new IRIValidator())
//                .addServlet("/$/validate/data",   new DataValidator())

                .build()
                .start();


        //http://localhost:3030/$/datasets
        // Body: dbName=%2Fds&dbType=mem

        // Create!
        Params params = Params.create().add("dbName", "/ds2").add("dbType", "mem");
        //String HOST = "192.168.1.13";
        String HOST = "localhost";
        String baseURL = "http://"+HOST+":3030";

        HttpOp.httpPostForm(baseURL+"/$/datasets", params);

        UpdateExec.service(baseURL+"/ds2").update("INSERT DATA { <x:s> <x:p> 123 }").execute();
        RowSet rowSet = QueryExec.service(baseURL+"/ds2").query("SELECT ?o { ?s ?p ?o }").select();
        RowSetOps.out(rowSet);

        System.out.println("DONE");
        System.exit(0);
    }
}
