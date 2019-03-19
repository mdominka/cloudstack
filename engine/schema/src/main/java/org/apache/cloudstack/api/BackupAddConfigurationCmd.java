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

@APICommand(name = "addBackupConfiguration", description = "Add a new Backup Configuration", responseObject = BackupConfigurationResponse.class,
        requestHasSensitiveInfo = false, responseHasSensitiveInfo = false)
public class BackupAddConfigurationCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(BackupAddConfigurationCmd.class.getName());
    private static final String s_name = "backupconfigurationresponse";

    @Inject
    private BackupManager _backupManager;

    @Parameter(name = ApiConstants.NAME, type = CommandType.STRING, required = true, description = "Name")
    private String name;

    @Parameter(name = ApiConstants.VALUE, type = CommandType.STRING, required = true, description = "Value")
    private String value;

    @Parameter(name = ApiConstants.DESCRIPTION, type = CommandType.STRING, required = false, description = "Description")
    private String description;

    public BackupAddConfigurationCmd() {
        super();
    }

    public BackupAddConfigurationCmd(final BackupManager backupManager) {
        super();
        _backupManager = backupManager;
    }

    @Override
    public void execute() throws ServerApiException {
        try {
            final BackupConfigurationResponse response = _backupManager.addConfiguration(name, value, description);
            response.setObjectName("BackupAddConfiguration");
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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
