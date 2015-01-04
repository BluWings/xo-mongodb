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

import java.lang.annotation.Annotation;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.buschmais.xo.spi.session.XOSession;
import com.mongodb.DB;
import com.smbtec.xo.mongodb.api.MongoDbDatastoreSession;
import com.smbtec.xo.mongodb.api.annotation.Query;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.mongodb.impl.metadata.RelationshipMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbDatastoreSessionImpl implements MongoDbDatastoreSession {

    private final DB database;

    private MongoDbDocumentManager documentManager;
    private MongoDbRelationshipManager relationManager;

    public MongoDbDatastoreSessionImpl(DB database) {
        this.database = database;
        documentManager = new MongoDbDocumentManager(database);
        relationManager = new MongoDbRelationshipManager(database);
    }

    @Override
    public DB getDatabase() {
        return database;
    }

    @Override
    public DatastoreTransaction getDatastoreTransaction() {
        return null;
    }

    @Override
    public DatastoreEntityManager<Object, MongoDbDocument, DocumentMetadata, String, PropertyMetadata> getDatastoreEntityManager() {
        return documentManager;
    }

    @Override
    public DatastoreRelationManager<MongoDbDocument, Object, MongoDbRelation, RelationshipMetadata, String, PropertyMetadata> getDatastoreRelationManager() {
        return relationManager;
    }

    @Override
    public Class<? extends Annotation> getDefaultQueryLanguage() {
        return Query.class;
    }

    @Override
    public <QL extends Annotation> DatastoreQuery<QL> createQuery(Class<QL> queryLanguage) {
        if (Query.class.equals(queryLanguage)) {
            return (DatastoreQuery<QL>) new JSONQuery(database);
        }
        throw new XOException("Unsupported query language: " + queryLanguage.getName());
    }

    @Override
    public void close() {
        // intentionally left blank
    }

    @Override
    public <R> R createRepository(XOSession xoSession, Class<R> type) {
        return (R) new MongoDbRepositoryImpl(database, xoSession);
    }

}
