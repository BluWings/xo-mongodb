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
import java.util.Collection;
import java.util.Map;

import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
import com.buschmais.xo.spi.reflection.AnnotatedElement;
import com.buschmais.xo.spi.reflection.AnnotatedMethod;
import com.buschmais.xo.spi.reflection.AnnotatedType;
import com.buschmais.xo.spi.reflection.PropertyMethod;
import com.google.common.base.Strings;
import com.smbtec.xo.mongodb.api.annotation.Document;
import com.smbtec.xo.mongodb.api.annotation.Index;
import com.smbtec.xo.mongodb.api.annotation.Indexed;
import com.smbtec.xo.mongodb.api.annotation.Indexes;
import com.smbtec.xo.mongodb.api.annotation.Property;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.mongodb.impl.metadata.RelationshipMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbMetadataFactory implements DatastoreMetadataFactory<DocumentMetadata, String, RelationshipMetadata, String> {

    public DocumentMetadata createEntityMetadata(AnnotatedType annotatedType, Map<Class<?>, TypeMetadata> metadataByType) {
        final Document annotation = annotatedType.getAnnotation(Document.class);
        String collection = null;
        if (annotation != null) {
            collection = annotation.collection();
            if (Strings.isNullOrEmpty(collection)) {
                collection = annotatedType.getName();
            }
        }
        final Index indexAnnotation = annotatedType.getAnnotation(Index.class);
        if (indexAnnotation != null) {
            com.smbtec.xo.mongodb.impl.Index index = new com.smbtec.xo.mongodb.impl.Index(indexAnnotation);
            return new DocumentMetadata(collection, Arrays.asList(index));
        }
        final Indexes indexAnnotations = annotatedType.getAnnotation(Indexes.class);
        if (indexAnnotations != null) {
            Collection<IndexDefinition> indexes = new ArrayList<>();
            for (Index index : indexAnnotations.value()) {
                indexes.add(new com.smbtec.xo.mongodb.impl.Index(index));
            }
            return new DocumentMetadata(collection, indexes);
        }
        return new DocumentMetadata(collection);
    }

    public RelationshipMetadata createRelationMetadata(AnnotatedElement<?> annotatedElement, Map<Class<?>, TypeMetadata> metadataByType) {
        boolean isCollection = false;
        String label = annotatedElement.getName();
        if (annotatedElement instanceof PropertyMethod) {
            PropertyMethod propertyMethod = (PropertyMethod) annotatedElement;
            if (Collection.class.isAssignableFrom(propertyMethod.getType())) {
                isCollection = true;
            }
            Property property = propertyMethod.getAnnotation(Property.class);
            if (property != null && !Strings.isNullOrEmpty(property.value())) {
                label = property.value();
            }
        }
        return new RelationshipMetadata(label, isCollection);
    }

    public <ImplementedByMetadata> ImplementedByMetadata createImplementedByMetadata(AnnotatedMethod annotatedMethod) {
        return null;
    }

    public <CollectionPropertyMetadata> CollectionPropertyMetadata createCollectionPropertyMetadata(PropertyMethod propertyMethod) {
        return null;
    }

    public <ReferencePropertyMetadata> ReferencePropertyMetadata createReferencePropertyMetadata(PropertyMethod propertyMethod) {
        return null;
    }

    public PropertyMetadata createPropertyMetadata(PropertyMethod propertyMethod) {
        final Property property = propertyMethod.getAnnotationOfProperty(Property.class);
        final String name = property != null ? property.value() : propertyMethod.getName();
        final Indexed indexedAnnotation = propertyMethod.getAnnotation(Indexed.class);
        if (indexedAnnotation != null) {
            com.smbtec.xo.mongodb.impl.Index index = new com.smbtec.xo.mongodb.impl.Index(name, indexedAnnotation);
            return new PropertyMetadata(name, index);
        }
        return new PropertyMetadata(name);
    }

    public <IndexedPropertyMetadata> IndexedPropertyMetadata createIndexedPropertyMetadata(PropertyMethod propertyMethod) {
        return null;
    }
}
