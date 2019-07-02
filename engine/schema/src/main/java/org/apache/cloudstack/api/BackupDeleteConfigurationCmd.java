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

import com.cloud.exception.InvalidParameterValueException;
import com.cloud.user.Account;
import com.cloud.vm.snapshot.BackupManager;
import org.apache.log4j.Logger;

import javax.inject.Inject;

@APICommand(name = "deleteBackupConfiguration", description = "Remove an Backup Configuration", responseObject = BackupConfigurationResponse.class,
        requestHasSensitiveInfo = false, responseHasSensitiveInfo = false)
public class BackupDeleteConfigurationCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(BackupDeleteConfigurationCmd.class.getName());
    private static final String s_name = "backupconfigurationresponse";

    @Inject
    private BackupManager _backupManager;

    @Parameter(name = ApiConstants.NAME, type = CommandType.STRING, description = "Name")
    private String name;

    @Parameter(name = ApiConstants.VALUE, type = CommandType.STRING, description = "Value")
    private String value;

    @Parameter(name = ApiConstants.DESCRIPTION, type = CommandType.STRING, description = "Description")
    private String description;

    public BackupDeleteConfigurationCmd() {
        super();
    }

    public BackupDeleteConfigurationCmd(final BackupManager backupManager) {
        super();
        _backupManager = backupManager;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void execute() throws ServerApiException {
        try {
            final BackupConfigurationResponse response = _backupManager.deleteConfiguration(this);
            response.setObjectName("BackupDeleteConfiguration");
            response.setResponseName(getCommandName());
            setResponseObject(response);
        } catch (final InvalidParameterValueException e) {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, e.toString());
        }

    }

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM;
    }
}
