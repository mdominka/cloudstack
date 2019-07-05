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
    }

    public BackupManagerImpl(final BackupConfigurationDao backupConfigurationDao,
                           final BackupConfiguration backupConfiguration) {
        _backupConfigurationDao = backupConfigurationDao;
        _backupConfiguration = backupConfiguration;
    }

    @Override
    public BackupConfigurationResponse createBackupConfigurationResponse(final BackupConfigurationVO configuration) {
        return new BackupConfigurationResponse(configuration.getBucket(),
            configuration.getEndpoint(), configuration.getAccessKey(),
            configuration.getSecretKey(), configuration.getDescription());
    }

    @Override
    public BackupConfigurationResponse addConfiguration(final String bucket, final String endpoint,
        final String accessKey, final String secretKey, final String description)
        throws InvalidParameterValueException {

        final BackupConfigurationVO entity = new BackupConfigurationVO(bucket, endpoint, accessKey,
            secretKey, description);

        try {
            _backupConfigurationDao.persist(entity);
        } catch (final Exception e) {
            throw new InvalidParameterValueException("Duplicate configuration");
        }
        return createBackupConfigurationResponse(entity);
    }

    @Override
    public BackupConfigurationResponse deleteConfiguration(final BackupDeleteConfigurationCmd cmd)
        throws InvalidParameterValueException {

        final String bucket = cmd.getBucket();
        final String endpoint = cmd.getEndpoint();
        final String accessKey = cmd.getAccessKey();
        final String secretKey = cmd.getSecretKey();
        final String description = cmd.getDescription();

        final SearchBuilder<BackupConfigurationVO> sb = _backupConfigurationDao.createSearchBuilder();
        sb.and("bucket", sb.entity().getBucket(), EQ);
        sb.and("endpoint", sb.entity().getEndpoint(), EQ);
        sb.done();

        final SearchCriteria<BackupConfigurationVO> sc = sb.create();
        sc.setParameters("bucket", bucket);
        sc.setParameters("endpoint", endpoint);

        _backupConfigurationDao.remove(sc);

        final BackupConfigurationVO entity = new BackupConfigurationVO(bucket, endpoint, accessKey,
            secretKey, description);
        return createBackupConfigurationResponse(entity);
    }

    @Override
    public Pair<List<? extends BackupConfigurationVO>, Integer> listConfigurations(final BackupListConfigurationCmd cmd) {
        final String bucket = cmd.getBucket();
        final String endpoint = cmd.getEndpoint();
        final String accessKey = cmd.getAccessKey();
        final Pair<List<BackupConfigurationVO>, Integer> result =
            _backupConfigurationDao.searchConfigurations(bucket, endpoint, accessKey);
        return new Pair<>(result.first(), result.second());
    }

    @Override
    public List<Class<?>> getCommands() {
        final List<Class<?>> cmdList = new ArrayList<>();
        cmdList.add(BackupListConfigurationCmd.class);
        cmdList.add(BackupAddConfigurationCmd.class);
        cmdList.add(BackupDeleteConfigurationCmd.class);
        return cmdList;
    }
}
