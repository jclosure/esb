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

/**
 * @author <a href="http://www.christianposta.com/blog">Christian Posta</a>
 */
public class FileTransferRouteBuilder extends RouteBuilder{

    public static final int DEFAULT_POLL_DELAY = 5;

    // allow us to completely mock out the endpoint...
    private String ftpUri;
    private String remoteSinkUri;

    private int filePollDelay = DEFAULT_POLL_DELAY;
    private String workingFilePath;
    private String completedFilePath;
    private String failedFilePath;
    private String privateKeyPath;
    private String privateKeyPassword;
    private String localWorkingFilePath;

    @Override
    public void configure() throws Exception {
        from(getFtpUri())
                // we can do some interesting things here...
                .log("we got a file! ${header.CamelFileName}")
                .to(getRemoteSinkUri())
                .log("completd send!");
    }

    private String getRemoteSinkUri() {
        if (this.remoteSinkUri == null) {
            return "sftp://cloudlinux06.amd.com/data?username=root&password=pass@word1";
        }
        return remoteSinkUri;
    }

    public void setRemoteSinkUri(String remoteSinkUri) {
        this.remoteSinkUri = remoteSinkUri;
    }

    public String getFtpUri() {
        if (ftpUri == null) {
            return buildFtpUri();
        }
        return ftpUri;
    }

    private String buildFtpUri() {

        return "sftp://root@cloudlinux04.amd.com/data"
                + "?delay=" + filePollDelay * 1000
//                + "&preMove=" + workingFilePath
                + "&move=" + completedFilePath
                + "&moveFailed=" + failedFilePath
                + "&privateKeyFile=" + privateKeyPath
                + "&privateKeyFilePassphrase=" + privateKeyPassword
                + "&localWorkDirectory=" + localWorkingFilePath
                // let's grab only one file at a time.. these could be large
//                + "&antInclude="
//                + "&maxMessagesPerPoll=1"
                + "&eagerMaxMessagesPerPoll=true"
                + "&recursive=true"
                + "&binary=true";
    }

    public void setFtpUri(String ftpUri) {
        this.ftpUri = ftpUri;
    }

    public int getFilePollDelay() {
        return filePollDelay;
    }

    public void setFilePollDelay(int filePollDelay) {
        this.filePollDelay = filePollDelay;
    }

    public String getWorkingFilePath() {
        return workingFilePath;
    }

    public void setWorkingFilePath(String workingFilePath) {
        this.workingFilePath = workingFilePath;
    }

    public String getCompletedFilePath() {
        return completedFilePath;
    }

    public void setCompletedFilePath(String completedFilePath) {
        this.completedFilePath = completedFilePath;
    }

    public String getFailedFilePath() {
        return failedFilePath;
    }

    public void setFailedFilePath(String failedFilePath) {
        this.failedFilePath = failedFilePath;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }

    public void setPrivateKeyPassword(String privateKeyPassword) {
        this.privateKeyPassword = privateKeyPassword;
    }

    public String getLocalWorkingFilePath() {
        return localWorkingFilePath;
    }

    public void setLocalWorkingFilePath(String localWorkingFilePath) {
        this.localWorkingFilePath = localWorkingFilePath;
    }
}
