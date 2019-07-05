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
package com.cloud.vm.snapshot.dao;

import static java.util.Objects.nonNull;

import com.cloud.utils.Pair;
import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;
import com.cloud.utils.db.SearchCriteria.Op;
import com.cloud.vm.snapshot.BackupConfigurationVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackupConfigurationDaoImpl extends GenericDaoBase<BackupConfigurationVO, Long> implements
    BackupConfigurationDao {

    public BackupConfigurationDaoImpl() {
    }

    @Override
    public BackupConfigurationVO findByName(final String bucket) {
        final SearchCriteria<BackupConfigurationVO> sc = getSearchCriteria(bucket, null, null);
        return findOneBy(sc);
    }

    @Override
    public BackupConfigurationVO find(final String bucket, final String endpoint,
        final String accessKey) {
        final SearchCriteria<BackupConfigurationVO> sc = getSearchCriteria(bucket, endpoint, accessKey);
        return findOneBy(sc);
    }

    @Override
    public Pair<List<BackupConfigurationVO>, Integer> searchConfigurations(final String bucket,
        final String endpoint, final String accessKey) {
        final SearchCriteria<BackupConfigurationVO> sc = getSearchCriteria(bucket, endpoint,
            accessKey);
        return searchAndCount(sc, null);
    }

    private SearchCriteria<BackupConfigurationVO> getSearchCriteria(final String bucket,
        final String endpoint, final String accessKey) {

        final SearchBuilder<BackupConfigurationVO> searchBuilder = getSearchBuilder(bucket, endpoint,
            accessKey);
        final SearchCriteria<BackupConfigurationVO> searchCriteria = searchBuilder.create();
        if (nonNull(bucket)) {
            searchCriteria.setParameters("bucket", bucket);
        }
        if (nonNull(endpoint)) {
            searchCriteria.setParameters("endpoint", endpoint);
        }
        if (nonNull(accessKey)) {
            searchCriteria.setParameters("access_key", accessKey);
        }
        return searchCriteria;
    }

    private SearchBuilder<BackupConfigurationVO> getSearchBuilder(final String bucket,
        final String endpoint, final String accessKey){

        final SearchBuilder<BackupConfigurationVO> search = createSearchBuilder();
        if (nonNull(bucket)){
            search.and("bucket", search.entity().getBucket(), Op.EQ);
        }
        if (nonNull(endpoint)){
            search.and("endpoint", search.entity().getEndpoint(), Op.EQ);
        }
        if (nonNull(accessKey)){
            search.and("access_key", search.entity().getAccessKey(), Op.EQ);
        }
        search.done();
        return search;
    }

}
