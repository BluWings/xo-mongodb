package com.smbtec.xo.mongodb.impl;

import java.util.Map;
import java.util.Set;

import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public abstract class AbstractMongoDbPropertyManager<E extends DBObject> implements
        DatastorePropertyManager<E, PropertyMetadata> {

    /**
     * This constant contains the prefix for discriminator properties.
     */
    public static final String XO_DISCRIMINATORS_PROPERTY = "_xo_discriminator_";

    public static final String XO_IN_DOCUMENT = "_xo_in_document";

    public static final String XO_OUT_DOCUMENT = "_xo_out_document";

    public static final String MONGODB_ID = "_id";

    abstract DBCollection getDBCollection();

    public void setProperty(E entity, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata, Object value) {
        entity.put(metadata.getDatastoreMetadata().getName(), value);
    }

    public boolean hasProperty(E entity, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return entity.containsField(metadata.getDatastoreMetadata().getName());
    }

    public void removeProperty(E entity, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        entity.removeField(metadata.getDatastoreMetadata().getName());
    }

    public Object getProperty(E entity, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return entity.get(metadata.getDatastoreMetadata().getName());
    }

    public void setProperties(E element, Map<String, Object> properties) {
        element.putAll(properties);
    }

    public void removeProperties(E element, Set<String> properties) {
        for (String property : properties) {
            element.removeField(property);
        }
    }
}
