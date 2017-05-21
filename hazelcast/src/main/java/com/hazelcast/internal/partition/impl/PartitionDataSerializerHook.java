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

package com.hazelcast.internal.partition.impl;

import com.hazelcast.internal.partition.NonFragmentedServiceNamespace;
import com.hazelcast.internal.partition.PartitionRuntimeState;
import com.hazelcast.internal.partition.ReplicaFragmentMigrationState;
import com.hazelcast.internal.partition.operation.*;
import com.hazelcast.internal.serialization.DataSerializerHook;
import com.hazelcast.internal.serialization.impl.ArrayDataSerializableFactory;
import com.hazelcast.internal.serialization.impl.FactoryIdHelper;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.util.ConstructorFunction;

import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.PARTITION_DS_FACTORY;
import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.PARTITION_DS_FACTORY_ID;

public final class PartitionDataSerializerHook implements DataSerializerHook {

    public static final int F_ID = FactoryIdHelper.getFactoryId(PARTITION_DS_FACTORY, PARTITION_DS_FACTORY_ID);

    public static final int PARTITION_RUNTIME_STATE = 1;
    public static final int ASSIGN_PARTITIONS = 2;
    public static final int CHECK_REPLICA_VERSION = 3;
    public static final int FETCH_PARTITION_STATE = 4;
    public static final int HAS_ONGOING_MIGRATION = 5;
    public static final int MIGRATION_COMMIT = 6;
    public static final int LEGACY_MIGRATION = 7;
    public static final int LEGACY_MIGRATION_REQUEST = 8;
    public static final int PARTITION_STATE_OP = 9;
    public static final int PROMOTION_COMMIT = 10;
    public static final int REPLICA_SYNC_REQUEST = 11;
    public static final int REPLICA_SYNC_RESPONSE = 12;
    public static final int REPLICA_SYNC_RETRY_RESPONSE = 13;
    public static final int SAFE_STATE_CHECK = 14;
    public static final int SHUTDOWN_REQUEST = 15;
    public static final int SHUTDOWN_RESPONSE = 16;
    public static final int REPLICA_FRAGMENT_MIGRATION_STATE = 17;
    public static final int MIGRATION = 18;
    public static final int MIGRATION_REQUEST = 19;
    public static final int NONFRAGMENTED_SERVICE_NAMESPACE = 20;

    private static final int LEN = NONFRAGMENTED_SERVICE_NAMESPACE + 1;

    @Override
    public int getFactoryId() {
        return F_ID;
    }

    @Override
    public DataSerializableFactory createFactory() {
        ConstructorFunction<Integer, IdentifiedDataSerializable>[] constructors = new ConstructorFunction[LEN];

        constructors[PARTITION_RUNTIME_STATE] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new PartitionRuntimeState();
            }
        };
        constructors[ASSIGN_PARTITIONS] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new AssignPartitions();
            }
        };
        constructors[CHECK_REPLICA_VERSION] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new CheckReplicaVersion();
            }
        };
        constructors[FETCH_PARTITION_STATE] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new FetchPartitionStateOperation();
            }
        };
        constructors[HAS_ONGOING_MIGRATION] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new HasOngoingMigration();
            }
        };
        constructors[MIGRATION_COMMIT] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new MigrationCommitOperation();
            }
        };
        constructors[LEGACY_MIGRATION] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new LegacyMigrationOperation();
            }
        };
        constructors[LEGACY_MIGRATION_REQUEST] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new LegacyMigrationRequestOperation();
            }
        };
        constructors[PARTITION_STATE_OP] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new PartitionStateOperation();
            }
        };
        constructors[PROMOTION_COMMIT] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new PromotionCommitOperation();
            }
        };
        constructors[REPLICA_SYNC_REQUEST] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new ReplicaSyncRequest();
            }
        };
        constructors[REPLICA_SYNC_RESPONSE] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new ReplicaSyncResponse();
            }
        };
        constructors[REPLICA_SYNC_RETRY_RESPONSE] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new ReplicaSyncRetryResponse();
            }
        };
        constructors[SAFE_STATE_CHECK] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new SafeStateCheckOperation();
            }
        };
        constructors[SHUTDOWN_REQUEST] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new ShutdownRequestOperation();
            }
        };
        constructors[SHUTDOWN_RESPONSE] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new ShutdownResponseOperation();
            }
        };
        constructors[REPLICA_FRAGMENT_MIGRATION_STATE] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            @Override
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new ReplicaFragmentMigrationState();
            }
        };
        constructors[MIGRATION] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            @Override
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new MigrationOperation();
            }
        };
        constructors[MIGRATION_REQUEST] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            @Override
            public IdentifiedDataSerializable createNew(Integer arg) {
                return new MigrationRequestOperation();
            }
        };
        constructors[NONFRAGMENTED_SERVICE_NAMESPACE] = new ConstructorFunction<Integer, IdentifiedDataSerializable>() {
            public IdentifiedDataSerializable createNew(Integer arg) {
                return NonFragmentedServiceNamespace.INSTANCE;
            }
        };
        return new ArrayDataSerializableFactory(constructors);
    }
}
