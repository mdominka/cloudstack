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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

@Component
public class BackupManagerImpl extends ManagerBase implements BackupService {

    private static final String CLUSTER_PREFIX = "hci-cl01-nhjj/";

    @Inject
    DataStoreManager _dataStoreMgr;
    @Inject
    private BackupConfigurationDao backupConfigurationDao;

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

        final List<S3ObjectSummary> s3Objects =
            S3Utils.listDirectory(s3TO, config.getBucket(), CLUSTER_PREFIX);

        return s3Objects;

//        String accessKey = "accessKey1";
//        String secretKey = "verySecretKey1";
//        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
//
//        ClientConfiguration clientConfig = new ClientConfiguration();
//        clientConfig.setProtocol(Protocol.HTTPS);
//
//        AmazonS3 conn = new AmazonS3Client(credentials, clientConfig);
//        conn.setEndpoint("sr03.cs.ewerk.com");
//
//        String prefix = "hci-cl01-nhjj";
//
//        String delimiter = "/";
//        if (!prefix.endsWith(delimiter)) {
//          prefix += delimiter;
//        }
//
//
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
//            .withBucketName("zenko").withPrefix(prefix)
//            .withDelimiter(delimiter);
//
//        ObjectListing objects = conn.listObjects(listObjectsRequest);
//        List<S3ObjectSummary> myList = new ArrayList<S3ObjectSummary>();
//
//        myList.addAll(objects.getObjectSummaries());
//
//        return myList;
    }
}
