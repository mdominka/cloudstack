// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.api;

import com.cloud.user.Account;
import com.cloud.utils.Pair;
import com.cloud.vm.snapshot.BackupConfigurationVO;
import com.cloud.vm.snapshot.BackupManager;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@APICommand(name = "listBackupConfigurations", responseObject = BackupConfigurationResponse.class, description = "Lists all Backup configurations",
        requestHasSensitiveInfo = false, responseHasSensitiveInfo = false)
public class BackupListConfigurationCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(BackupListConfigurationCmd.class.getName());

    private static final String s_name = "backupconfigurationresponse";

    @Inject
    private BackupManager _backupManager;

    @Parameter(name = ApiConstants.S3_BUCKET_NAME, type = CommandType.STRING, description = "Bucket")
    private String bucket;

    @Parameter(name = ApiConstants.S3_END_POINT, type = CommandType.STRING, description = "Endpoint")
    private String endpoint;

    @Parameter(name = ApiConstants.S3_ACCESS_KEY, type = CommandType.STRING, description = "Accesskey")
    private String accessKey;

    @Parameter(name = ApiConstants.S3_SECRET_KEY, type = CommandType.STRING, description = "Secretkey")
    private String secretKey;

    @Parameter(name = ApiConstants.DESCRIPTION, type = CommandType.STRING, description = "Description")
    private String description;

    public BackupListConfigurationCmd() {
        super();
    }

    public BackupListConfigurationCmd(final BackupManager backupManager) {
        super();
        _backupManager = backupManager;
    }

    private List<BackupConfigurationResponse> createBackupConfigurationResponses(final List<? extends BackupConfigurationVO> configurations) {
        final List<BackupConfigurationResponse> responses = new ArrayList<BackupConfigurationResponse>();
        for (final BackupConfigurationVO resource : configurations) {
            final BackupConfigurationResponse configurationResponse = _backupManager.createBackupConfigurationResponse(resource);
            configurationResponse.setObjectName("BackupConfiguration");
            responses.add(configurationResponse);
        }
        return responses;
    }

    @Override
    public void execute() {
        final Pair<List<? extends BackupConfigurationVO>, Integer> result = _backupManager.listConfigurations(this);
        final List<BackupConfigurationResponse> responses = createBackupConfigurationResponses(result.first());
        final ListResponse<BackupConfigurationResponse> response = new ListResponse<BackupConfigurationResponse>();
        response.setResponses(responses, result.second());
        response.setResponseName(getCommandName());
        setResponseObject(response);
    }

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() { return accessKey; }

    public void setAccessKey(final  String accessKey) { this.accessKey = accessKey;}

    public String getSecretKey() { return  secretKey; }

    public void setSecretKey(final String secretKey) { this.secretKey = secretKey;}

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
