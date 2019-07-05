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

import com.cloud.serializer.Param;
import com.cloud.vm.snapshot.BackupConfiguration;
import com.google.gson.annotations.SerializedName;

@EntityReference(value = BackupConfiguration.class)
public class BackupConfigurationResponse extends BaseResponse {
    @SerializedName(ApiConstants.S3_BUCKET_NAME)
    @Param(description = "bucket name of the backup configuration")
    private String bucket;

    @SerializedName(ApiConstants.S3_END_POINT)
    @Param(description = "endpoint of the backup configuration")
    private String endpoint;

    @SerializedName(ApiConstants.S3_ACCESS_KEY)
    @Param(description = "access key of the backup configuration")
    private String accessKey;

    @SerializedName(ApiConstants.S3_SECRET_KEY)
    @Param(description = "secret key of the backup configuration")
    private String secretKey;

    @SerializedName(ApiConstants.DESCRIPTION)
    @Param(description = "description of the backup configuration")
    private String description;

    public BackupConfigurationResponse() {
        super();
    }

    public BackupConfigurationResponse(final String bucket, final String endpoint,
        final String accessKey, final String secretKey) {
        this(bucket, endpoint, accessKey, secretKey, null);
    }

    public BackupConfigurationResponse(final String bucket, final String endpoint,
        final String accessKey, final String secretKey, final String description) {
        this.bucket = bucket;
        this.endpoint = endpoint;
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

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(final String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
