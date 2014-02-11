/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amd.poc.routes;

import org.apache.camel.builder.RouteBuilder;

import java.util.ArrayList;

/**
 * @author <a href="http://www.christianposta.com/blog">Christian Posta</a>
 */
public class WCFServiceRouteBuilder extends RouteBuilder {

    private String toEndpoint;

    @Override
    public void configure() throws Exception {
        from("direct:helloworld")
                .log("about to send a request to the HelloWorld service..")
                .enrich("cxf:bean:wcfServiceEndpoint")
                .log("woohoo! got a reponse: ${body}")
                .to(getToEndpoint());

    }

    public void setToEndpoint(String toEndpoint) {
        this.toEndpoint = toEndpoint;
    }

    public String getToEndpoint() {
        if (toEndpoint == null) {
            return "log:com.amd.poc.routes.PingWCFServiceRouteBuilder?showBody=true";
        } else {
            return toEndpoint;
        }
    }
}
