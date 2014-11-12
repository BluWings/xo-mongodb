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

import static com.smbtec.xo.mongodb.api.MongoDbConstants.MONGODB_ID;

import java.util.Iterator;
import java.util.Objects;

import com.buschmais.xo.api.ResultIterable;
import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.buschmais.xo.spi.session.XOSession;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.smbtec.xo.mongodb.api.MongoDbRepository;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbRepositoryImpl implements MongoDbRepository {

    private DB database;
    private XOSession xoSession;

    public MongoDbRepositoryImpl(DB database, XOSession xoSession) {
        this.database = database;
        this.xoSession = xoSession;
    }

    @Override
    public <T> T findOne(Class<T> type) {
        EntityTypeMetadata<DocumentMetadata> entityMetadata = checkEntityMetadata(type);
        String collectionName = entityMetadata.getDatastoreMetadata().getDiscriminator();
        DBObject result = database.getCollection(collectionName).findOne();
        if (result == null) {
            return null;
        } else {
            MongoDbDocument document = new MongoDbDocument(result, collectionName);
            return (T) xoSession.fromDatastore(document);
        }
    }

    @Override
    public <T> T findOne(Class<T> type, Object id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id must not be null");
        }
        EntityTypeMetadata<DocumentMetadata> entityMetadata = checkEntityMetadata(type);
        String collectionName = entityMetadata.getDatastoreMetadata().getDiscriminator();
        DBObject result = database.getCollection(collectionName).findOne(
                BasicDBObjectBuilder.start(MONGODB_ID, id).get());
        if (result == null) {
            return null;
        } else {
            MongoDbDocument document = new MongoDbDocument(result, collectionName);
            return (T) xoSession.fromDatastore(document);
        }
    }

    @Override
    public <T> ResultIterable<T> findAll(Class<T> type) {
        EntityTypeMetadata<DocumentMetadata> entityMetadata = checkEntityMetadata(type);
        String collectionName = entityMetadata.getDatastoreMetadata().getDiscriminator();
        DBCursor cursor = database.getCollection(collectionName).find();
        if (cursor == null) {
            return null;
        } else {
            Iterator<DBObject> iterator = cursor.iterator();
            return xoSession.toResult(new ResultIterator<MongoDbDocument>() {

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public MongoDbDocument next() {
                    return new MongoDbDocument(iterator.next(), collectionName);
                }

                @Override
                public void close() {
                }
            });
        }
    }

    @Override
    public <T> ResultIterable<T> findAll(Class<T> type, Iterable<Object> ids) {
        return null;
    }

    private EntityTypeMetadata<DocumentMetadata> checkEntityMetadata(Class type) {
        EntityTypeMetadata<DocumentMetadata> entityMetadata = xoSession.getEntityMetadata(type);
        if (entityMetadata == null) {
            throw new XOException("No EntityMetadata found for " + type.getName());
        }
        return entityMetadata;
    }

}
