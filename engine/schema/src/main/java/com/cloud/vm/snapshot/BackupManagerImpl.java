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
package com.cloud.vm.snapshot;

import com.cloud.utils.Pair;
import com.cloud.vm.snapshot.dao.BackupConfigurationDao;
import org.apache.cloudstack.api.BackupConfigurationResponse;
import org.apache.cloudstack.api.BackupListConfigurationCmd;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@Component
public class BackupManagerImpl implements BackupManager {
    private static final Logger s_logger = Logger.getLogger(BackupManagerImpl.class.getName());

    @Inject
    private BackupConfigurationDao _backupConfigurationDao;

    @Inject
    private BackupConfiguration _backupConfiguration;

    public BackupManagerImpl() {
        super();
    }

    public BackupManagerImpl(final BackupConfigurationDao backupConfigurationDao,
                           final BackupConfiguration backupConfiguration) {
        super();
        _backupConfigurationDao = backupConfigurationDao;
        _backupConfiguration = backupConfiguration;
    }

    @Override
    public BackupConfigurationResponse createBackupConfigurationResponse(final BackupConfigurationVO configuration) {
        return new BackupConfigurationResponse(configuration.getName(), configuration.getValue(), configuration.getDescription());
    }

    @Override
    public Pair<List<? extends BackupConfigurationVO>, Integer> listConfigurations(final BackupListConfigurationCmd cmd) {
        final String name = cmd.getName();
        final String value = cmd.getValue();
        final String description = cmd.getDescription();
        final Pair<List<BackupConfigurationVO>, Integer> result = _backupConfigurationDao.searchConfigurations(name, value, description);
        return new Pair<List<? extends BackupConfigurationVO>, Integer>(result.first(), result.second());
    }

    @Override
    public List<Class<?>> getCommands() {
        final List<Class<?>> cmdList = new ArrayList<Class<?>>();
        cmdList.add(BackupListConfigurationCmd.class);
        return cmdList;
    }
}
