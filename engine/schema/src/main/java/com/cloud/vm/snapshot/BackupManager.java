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

import com.cloud.exception.InvalidParameterValueException;
import com.cloud.utils.Pair;
import com.cloud.utils.component.PluggableService;
import org.apache.cloudstack.api.BackupConfigurationResponse;
import org.apache.cloudstack.api.BackupDeleteConfigurationCmd;
import org.apache.cloudstack.api.BackupListConfigurationCmd;

import java.util.List;

public interface BackupManager extends PluggableService {
  BackupConfigurationResponse createBackupConfigurationResponse(
      BackupConfigurationVO configuration);

  BackupConfigurationResponse addConfiguration(String bucket, String endpoint, String accessKey,
      String secretKey, String region, String description)
      throws InvalidParameterValueException;

  BackupConfigurationResponse deleteConfiguration(BackupDeleteConfigurationCmd cmd)
      throws InvalidParameterValueException;

  Pair<List<? extends BackupConfigurationVO>, Integer> listConfigurations(
      BackupListConfigurationCmd cmd);

}
