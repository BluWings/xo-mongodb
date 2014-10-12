package com.smbtec.xo.mongodb.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbDocumentManager extends AbstractMongoDbPropertyManager<DBObject> implements
        DatastoreEntityManager<Object, DBObject, DocumentMetadata, String, PropertyMetadata> {

    private final DBCollection documents;

    public MongoDbDocumentManager(DBCollection documentCollection) {
        this.documents = documentCollection;
    }

    @Override
    DBCollection getDBCollection() {
        return documents;
    }

    public boolean isEntity(Object o) {
        return DBObject.class.isAssignableFrom(o.getClass());
    }

    public Set<String> getEntityDiscriminators(DBObject entity) {
        final Set<String> discriminators = new HashSet<>();
        for (final String key : entity.keySet()) {
            if (key.startsWith(XO_DISCRIMINATORS_PROPERTY)) {
                discriminators.add((String) entity.get(key));
            }
        }
        if (discriminators.isEmpty()) {
            throw new XOException(
                    "A vertex was found without discriminators. Does another framework alter the database?");
        }
        return discriminators;

    }

    public Object getEntityId(DBObject entity) {
        return entity.get(MONGODB_ID);
    }

    public BasicDBObject createEntity(TypeMetadataSet<EntityTypeMetadata<DocumentMetadata>> types,
            Set<String> discriminators, Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        BasicDBObject document = new BasicDBObject();
        setProperties(document, getDiscriminatorProperties(discriminators));
        documents.insert(document);
        return document;
    }

    public void deleteEntity(DBObject entity) {
        documents.remove(entity);
    }

    public ResultIterator<DBObject> findEntity(EntityTypeMetadata<DocumentMetadata> entityTypeMetadata,
            String discriminator, Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> values) {
        if (values.size() > 1) {
            throw new XOException("Only one property value is supported for find operation");
        }

        Map.Entry<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> entry = values.entrySet().iterator()
                .next();
        PrimitivePropertyMethodMetadata<PropertyMetadata> propertyMethodMetadata = entry.getKey();
        PropertyMetadata propertyMetadata = propertyMethodMetadata.getDatastoreMetadata();
        Object value = entry.getValue();

        final DBCursor matches = getDBCollection().find(new BasicDBObject(propertyMetadata.getName(), value));

        return new ResultIterator<DBObject>() {

            public boolean hasNext() {
                return matches.hasNext();
            }

            public DBObject next() {
                return (DBObject) matches.next();
            }

            public void close() {
                matches.close();
            }

            @Override
            public void remove() {
            }
        };
    }

    public void migrateEntity(DBObject entity, TypeMetadataSet<EntityTypeMetadata<DocumentMetadata>> types,
            Set<String> discriminators, TypeMetadataSet<EntityTypeMetadata<DocumentMetadata>> targetTypes,
            Set<String> targetDiscriminators) {
        removeProperties(entity, getDiscriminatorProperties(discriminators).keySet());
        setProperties(entity, getDiscriminatorProperties(targetDiscriminators));
    }

    public void flushEntity(DBObject entity) {
        getDBCollection().save(entity);
    }

    private Map<String, Object> getDiscriminatorProperties(Set<String> discriminators) {
        Map<String, Object> properties = new HashMap<String, Object>();
        for (final String discriminator : discriminators) {
            properties.put(XO_DISCRIMINATORS_PROPERTY + discriminator, discriminator);
        }
        return properties;
    }

}
