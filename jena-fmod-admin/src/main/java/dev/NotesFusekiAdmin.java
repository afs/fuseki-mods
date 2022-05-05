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
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;

public class NotesFusekiAdmin {
    // [ ] Fuseki main+admin

    // ---
    // Modules add command line args?

    // Packaging apache-jena-fuseki2


    // [ ] Fuseki main+sparqler
    //     jena-fmod-sparqler

    // [ ] Security
    // https://unpkg.com/

    // ----

    // [ ] Startup - inject disk.
    // Future - disk builds a configuration.
    // [ ] Localhost - put in ActionCtl.
    // [ ] Split "datasets" into read and write functions. Or test
    // [ ] AdminAllowed:
    //     "--admin user=password" or "--admin local"
    //     "--adminArea run/"


    public static void main(String ... a) {
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
    }
}
