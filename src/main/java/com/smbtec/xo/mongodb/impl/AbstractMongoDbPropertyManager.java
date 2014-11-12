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

import java.util.Map;
import java.util.Set;

import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public abstract class AbstractMongoDbPropertyManager<E extends MongoDbObject> implements
        DatastorePropertyManager<E, PropertyMetadata> {

    public void setProperty(E entity, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata, Object value) {
        entity.getDelegate().put(metadata.getDatastoreMetadata().getName(), value);
    }

    public boolean hasProperty(E entity, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return entity.getDelegate().containsField(metadata.getDatastoreMetadata().getName());
    }

    public void removeProperty(E entity, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        entity.getDelegate().removeField(metadata.getDatastoreMetadata().getName());
    }

    public Object getProperty(E entity, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return entity.getDelegate().get(metadata.getDatastoreMetadata().getName());
    }

    public void setProperties(E element, Map<String, Object> properties) {
        element.getDelegate().putAll(properties);
    }

    public void removeProperties(E element, Set<String> properties) {
        for (String property : properties) {
            element.getDelegate().removeField(property);
        }
    }
}
