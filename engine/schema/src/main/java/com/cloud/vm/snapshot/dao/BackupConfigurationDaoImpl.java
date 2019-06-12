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
    private final SearchBuilder<BackupConfigurationVO> nameSearch;

    public BackupConfigurationDaoImpl() {
        super();
        nameSearch = createSearchBuilder();
        nameSearch.and("name", nameSearch.entity().getName(), Op.EQ);
        nameSearch.done();
    }

    @Override
    public BackupConfigurationVO findByName(final String name) {
        final SearchCriteria<BackupConfigurationVO> sc = nameSearch.create();
        sc.setParameters("name", name);
        return findOneBy(sc);
    }

    @Override
    public BackupConfigurationVO find(String name, String value, String description) {
        SearchCriteria<BackupConfigurationVO> sc = getSearchCriteria(name, value, description);
        return findOneBy(sc);
    }

    @Override
    public Pair<List<BackupConfigurationVO>, Integer> searchConfigurations(String name, String value, String description) {
        SearchCriteria<BackupConfigurationVO> sc = getSearchCriteria(name, value, description);
        return searchAndCount(sc, null);
    }

    private SearchCriteria<BackupConfigurationVO> getSearchCriteria(String name, String value, String description) {
        SearchCriteria<BackupConfigurationVO> sc;
        sc = nameSearch.create();
        if (name != null) {
            sc.setParameters("name", name);
        }
        if (value != null) {
            sc.setParameters("value", value);
        }
        if (description != null) {
            sc.setParameters("description", description);
        }
        return sc;
    }
}
