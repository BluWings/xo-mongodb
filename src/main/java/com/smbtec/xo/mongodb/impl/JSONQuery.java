/*
 * eXtended Objects - MongoDB Binding
 *
 * Copyright (C) 2014 SMB GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.smbtec.xo.mongodb.impl;

import java.util.HashMap;
import java.util.Map;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.smbtec.xo.mongodb.api.annotation.Query;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class JSONQuery implements DatastoreQuery<Query> {

    private DB database;

    public JSONQuery(DB database) {
        this.database = database;
    }

    public ResultIterator<Map<String, Object>> execute(String query, Map<String, Object> parameters) {
        return execute0(query, null, parameters);
    }

    public ResultIterator<Map<String, Object>> execute(Query query, Map<String, Object> parameters) {
        return execute0(query.value(), query.type(), parameters);
    }

    private ResultIterator<Map<String, Object>> execute0(String json, Class<?> type, Map<String, Object> parameters) {
        // TODO
        DBCollection collection = database.getCollection("A");

        final DBCursor matches = collection.find((DBObject) JSON.parse(json));

        return new ResultIterator<Map<String, Object>>() {

            public boolean hasNext() {
                return matches.hasNext();
            }

            public Map<String, Object> next() {
                DBObject next = matches.next();
                final Map<String, Object> result = new HashMap<>();
                // TODO
                result.put("result", new MongoDbDocument(next, "A"));
                return result;
            }

            public void remove() {
                throw new XOException("Remove operation is not supported for query results.");
            }

            public void close() {
                matches.close();
            }
        };

    }
}
