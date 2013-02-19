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

package org.apache.jackrabbit.mongomk.prototype

import org.apache.jackrabbit.mongomk.MongoUtils

import static org.junit.Assert.assertEquals


class SimpleMongoMKTest extends GroovyTestCase{
    static DB_NAME = 'MongoMKDB'

    void testHelloMK(){
        MongoMK mk = new MongoMK(MongoUtils.getConnection().getDB(),0);

        String rev = mk.commit("/", "+\"test\":{\"name\": \"Hello\"}", null, null);
        String test = mk.getNodes("/test", rev, 0, 0, Integer.MAX_VALUE, null);
        assertEquals("{\"name\":\"Hello\"}", test);

        rev = mk.commit("/test", "+\"a\":{\"name\": \"World\"}", null, null);
        rev = mk.commit("/test", '''+"b":{"name":"!"}''', null, null);
        test = mk.getNodes("/test", rev, 0, 0, Integer.MAX_VALUE, null);
        Node.Children c;
        c = mk.readChildren("/",
                Revision.fromString(rev), Integer.MAX_VALUE);
        assertEquals("/: [/test]", c.toString());
        c = mk.readChildren("/test",
                Revision.fromString(rev), Integer.MAX_VALUE);
        assertEquals("/test: [/test/a, /test/b]", c.toString());

        // System.out.println(test);
        mk.dispose();
    }

}
