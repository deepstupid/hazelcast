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

package com.hazelcast.client.impl.protocol.task.map;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.codec.MapAssignAndGetUuidsCodec;
import com.hazelcast.instance.Node;
import com.hazelcast.nio.Connection;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.OperationFactory;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hazelcast.map.impl.MapService.SERVICE_NAME;

public class MapAssignAndGetUuidsMessageTask
        extends AbstractMapAllPartitionsMessageTask<MapAssignAndGetUuidsCodec.RequestParameters> {

    public MapAssignAndGetUuidsMessageTask(ClientMessage clientMessage, Node node, Connection connection) {
        super(clientMessage, node, connection);
    }

    @Override
    protected MapAssignAndGetUuidsCodec.RequestParameters decodeClientMessage(ClientMessage clientMessage) {
        return MapAssignAndGetUuidsCodec.decodeRequest(clientMessage);
    }

    @Override
    protected ClientMessage encodeResponse(Object response) {
        return MapAssignAndGetUuidsCodec.encodeResponse(((List<Data>) response));
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public String getDistributedObjectName() {
        return null;
    }

    @Override
    public String getMethodName() {
        return null;
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }

    @Override
    protected OperationFactory createOperationFactory() {
        return new MapAssignAndGetUuidsOperationFactory();
    }

    @Override
    protected Object reduce(Map<Integer, Object> map) {
        List<Data> objects = new ArrayList<Data>(2 * map.size());
        for (Map.Entry<Integer, Object> entry : map.entrySet()) {
            objects.add(serializationService.toData(entry.getKey()));
            objects.add(serializationService.toData(entry.getValue()));
        }
        return objects;
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
