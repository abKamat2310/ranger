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

package org.apache.ranger.biz;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ranger.plugin.model.RangerPolicy;
import org.apache.ranger.plugin.model.RangerPolicy.RangerPolicyResource;
import org.apache.ranger.plugin.policyengine.RangerAccessResource;
import org.apache.ranger.plugin.util.RangerRoles;

public interface RangerPolicyAdmin {

    boolean isAccessAllowed(RangerAccessResource resource, String user, Set<String> userGroups, String accessType);

    boolean isAccessAllowed(RangerPolicy policy, String user, Set<String> userGroups, Set<String> roles, String accessType);

    List<RangerPolicy> getExactMatchPolicies(RangerAccessResource resource, Map<String, Object> evalContext);

    List<RangerPolicy> getExactMatchPolicies(RangerPolicy policy, Map<String, Object> evalContext);

    List<RangerPolicy> getMatchingPolicies(RangerAccessResource resource);

    long getPolicyVersion();

    long getRoleVersion();

    void setRangerRoles(RangerRoles rangerRoles);

    String getServiceName();

    Set<String> getRolesFromUserAndGroups(String user, Set<String> groups);

    // This API is used only by test-code
    boolean isAccessAllowedByUnzonedPolicies(Map<String, RangerPolicyResource> resources, String user, Set<String> userGroups, String accessType);

    // This API is used only by test-code
    List<RangerPolicy> getAllowedUnzonedPolicies(String user, Set<String> userGroups, String accessType);

}
