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

import com.cloud.utils.db.Encrypt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "backup_configuration")
public class BackupConfigurationVO {

    @Id
    @Column(name = "bucket")
    private String bucket;

    @Id
    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "access_key")
    private String accessKey;

    @Encrypt
    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "description")
    private String description;

    public BackupConfigurationVO() {
    }

    public BackupConfigurationVO(final String bucket, final String regionEndpoint,
        final String accessKey, final String secretKey, final String description) {
        this.bucket = bucket;
        this.endpoint = regionEndpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.description = description;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() { return accessKey; }

    public void setAccessKey(final String accessKey) { this.accessKey = accessKey; }

    public String getSecretKey() { return secretKey; }

    public void setSecretKey(final String secretKey) { this.secretKey = secretKey; }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
