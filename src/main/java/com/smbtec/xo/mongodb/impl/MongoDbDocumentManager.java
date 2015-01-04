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

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.google.common.collect.Iterables;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.smbtec.xo.mongodb.api.MongoDbConstants;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbDocumentManager extends AbstractMongoDbPropertyManager<MongoDbDocument> implements
        DatastoreEntityManager<Object, MongoDbDocument, DocumentMetadata, String, PropertyMetadata> {

    private final DB database;

    public MongoDbDocumentManager(DB database) {
        this.database = database;
    }

    @Override
    public boolean isEntity(Object o) {
        return MongoDbDocument.class.isAssignableFrom(o.getClass());
    }

    @Override
    public Set<String> getEntityDiscriminators(MongoDbDocument entity) {
        final Set<String> discriminators = new HashSet<>();
        discriminators.add(entity.getLabel());
        return discriminators;
    }

    @Override
    public Object getEntityId(MongoDbDocument entity) {
        return entity.getDelegate().get(MongoDbConstants.MONGODB_ID);
    }

    @Override
    public MongoDbDocument findEntityById(EntityTypeMetadata<DocumentMetadata> metadata, String discriminator, Object id) {
        DBObject delegate = database.getCollection(discriminator).find(new BasicDBObject(MongoDbConstants.MONGODB_ID, id)).one();
        if (delegate != null) {
            return new MongoDbDocument(delegate, discriminator);
        } else {
            return null;
        }
    }

    @Override
    public MongoDbDocument createEntity(TypeMetadataSet<EntityTypeMetadata<DocumentMetadata>> types, Set<String> discriminators,
            Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        if (types.size() > 1) {
            throw new XOException("multiple inheritance not allowed");
        }
        EntityTypeMetadata<DocumentMetadata> typeMetadata = Iterables.getOnlyElement(types);
        BasicDBObject document = new BasicDBObject();
        for (Entry<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> properties : exampleEntity.entrySet()) {
            String key = properties.getKey().getDatastoreMetadata().getName();
            Object v = properties.getValue();
            document.put(key, v);
        }

        String label = typeMetadata.getDatastoreMetadata().getDiscriminator();
        DBCollection collection = database.getCollection(label);
        collection.insert(document);
        return new MongoDbDocument(document, label);
    }

    @Override
    public void deleteEntity(MongoDbDocument entity) {
        database.getCollection(entity.getLabel()).remove(entity.getDelegate());
    }

    @Override
    public ResultIterator<MongoDbDocument> findEntity(EntityTypeMetadata<DocumentMetadata> entityTypeMetadata, String discriminator,
            Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> values) {
        if (values.size() > 1) {
            throw new XOException("Only one property value is supported for find operation");
        }

        Map.Entry<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> entry = values.entrySet().iterator().next();
        PrimitivePropertyMethodMetadata<PropertyMetadata> propertyMethodMetadata = entry.getKey();
        PropertyMetadata propertyMetadata = propertyMethodMetadata.getDatastoreMetadata();
        Object value = entry.getValue();
        String collectionName = entityTypeMetadata.getDatastoreMetadata().getDiscriminator();

        final DBCursor matches = database.getCollection(collectionName).find(new BasicDBObject(propertyMetadata.getName(), value));

        return new ResultIterator<MongoDbDocument>() {

            public boolean hasNext() {
                return matches.hasNext();
            }

            public MongoDbDocument next() {
                return new MongoDbDocument(matches.next(), discriminator);
            }

            public void close() {
                matches.close();
            }

            @Override
            public void remove() {
                // intentionally left blank
            }
        };
    }

    @Override
    public void migrateEntity(MongoDbDocument entity, TypeMetadataSet<EntityTypeMetadata<DocumentMetadata>> types,
            Set<String> discriminators, TypeMetadataSet<EntityTypeMetadata<DocumentMetadata>> targetTypes, Set<String> targetDiscriminators) {
        // TODO
    }

    @Override
    public void flushEntity(MongoDbDocument entity) {
        database.getCollection(entity.getLabel()).save(entity.getDelegate());
    }

}
