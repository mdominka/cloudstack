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

package org.apache.cloudstack.api.command.user.backup;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloud.vm.backup.Backup;
import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListTaggedResourcesCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.response.BackupResponse;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.cloudstack.api.response.S3BackupResponse;

import java.util.ArrayList;
import java.util.List;

@APICommand(name = "listS3Backups", description = "List backups by conditions", responseObject = BackupResponse.class, since = "4.2.0", entityType = {Backup.class},
        requestHasSensitiveInfo = false, responseHasSensitiveInfo = false)
public class ListBackupCmd extends BaseListTaggedResourcesCmd {

    private static final String s_name = "lists3backupsresponse";

    @Parameter(name = ApiConstants.ID, type = CommandType.UUID, entityType = BackupResponse.class, description = "lists backup snapshot by eTag ID")
    private Long id;

    @Parameter(name = ApiConstants.NAME, type = CommandType.STRING, description = "lists snapshot by snapshot name")
    private String snapshotName;

    @Parameter(name = ApiConstants.VOLUME_ID, type = CommandType.LONG, description = "the ID of the disk volume")
    private Long volumeId;

    @Parameter(name = ApiConstants.VOLUME_NAME, type = CommandType.STRING, description = "the name of the disk volume")
    private String volumeName;

    @Parameter(name = ApiConstants.SNAPSHOT_ID, type = CommandType.LONG, description = "the id of"
        + " the snapshot")
    private Long snapshotId;

    @Parameter(name = ApiConstants.CREATED, type = CommandType.STRING, description = "the creation date of the snapshot")
    private String creationDate;

    @Parameter(name = ApiConstants.SNAPSHOT_TYPE, type = CommandType.STRING,
        description = "the recurring type of the snapshot: manual, hourly, daily, weekly, monthly")
    private String snapshotType;

    @Override
    public void execute() {
        final List<S3ObjectSummary> result = _BackupService.listBackups(this);
        final ListResponse<S3BackupResponse> response = new ListResponse<>();
        final List<S3BackupResponse> backupResponses = new ArrayList<>();

        for (final S3ObjectSummary s3Object : result) {
          final S3BackupResponse backupResponse = _responseGenerator.createBackupResponse(s3Object);
          backupResponse.setObjectName("backup");
          backupResponses.add(backupResponse);
        }

        response.setResponses(backupResponses);
        response.setResponseName(getCommandName());
        setResponseObject(response);
    }

    @Override
    public String getCommandName() {
        return s_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(final String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public Long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(final Long volumeId) {
        this.volumeId = volumeId;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(final String volumeName) {
        this.volumeName = volumeName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }

    public String getSnapshotType() {
        return snapshotType;
    }

    public void setSnapshotType(final String snapshotType) {
        this.snapshotType = snapshotType;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(final Long snapshotId) {
        this.snapshotId = snapshotId;
    }
}
