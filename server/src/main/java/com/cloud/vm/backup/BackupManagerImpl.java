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

import static java.util.Objects.isNull;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloud.agent.api.to.S3TO;
import com.cloud.utils.component.ManagerBase;
import com.cloud.utils.storage.S3.S3Utils;
import com.cloud.vm.snapshot.BackupConfigurationVO;
import com.cloud.vm.snapshot.crypto.Aes;
import com.cloud.vm.snapshot.dao.BackupConfigurationDao;
import org.apache.cloudstack.api.command.user.backup.ListBackupCmd;
import org.apache.cloudstack.storage.datastore.db.StoragePoolDetailsDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

@Component
public class BackupManagerImpl extends ManagerBase implements BackupService {

  private static final String CLUSTER_PREFIX = "hci-cl01-nhjj/";
  private static final String S3_MANIFEST_RECORD = "manifest";

  @Inject
  private BackupConfigurationDao backupConfigurationDao;
  @Inject
  private StoragePoolDetailsDao storagePoolDetailsDao;

  @Override
  public List<S3ObjectSummary> listBackups(final ListBackupCmd cmd) {
    final List<BackupConfigurationVO> config = backupConfigurationDao.listAll();

    if (isNull(config) || config.isEmpty()) {
      return new ArrayList<>();
    }
    final S3TO s3TO = new S3TO();
    s3TO.setSecretKey(Aes.decrypt(config.get(0).getSecretKey()));
    s3TO.setAccessKey(config.get(0).getAccessKey());
    s3TO.setEndPoint(config.get(0).getEndpoint());
    s3TO.setBucketName(config.get(0).getBucket());
    s3TO.setHttps(true);
    s3TO.setRegion(config.get(0).getRegion());

    return doFilterS3Objects(
        S3Utils.listDirectory(s3TO, config.get(0).getBucket(), CLUSTER_PREFIX));
  }

  private List<S3ObjectSummary> doFilterS3Objects(final List<S3ObjectSummary> listDirectory) {
    return listDirectory.stream()
        .filter(s -> s.getKey().toLowerCase().contains(S3_MANIFEST_RECORD))
        .collect(Collectors.toList());
  }
}
