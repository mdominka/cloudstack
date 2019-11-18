-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlDialectInspectionForFile

-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--   http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.

--;
-- Schema upgrade from 4.13.0.0 to 4.14.0.0
--;

-- KVM: enable storage data motion on KVM hypervisor_capabilities
UPDATE `cloud`.`hypervisor_capabilities` SET `storage_motion_supported` = 1 WHERE `hypervisor_capabilities`.`hypervisor_type` = 'KVM';

-- S3 backup configuration
DROP TABLE IF EXISTS `cloud`.`backup_configuration`;

CREATE TABLE `cloud`.`backup_configuration` (
  `bucket` varchar(255) NOT NULL,
  `endpoint` varchar(255) NOT NULL,
  `access_key` varchar(255) NOT NULL,
  `secret_key` varchar(255) NOT NULL,
  `region` varchar(255) NOT NULL,
  `description` varchar(1024),
  PRIMARY KEY  (`bucket`, `endpoint`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `snapshots` ADD `sf_snapshot_id` BIGINT(20) UNSIGNED NULL DEFAULT NULL COMMENT 'Solidfire Snapshot ID';