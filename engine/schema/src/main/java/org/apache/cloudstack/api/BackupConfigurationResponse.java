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
    @SerializedName(ApiConstants.NAME)
    @Param(description = "name of the backup configuration")
    private String name;

    @SerializedName(ApiConstants.VALUE)
    @Param(description = "value of the backup configuration")
    private String value;

    @SerializedName(ApiConstants.DESCRIPTION)
    @Param(description = "description of the backup configuration")
    private String description;

    public BackupConfigurationResponse() {
        super();
    }

    public BackupConfigurationResponse(final String name) {
        super();
        setName(name);
    }

    public BackupConfigurationResponse(final String name, final String value) {
        this(name);
        setValue(value);
    }

    public BackupConfigurationResponse(final String name, final String value, final String description) {
        this(name, value);
        setDescription(description);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
