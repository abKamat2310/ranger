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

package org.apache.ranger.plugin.policyengine;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.ranger.plugin.model.RangerPolicy;
import org.apache.ranger.plugin.model.RangerServiceDef;
import org.apache.ranger.plugin.util.GrantRevokeRequest;
import org.apache.ranger.plugin.util.RangerAccessRequestUtil;
import org.apache.ranger.plugin.util.RangerRoles;

public interface RangerPolicyEngine {
	String GROUP_PUBLIC   = "public";
	String ANY_ACCESS     = "_any";
	String ADMIN_ACCESS   = "_admin";

	String AUDIT_ALL      = "audit-all";
	String AUDIT_NONE     = "audit-none";
	String AUDIT_DEFAULT  = "audit-default";

	String USER_CURRENT   = "{" + RangerAccessRequestUtil.KEY_USER + "}";
	String RESOURCE_OWNER = "{OWNER}";

	void setUseForwardedIPAddress(boolean useForwardedIPAddress);

	void setTrustedProxyAddresses(String[] trustedProxyAddresses);

	RangerServiceDef getServiceDef();

	long getPolicyVersion();

	long getRoleVersion();

	void setRangerRoles(RangerRoles rangerRoles);

	RangerAccessResult evaluatePolicies(RangerAccessRequest request, int policyType, RangerAccessResultProcessor resultProcessor);

	Collection<RangerAccessResult> evaluatePolicies(Collection<RangerAccessRequest> requests, int policyType, RangerAccessResultProcessor resultProcessor);

	RangerResourceACLs getResourceACLs(RangerAccessRequest request);

	Set<String> getRolesFromUserAndGroups(String user, Set<String> groups);

	// Helpers

	List<RangerPolicy> getResourcePolicies(String zoneName);

	List<RangerPolicy> getResourcePolicies();

	List<RangerPolicy> getTagPolicies();

	String getMatchedZoneName(GrantRevokeRequest grantRevokeRequest);

	// This API is used only used by test code
	RangerResourceAccessInfo getResourceAccessInfo(RangerAccessRequest request);
}
