/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.resource.service.impl;

import cn.sliew.scaleph.resource.service.ResourceDescriptor;
import cn.sliew.scaleph.resource.service.ResourceService;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private List<ResourceDescriptor> resourceDescriptors;

    @Override
    public List<ResourceDescriptor> getSupportedResources() {
        return Collections.unmodifiableList(resourceDescriptors);
    }

    @Override
    public ResourceDescriptor getResourceDescriptor(ResourceType type) {
        Optional<ResourceDescriptor> optional = resourceDescriptors.stream()
                .filter(resourceDescriptor -> resourceDescriptor.getResourceType() == type)
                .findAny();
        return optional.orElseThrow(() -> new IllegalStateException("unknown resource type for " + type.getValue()));
    }
}
