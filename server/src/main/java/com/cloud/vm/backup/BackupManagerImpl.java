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

import static java.util.Collections.emptyList;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloud.agent.api.to.S3TO;
import com.cloud.utils.component.ManagerBase;
import com.cloud.utils.storage.S3.S3Utils;
import com.cloud.vm.snapshot.BackupConfigurationVO;
import com.cloud.vm.snapshot.crypto.Aes;
import com.cloud.vm.snapshot.dao.BackupConfigurationDao;
import org.apache.cloudstack.api.command.user.backup.ListBackupCmd;
import org.apache.cloudstack.storage.datastore.db.StoragePoolDetailVO;
import org.apache.cloudstack.storage.datastore.db.StoragePoolDetailsDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

@Component
public class BackupManagerImpl extends ManagerBase implements BackupService {

  private static final String CLUSTER_PREFIX_DEFAULT = "hci-cl01-nhjj/";
  private static final String S3_MANIFEST_RECORD = "manifest";
  private static final String CLUSTER_PREFIX = "clusterPrefix";
  private static final Character SEPARATOR = '/';

  @Inject
  private BackupConfigurationDao backupConfigurationDao;
  @Inject
  private StoragePoolDetailsDao storagePoolDetailsDao;

  @Override
  public List<S3ObjectSummary> listBackups(final ListBackupCmd cmd) {
    final List<BackupConfigurationVO> config = backupConfigurationDao.listAll();

    if (isEmpty(config)) {
      return emptyList();
    }

    final S3TO s3TO = buildS3Object(config);

    final List<StoragePoolDetailVO> storagePoolDetails =
        storagePoolDetailsDao.findDetails(CLUSTER_PREFIX, null, null);

    if (isNotEmpty(storagePoolDetails)) {
      final List<S3ObjectSummary> s3ObjectSummaries = new ArrayList<>();
      storagePoolDetails.stream().map(StoragePoolDetailVO::getValue)
          .forEach(value -> s3ObjectSummaries.addAll(doFilterS3Objects(S3Utils.listDirectory(s3TO,
              config.get(0).getBucket(), value + SEPARATOR))));

      return s3ObjectSummaries;
    }
    return doFilterS3Objects(S3Utils.listDirectory(s3TO, config.get(0).getBucket(), CLUSTER_PREFIX_DEFAULT));
  }

  private List<S3ObjectSummary> doFilterS3Objects(final List<S3ObjectSummary> listDirectory) {
    return listDirectory.stream()
        .filter(s -> s.getKey().toLowerCase().contains(S3_MANIFEST_RECORD))
        .collect(Collectors.toList());
  }

  private S3TO buildS3Object(final List<BackupConfigurationVO> config) {
    final S3TO s3TO = new S3TO();
    s3TO.setSecretKey(Aes.decrypt(config.get(0).getSecretKey()));
    s3TO.setAccessKey(config.get(0).getAccessKey());
    s3TO.setEndPoint(config.get(0).getEndpoint());
    s3TO.setBucketName(config.get(0).getBucket());
    s3TO.setHttps(true);
    s3TO.setRegion(config.get(0).getRegion());

    return s3TO;
  }
}
