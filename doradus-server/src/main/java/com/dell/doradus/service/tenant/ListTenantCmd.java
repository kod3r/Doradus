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

package com.dell.doradus.service.tenant;

import com.dell.doradus.common.HttpMethod;
import com.dell.doradus.common.UNode;
import com.dell.doradus.service.rest.NotFoundException;
import com.dell.doradus.service.rest.UNodeOutCallback;
import com.dell.doradus.service.rest.annotation.Description;

@Description(
    name = "ListTenant",
    summary = "List a specific tenant's details.",
    methods = HttpMethod.GET,
    uri = "/_tenant/{tenant}",
    privileged = true,
    outputEntity = "tenant"
)
public class ListTenantCmd extends UNodeOutCallback {

    @Override
    public UNode invokeUNodeOut() {
        String tenantName = m_request.getVariableDecoded("tenant");
        TenantDefinition tenantDef = TenantService.instance().getTenantDefinition(tenantName);
        if (tenantDef == null) {
            throw new NotFoundException("Tenant not found: " + tenantName);
        }
        return tenantDef.toDoc();
    }

}   // class ListTenantCmd
