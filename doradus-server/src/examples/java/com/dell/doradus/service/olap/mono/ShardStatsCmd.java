/*
 * Copyright (C) 2014 Dell, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dell.doradus.service.olap.mono;

import com.dell.doradus.common.ApplicationDefinition;
import com.dell.doradus.common.HttpMethod;
import com.dell.doradus.common.UNode;
import com.dell.doradus.olap.store.SegmentStats;
import com.dell.doradus.service.olap.OLAPService;
import com.dell.doradus.service.rest.UNodeOutCallback;
import com.dell.doradus.service.rest.annotation.Description;

@Description(
    name = "Stats",
    summary = "Returns statistics for the data in the 'mono' shard.",
    methods = HttpMethod.GET,
    uri = "/{application}/_stats",
    outputEntity = "stats"
)
public class ShardStatsCmd extends UNodeOutCallback {

    @Override
    public UNode invokeUNodeOut() {
        ApplicationDefinition appDef = m_request.getAppDef();
        SegmentStats stats = OLAPService.instance().getStats(appDef, OLAPMonoService.MONO_SHARD_NAME);
        return stats.toUNode();
    }

}   // class ShardStatsCmd
