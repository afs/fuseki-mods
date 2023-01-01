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

package org.apache.jena.fuseki.mod.shiro;

import java.util.List;
import java.util.Set;

import javax.servlet.Filter;

import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.sys.FusekiModule;
import org.apache.jena.rdf.model.Model;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fuseki Module for Apache Shiro
 */
public class FMod_Shiro implements FusekiModule {
    public static final Logger shiroConfigLog = LoggerFactory.getLogger(Fuseki.PATH+".Shiro");

    @Override
    public String name() { return "ModShiro"; }

    //@Override public void start() { }

    @Override public void prepare(FusekiServer.Builder serverBuilder, Set<String> datasetNames, Model configModel) {
        Filter filter = new ShiroFilter() {
            @Override public void init() throws Exception {
                // Shiro environment initialization, done here because we don't have webapp listeners.
                EnvironmentLoaderListener shiroListener = new ShiroEnvironmentLoaderListener(List.of("file:shiro.ini"));
                shiroListener.initEnvironment(getServletContext());
                super.init();
            }
        };
        // This is a "before" filter.
        serverBuilder.addFilter("/*", filter);
    }

    @Override public void serverBeforeStarting(FusekiServer server) {
        // Shiro requires a session handler.
        org.eclipse.jetty.server.Server jettyServer = server.getJettyServer();

//        try {
//          if ( jettyServer.getSessionIdManager() == null ) {
//          SessionIdManager idmanager = new DefaultSessionIdManager(jettyServer);
//          jettyServer.setSessionIdManager(idmanager);
//        } catch (RuntimeException ex) {
//            shiroConfigLog.error("Failed to set a session id nmanager - server aborted");
//            throw ex;
//        }

        try {
            ServletContextHandler servletContextHandler = ((ServletContextHandler)(jettyServer.getHandler()));
            if ( servletContextHandler.getSessionHandler() == null ) {
                SessionHandler sessionsHandler = new SessionHandler();
                servletContextHandler.setHandler(sessionsHandler);
            }
        } catch (RuntimeException ex) {
            shiroConfigLog.error("Failed to set a session manager - server aborted");
            throw ex;
        }
    }

//    @Override public void serverAfterStarting(FusekiServer server) {
//    @Override public void serverStopped(FusekiServer server) { }
//    @Override public void stop() {}

}
