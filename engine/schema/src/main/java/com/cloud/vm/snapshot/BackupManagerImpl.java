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

import static com.cloud.utils.db.SearchCriteria.Op.EQ;
import static java.util.Objects.nonNull;

import com.cloud.exception.InvalidParameterValueException;
import com.cloud.utils.Pair;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;
import com.cloud.vm.snapshot.dao.BackupConfigurationDao;
import org.apache.cloudstack.api.BackupAddConfigurationCmd;
import org.apache.cloudstack.api.BackupConfigurationResponse;
import org.apache.cloudstack.api.BackupDeleteConfigurationCmd;
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
    public BackupConfigurationResponse addConfiguration(final String name, final String value,
        final String description) throws InvalidParameterValueException {

        final BackupConfigurationVO configuration =
            _backupConfigurationDao.find(name, value, description);

        if (nonNull(configuration)){
            throw new InvalidParameterValueException("Duplicate configuration");
        }
        final BackupConfigurationVO entity = new BackupConfigurationVO(name, value, description);
        _backupConfigurationDao.persist(entity);

        return createBackupConfigurationResponse(entity);
    }

    @Override
    public BackupConfigurationResponse deleteConfiguration(final BackupDeleteConfigurationCmd cmd)
        throws InvalidParameterValueException {

        final String name = cmd.getName();
        final String value = cmd.getValue();
        final String description = cmd.getDescription();

        final SearchBuilder<BackupConfigurationVO> sb = _backupConfigurationDao.createSearchBuilder();
        sb.and("name", sb.entity().getName(), EQ);
        sb.and("value", sb.entity().getValue(), EQ);
        sb.and("description", sb.entity().getDescription(), EQ);
        sb.done();

        final SearchCriteria<BackupConfigurationVO> sc = sb.create();
        sc.setParameters("name", name);
        sc.setParameters("value", value);
        sc.setParameters("description", description);

        _backupConfigurationDao.remove(sc);

        final BackupConfigurationVO entity = new BackupConfigurationVO(name, value, description);
        return createBackupConfigurationResponse(entity);
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
        cmdList.add(BackupAddConfigurationCmd.class);
        cmdList.add(BackupDeleteConfigurationCmd.class);
        return cmdList;
    }
}
