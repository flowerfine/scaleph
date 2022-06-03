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

package cn.sliew.scaleph.plugin.framework.property;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;

@Getter
public class PropertyDependency<T> {
    private final String propertyName;
    private final Set<T> dependentValues;

    public PropertyDependency(final String propertyName) {
        this.propertyName = Objects.requireNonNull(propertyName);
        this.dependentValues = null;
    }

    public PropertyDependency(final String propertyName, final Set<T> dependentValues) {
        this.propertyName = Objects.requireNonNull(propertyName);
        this.dependentValues =
            Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(dependentValues)));
    }
}