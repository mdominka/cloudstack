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

package com.cloud.vm.backup;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloud.agent.api.to.S3TO;
import com.cloud.utils.component.ManagerBase;
import com.cloud.utils.storage.S3.S3Utils;
import com.cloud.vm.snapshot.BackupConfigurationVO;
import com.cloud.vm.snapshot.crypto.Aes;
import com.cloud.vm.snapshot.dao.BackupConfigurationDao;
import org.apache.cloudstack.api.command.user.backup.ListBackupCmd;
import org.apache.cloudstack.engine.subsystem.api.storage.DataStoreManager;
import org.apache.cloudstack.engine.subsystem.api.storage.PrimaryDataStore;
import org.apache.cloudstack.storage.datastore.db.StoragePoolDetailsDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

@Component
public class BackupManagerImpl extends ManagerBase implements BackupService {

    private static final String CLUSTER_PREFIX = "hci-cl01-nhjj/";
    private static final String S3_START_RECORD = "start";

    @Inject
    DataStoreManager _dataStoreMgr;
    @Inject
    private BackupConfigurationDao backupConfigurationDao;
    @Inject
    private StoragePoolDetailsDao storagePoolDetailsDao;

    @Override
    public List<S3ObjectSummary> listBackups(ListBackupCmd cmd) {
        List<PrimaryDataStore> stores =_dataStoreMgr.listPrimaryDataStores();

        final BackupConfigurationVO config = backupConfigurationDao.listAll().get(0);

        if (Objects.isNull(config)){
            return new ArrayList<>();
        }
        final S3TO s3TO = new S3TO();
        s3TO.setSecretKey(Aes.decrypt(config.getSecretKey()));
        s3TO.setAccessKey(config.getAccessKey());
        s3TO.setEndPoint(config.getEndpoint());
        s3TO.setBucketName(config.getBucket());
        s3TO.setHttps(true);
        s3TO.setRegion(config.getRegion());

        return doFilterS3Objects(S3Utils.listDirectory(s3TO, config.getBucket(), CLUSTER_PREFIX));
    }

    private List<S3ObjectSummary> doFilterS3Objects(final List<S3ObjectSummary> listDirectory) {
      return listDirectory.stream().filter( s ->
          s.getKey().toLowerCase().contains(S3_START_RECORD)).collect(Collectors.toList());
    }
}
