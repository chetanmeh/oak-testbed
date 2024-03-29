/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.jackrabbit.mongomk;

import org.apache.jackrabbit.mongomk.impl.MongoConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;

/**
 * A utility class to get a {@link MongoConnection} to a local mongo instance
 * and clean a test database.
 */
public class MongoUtils {

    protected static final String HOST =
            System.getProperty("mongo.host", "127.0.0.1");

    protected static final int PORT =
            Integer.getInteger("mongo.port", 27017);

    protected static final String DB =
            System.getProperty("mongo.db", "MongoMKDB");

    protected static MongoConnection mongoConnection;

    protected static Exception exception;

    /**
     * Get a connection if available. If not available, null is returned. If
     * called again, the same connection is returned, or null is returned if
     * there was an error.
     * 
     * @return the connection or null
     */
    public static MongoConnection getConnection() {
        if (exception != null) {
            return null;
        }
        if (mongoConnection == null) {
            try {
                mongoConnection = new MongoConnection(HOST, PORT, DB);
                mongoConnection.getDB().command(new BasicDBObject("ping", 1));
                // dropCollections(mongoConnection.getDB());
            } catch (Exception e) {
                exception = e;
            }
        }
        return mongoConnection;
    }
    
    /**
     * Drop all user defined collections. System collections are not dropped.
     * 
     * @param db the connection
     */
    public static void dropCollections(DB db) {
        for (String name : db.getCollectionNames()) {
            if (!name.startsWith("system.")) {
                db.getCollection(name).drop();
            }
        }
    }

}
