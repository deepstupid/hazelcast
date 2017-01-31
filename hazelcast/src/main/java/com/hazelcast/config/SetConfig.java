/*
 * Copyright (c) 2008-2017, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.config;

/**
 * Contains the configuration for an {@link com.hazelcast.core.ISet}.
 */
public class SetConfig extends CollectionConfig<SetConfig> {

    private SetConfigReadOnly readOnly;

    public SetConfig() {
    }

    public SetConfig(String name) {
        setName(name);
    }

    public SetConfig(SetConfig config) {
        super(config);
    }

    /**
     * Gets immutable version of this configuration.
     *
     * @return Immutable version of this configuration.
     * @deprecated this method will be removed in 3.9; it is meant for internal usage only.
     */
    @Override
    public SetConfigReadOnly getAsReadOnly() {
        if (readOnly == null) {
            readOnly = new SetConfigReadOnly(this);
        }
        return readOnly;
    }
}
