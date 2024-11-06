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

import java.util.concurrent.atomic.LongAdder;

public class NotesFusekiModServer {

    static public class Counter2 {
        // Not for synchronization
        private LongAdder counter = new LongAdder();

        public Counter2()    {}

        public void inc()   { counter.increment(); }
        public void dec()   { counter.decrement(); }
        public long value() { return counter.longValue(); }

        @Override
        public String toString() {
            return counter.toString();
        }
    }

    // 1Fuseki v1
    //  in jena-fuseki-main : org.apache.jena.fuseki.fmods
    //  FMod_UI
    //  FMod_Admin
    //  FMod_Shiro
    //  (FMod_GeoSPARQL)
    //  FMod_Prometheus


    // Copy template files.
    //   mod_shiro uses FUSEKI_SHIRO
    // 1/ Server config.ttl
    // 2/ New dataset? - write config.
    // FusekiApp

    // "Super module"
    // Environment variables policy
    // Sort out "FUSEKI_BASE", "FUSEKI_HOME" -- check javadoc

    // [ ] AdminAllowed:
    //     "--admin user=password" or "--admin local"
    //     "--adminBase run/"
    // [ ] Clean up FusekiApp - is all that code needed?
    // [ ] Port tests

    // == Build
    // Mods - make without fuseki
    // jena-fuseki-mods - excludes fuseki main

    // == mod-admin
    // [ ] Not updating configurations?
    // [ ] Crashes due to NPE adding argument.

    //  == mod-shiro
    // [ ] SHIRO_FILE environment variable
    // [ ] (optional) Log access failures.
    // Shiro on/off
    // Config only version (no create datasets)

    // == fuseki-mod-server
    // [ ]
    // [ ] Just make a combined jar file?!

}
