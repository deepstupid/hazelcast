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

package com.hazelcast.internal.ascii.rest;

import java.nio.ByteBuffer;

import static com.hazelcast.internal.ascii.TextCommandConstants.TextCommandType.HTTP_DELETE;

public class HttpDeleteCommand extends HttpCommand {
    private boolean nextLine;

    public HttpDeleteCommand(String uri) {
        super(HTTP_DELETE, uri);
    }

    @Override
    public boolean readFrom(ByteBuffer src) {
        while (src.hasRemaining()) {
            char c = (char) src.get();
            if (c == '\n') {
                if (nextLine) {
                    return true;
                }
                nextLine = true;
            } else if (c != '\r') {
                nextLine = false;
            }
        }
        return false;
    }
}
