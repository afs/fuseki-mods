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

package org.apache.jena.fuseki.mod.admin;

import java.util.Set;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.ctl.ActionCtl;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.sys.FusekiModule;
import org.apache.jena.fuseki.mgt.ActionBackup;
import org.apache.jena.fuseki.mgt.ActionBackupList;
import org.apache.jena.fuseki.mgt.ActionDatasets;
import org.apache.jena.fuseki.mod.other.ActionServerStatus;
import org.apache.jena.fuseki.webapp.FusekiEnv;
import org.apache.jena.fuseki.webapp.FusekiWebapp;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;

public class FMod_Admin implements FusekiModule {

    public FMod_Admin() {}

    private static Logger LOG = Fuseki.configLog;

    @Override
    public void start() {
        FusekiEnv.mode = FusekiEnv.INIT.STANDALONE;
        FusekiWebapp.formatBaseArea();
    }

    @Override
    public String name() {
        return "Admin";
    }

    @Override
    public void prepare(FusekiServer.Builder builder, Set<String> datasetNames, Model configModel) {
        // Modify the server to include the admin operations.
        ActionCtl actionBackup = new ActionBackup();
        builder
            // Admin.
            .addServlet("/$/datasets", new ActionDatasets())
            .addServlet("/$/backup", actionBackup)
            .addServlet("/$/backups", actionBackup)
            .addServlet("/$/backups-list", new ActionBackupList())
            // Before the Fuseki filter!
            .addFilter("/$/*", new LocalhostOnly())

            // General server functions.
            //.addServlet("/$/datasets", new ActionDatasets()) -- UI has a restricted version
            .addServlet("/$/server", new ActionServerStatus())
            .enablePing(true)
            .enableStats(true)
            // Not required but helpful.
            .enableCompact(true)
            .enableMetrics(true)
            .enableTasks(true)
            ;

        LOG.info("Fuseki Admin loaded");
    }
}
