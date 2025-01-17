/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ranger.plugin.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ranger.plugin.contextenricher.RangerContextEnricher;
import org.apache.ranger.plugin.policyengine.RangerAccessRequest;
import org.apache.ranger.plugin.policyengine.RangerPolicyEngine;
import org.apache.ranger.plugin.policyengine.RangerResourceACLs;
import org.apache.ranger.plugin.util.RangerRoles;
import org.apache.ranger.plugin.util.RangerRolesUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RangerAuthContext {
    private final Map<RangerContextEnricher, Object> requestContextEnrichers;
    private       RangerRolesUtil                    rangerRolesUtil;

    public RangerAuthContext(Map<RangerContextEnricher, Object> requestContextEnrichers) {
        this.requestContextEnrichers = requestContextEnrichers != null ? requestContextEnrichers : new ConcurrentHashMap<>();
    }

    public RangerAuthContext(Map<RangerContextEnricher, Object> requestContextEnrichers, RangerRoles roles) {
        this.requestContextEnrichers = requestContextEnrichers != null ? requestContextEnrichers : new ConcurrentHashMap<>();

        setRangerRoles(roles);
    }

    public Map<RangerContextEnricher, Object> getRequestContextEnrichers() {
        return requestContextEnrichers;
    }

    public void addOrReplaceRequestContextEnricher(RangerContextEnricher enricher, Object database) {
        // concurrentHashMap does not allow null to be inserted into it, so insert a dummy which is checked
        // when enrich() is called
        requestContextEnrichers.put(enricher, database != null ? database : enricher);
    }

    public void cleanupRequestContextEnricher(RangerContextEnricher enricher) {
        requestContextEnrichers.remove(enricher);
    }

    public void setRangerRoles(RangerRoles rangerRoles) {
        this.rangerRolesUtil = rangerRoles != null ? new RangerRolesUtil(rangerRoles) : new RangerRolesUtil(null);
    }

    public Set<String> getRolesForUserAndGroups(String user, Set<String> groups) {
        RangerRolesUtil          rangerRolesUtil  = this.rangerRolesUtil;
        Map<String, Set<String>> userRoleMapping  = rangerRolesUtil.getUserRoleMapping();
        Map<String, Set<String>> groupRoleMapping = rangerRolesUtil.getGroupRoleMapping();
        Set<String>              allRoles         = new HashSet<>();

        if (MapUtils.isNotEmpty(userRoleMapping) && StringUtils.isNotEmpty(user)) {
            Set<String> userRoles = userRoleMapping.get(user);

            if (CollectionUtils.isNotEmpty(userRoles)) {
                allRoles.addAll(userRoles);
            }
        }

        if (MapUtils.isNotEmpty(groupRoleMapping)) {
            if (CollectionUtils.isNotEmpty(groups)) {
                for (String group : groups) {
                    Set<String> groupRoles = groupRoleMapping.get(group);

                    if (CollectionUtils.isNotEmpty(groupRoles)) {
                        allRoles.addAll(groupRoles);
                    }
                }
            }

            Set<String> publicGroupRoles = groupRoleMapping.get(RangerPolicyEngine.GROUP_PUBLIC);

            if (CollectionUtils.isNotEmpty(publicGroupRoles)) {
                allRoles.addAll(publicGroupRoles);
            }
        }

        return allRoles;
    }

    public long getRoleVersion() { return this.rangerRolesUtil.getRoleVersion(); }

    // For backward compatibility
    public RangerResourceACLs getResourceACLs(RangerAccessRequest request) {
        // Invoke getResourceACLs on the first service in this plugin
        Collection<RangerBasePlugin> plugins = RangerBasePlugin.getServicePluginMap().values();

        if (plugins.size() > 0) {
            RangerBasePlugin[] array = plugins.toArray(new RangerBasePlugin[0]);

            return array[0].getResourceACLs(request);
        } else {
            return null;
        }
    }
}
