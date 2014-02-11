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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.datacontract.schemas._2004._07.webapplication1.DataObject;
import org.datacontract.schemas._2004._07.webapplication1.Receipt;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="http://www.christianposta.com/blog">Christian Posta</a>
 */
public class WCFServiceProxyTest extends CamelSpringTestSupport {

    @Test
    public void testWCFHelloWorldService() throws InterruptedException {
        MockEndpoint resultMockEndpoint = getMockEndpoint("mock:backend");
        resultMockEndpoint.whenAnyExchangeReceived(new Processor() {

            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("Hello World");
            }
        });
        resultMockEndpoint.expectedMessageCount(1);

        Exchange result = template.send("cxf:bean:cxfProxy", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(new ArrayList());
                exchange.getIn().setHeader(CxfConstants.OPERATION_NAME, "HelloWorld");
                exchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, "http://tempuri.org/");
            }
        });

        if (result.getException() != null) {
            result.getException().printStackTrace();
        }
        assertTrue("We had an unexpected exception!", result.getException() == null);

        assertMockEndpointsSatisfied(5, TimeUnit.SECONDS);
        resultMockEndpoint.reset();
    }

    @Test
    public void testWCFHelloService() throws InterruptedException {
        MockEndpoint resultMockEndpoint = getMockEndpoint("mock:backend");
        resultMockEndpoint.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                String body = exchange.getIn().getBody(String.class);
                exchange.getIn().setBody("Hello, " + body);
            }
        });
        resultMockEndpoint.expectedMessageCount(1);

        Exchange result = template.send("cxf:bean:cxfProxy", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                ArrayList<String> params = new ArrayList<String>();
                params.add("ceposta");
                exchange.getIn().setBody(params);
                exchange.getIn().setHeader(CxfConstants.OPERATION_NAME, "Hello");
                exchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, "http://tempuri.org/");
            }
        });

        assertTrue("We had an unexpected exception!", result.getException() == null);

        assertMockEndpointsSatisfied(5, TimeUnit.SECONDS);
        resultMockEndpoint.reset();

    }

    @Test
    public void testWCFProcessService() throws InterruptedException {
        MockEndpoint resultMockEndpoint = getMockEndpoint("mock:backend");
        resultMockEndpoint.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Receipt rc = new Receipt();
                rc.setSuccess(true);
                exchange.getIn().setBody(rc);
            }
        });
        resultMockEndpoint.expectedMessageCount(1);

        Exchange result = template.send("cxf:bean:cxfProxy", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                ArrayList<Object> params = new ArrayList<Object>();
                DataObject dataObject = new DataObject();
                dataObject.setX003CIdX003EKBackingField("ceposta-123");
                dataObject.setX003CNameX003EKBackingField("ceposta");
                params.add(dataObject);
                exchange.getIn().setBody(params);
                exchange.getIn().setHeader(CxfConstants.OPERATION_NAME, "Process");
                exchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, "http://tempuri.org/");
            }
        });

        assertTrue("We had an unexpected exception!", result.getException() == null);
        assertMockEndpointsSatisfied(5, TimeUnit.SECONDS);
        resultMockEndpoint.reset();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        WCFServiceProxyRouteBuilder rc = new WCFServiceProxyRouteBuilder();
        rc.setToBackendService("mock:backend");
        return rc;
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/spring-context-proxy-test.xml");
    }
}
