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

import com.cloud.vm.snapshot.dao.BackupConfigurationDao;
import org.apache.cloudstack.framework.config.ConfigKey;
import org.apache.cloudstack.framework.config.Configurable;
import org.apache.cloudstack.framework.config.dao.ConfigurationDao;
import javax.inject.Inject;

public class BackupConfiguration implements Configurable {

    @Inject
    private BackupConfigurationDao _backupConfigurationDao;

    public BackupConfiguration() {
    }

    public BackupConfiguration(final BackupConfigurationDao backupConfigurationDao) {
        _backupConfigurationDao = backupConfigurationDao;
    }

    @Deprecated
    public BackupConfiguration(final ConfigurationDao configDao, final BackupConfigurationDao backupConfigurationDao) {
        _backupConfigurationDao = backupConfigurationDao;
    }

    @Override
    public String getConfigComponentName() {
        return BackupConfiguration.class.getSimpleName();
    }

    @Override
    public ConfigKey<?>[] getConfigKeys() {
        return new ConfigKey[0];
    }
}
