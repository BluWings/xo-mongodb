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

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.xo.spi.datastore.Datastore;
import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.method.MethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
import com.mongodb.DB;
import com.smbtec.xo.mongodb.api.MongoDbDatastoreSession;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.mongodb.impl.metadata.RelationshipMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbDatastore implements Datastore<MongoDbDatastoreSession, DocumentMetadata, String, RelationshipMetadata, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbDatastore.class);

    private DB database;

    private final MongoDbMetadataFactory metadataFactory = new MongoDbMetadataFactory();

    public MongoDbDatastore(DB database) {
        this.database = database;
    }

    @Override
    public void init(Map<Class<?>, TypeMetadata> registeredMetadata) {
        for (TypeMetadata typeMetadata : registeredMetadata.values()) {
            if (typeMetadata instanceof EntityTypeMetadata) {
                EntityTypeMetadata<DocumentMetadata> entityTypeMetadata = (EntityTypeMetadata<DocumentMetadata>) typeMetadata;
                String collectionName = entityTypeMetadata.getDatastoreMetadata().getDiscriminator();
                Collection<IndexDefinition> indexDefinitions = entityTypeMetadata.getDatastoreMetadata().getIndexDefinitions();
                for (IndexDefinition indexDefinition : indexDefinitions) {
                    ensureIndex(collectionName, indexDefinition);
                }
                for (MethodMetadata<?, ?> propertyMethod : entityTypeMetadata.getProperties()) {
                    PropertyMetadata propertyMetadata = (PropertyMetadata) propertyMethod.getDatastoreMetadata();
                    if (propertyMetadata != null) {
                        IndexDefinition indexDefinition = propertyMetadata.getIndexDefinition();
                        if (indexDefinition != null) {
                            ensureIndex(collectionName, indexDefinition);
                        }
                    }
                }
            }
        }
    }

    @Override
    public DatastoreMetadataFactory<DocumentMetadata, String, RelationshipMetadata, String> getMetadataFactory() {
        return metadataFactory;
    }

    @Override
    public MongoDbDatastoreSession createSession() {
        return new MongoDbDatastoreSessionImpl(database);
    }

    @Override
    public void close() {
        // intentionally left blank
    }

    private void ensureIndex(String collectionName, IndexDefinition indexDefinition) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating {} on entity type {}", indexDefinition, collectionName);
        }
        database.getCollection(collectionName).createIndex(indexDefinition.getIndexKeys(), indexDefinition.getIndexOptions());
    }

}
