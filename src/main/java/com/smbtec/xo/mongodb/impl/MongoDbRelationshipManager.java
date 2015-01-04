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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata.Direction;
import com.mongodb.DB;
import com.mongodb.DBRef;
import com.smbtec.xo.mongodb.api.MongoDbConstants;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.mongodb.impl.metadata.RelationshipMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbRelationshipManager extends AbstractMongoDbPropertyManager<MongoDbRelation> implements
        DatastoreRelationManager<MongoDbDocument, Object, MongoDbRelation, RelationshipMetadata, String, PropertyMetadata> {

    private DB database;

    public MongoDbRelationshipManager(DB database) {
        this.database = database;
    }

    @Override
    public boolean isRelation(Object o) {
        return MongoDbRelation.class.isAssignableFrom(o.getClass());
    }

    @Override
    public String getRelationDiscriminator(MongoDbRelation relation) {
        return (String) relation.getLabel();
    }

    @Override
    public MongoDbRelation createRelation(MongoDbDocument source, RelationTypeMetadata<RelationshipMetadata> metadata, Direction direction,
            MongoDbDocument target, Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        final String label = metadata.getDatastoreMetadata().getDiscriminator();
        DBRef dbref = new DBRef(database, target.getLabel(), target.getDelegate().get(MongoDbConstants.MONGODB_ID));

        if (metadata.getDatastoreMetadata().isCollectionType()) {
            String collectionName = metadata.getDatastoreMetadata().getDiscriminator();
            Object old = source.getDelegate().get(collectionName);
            if (old != null) {
                @SuppressWarnings("unchecked")
                List<DBRef> oldDBRefList = (List<DBRef>) old;
                List<DBRef> dbRefList = new ArrayList<>(oldDBRefList);
                dbRefList.add(dbref);
                source.getDelegate().put(collectionName, dbRefList);
            } else {
                source.getDelegate().put(collectionName, Arrays.asList(dbref));
            }
        } else {
            source.getDelegate().put(label, dbref);
        }
        return new MongoDbReference(label, source, target);
    }

    @Override
    public void deleteRelation(MongoDbRelation relation) {
        // TODO
    }

    @Override
    public Object getRelationId(MongoDbRelation relation) {
        // TODO
        return null;
    }

    @Override
    public void flushRelation(MongoDbRelation relation) {
        // TODO
    }

    @Override
    public boolean hasSingleRelation(MongoDbDocument source, RelationTypeMetadata<RelationshipMetadata> metadata, Direction direction) {
        return source.getDelegate().get(metadata.getDatastoreMetadata().getDiscriminator()) != null;
    }

    @Override
    public MongoDbRelation getSingleRelation(MongoDbDocument source, RelationTypeMetadata<RelationshipMetadata> metadata,
            Direction direction) {
        String label = metadata.getDatastoreMetadata().getDiscriminator();
        DBRef dbref = (DBRef) source.getDelegate().get(label);
        return new MongoDbReference(label, source, dbref);
    }

    @Override
    public Iterable<MongoDbRelation> getRelations(MongoDbDocument source, RelationTypeMetadata<RelationshipMetadata> metadata,
            Direction direction) {
        String label = metadata.getDatastoreMetadata().getDiscriminator();
        Object object = source.getDelegate().get(label);
        if (object == null) {
            return Collections.<MongoDbRelation> emptyList();
        } else {
            List<DBRef> dbRefList = (List<DBRef>) object;
            Iterator<DBRef> iterator = dbRefList.iterator();
            return new Iterable<MongoDbRelation>() {
                @Override
                public Iterator<MongoDbRelation> iterator() {
                    return new Iterator<MongoDbRelation>() {

                        @Override
                        public boolean hasNext() {
                            return iterator.hasNext();
                        }

                        @Override
                        public MongoDbRelation next() {
                            return new MongoDbReference(label, source, iterator.next());
                        }
                    };
                }
            };
        }
    }

    @Override
    public MongoDbDocument getFrom(MongoDbRelation relation) {
        return relation.getSource();
    }

    @Override
    public MongoDbDocument getTo(MongoDbRelation relation) {
        return relation.getTarget();
    }

    @Override
    public MongoDbRelation findRelationById(RelationTypeMetadata<RelationshipMetadata> metadata, Object id) {
        return null;
    }

}
