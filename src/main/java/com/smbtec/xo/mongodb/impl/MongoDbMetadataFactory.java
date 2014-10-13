package com.smbtec.xo.mongodb.impl;

import java.util.Map;

import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
import com.buschmais.xo.spi.reflection.AnnotatedElement;
import com.buschmais.xo.spi.reflection.AnnotatedMethod;
import com.buschmais.xo.spi.reflection.AnnotatedType;
import com.buschmais.xo.spi.reflection.PropertyMethod;
import com.google.common.base.CaseFormat;
import com.smbtec.xo.mongodb.api.annotation.Document;
import com.smbtec.xo.mongodb.api.annotation.Property;
import com.smbtec.xo.mongodb.api.annotation.Relation;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.mongodb.impl.metadata.RelationshipMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbMetadataFactory implements
        DatastoreMetadataFactory<DocumentMetadata, String, RelationshipMetadata, String> {

    public DocumentMetadata createEntityMetadata(AnnotatedType annotatedType, Map<Class<?>, TypeMetadata> metadataByType) {
        final Document annotation = annotatedType.getAnnotation(Document.class);
        String value = null;
        if (annotation != null) {
            value = annotation.value();
            if ((value == null) || (value.isEmpty())) {
                value = annotatedType.getName();
            }
        }
        return new DocumentMetadata(value);
    }

    public RelationshipMetadata createRelationMetadata(AnnotatedElement<?> annotatedElement,
            Map<Class<?>, TypeMetadata> metadataByType) {
        Relation relationAnnotation;
        if (annotatedElement instanceof PropertyMethod) {
            relationAnnotation = ((PropertyMethod) annotatedElement).getAnnotationOfProperty(Relation.class);
        } else {
            relationAnnotation = annotatedElement.getAnnotation(Relation.class);
        }
        String name = null;
        if (relationAnnotation != null) {
            String value = relationAnnotation.value();
            if (!Relation.DEFAULT_VALUE.equals(value)) {
                name = value;
            }
        }
        if (name == null) {
            name = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, annotatedElement.getName());
        }
        return new RelationshipMetadata(name);
    }

    public <ImplementedByMetadata> ImplementedByMetadata createImplementedByMetadata(AnnotatedMethod annotatedMethod) {
        return null;
    }

    public <CollectionPropertyMetadata> CollectionPropertyMetadata createCollectionPropertyMetadata(
            PropertyMethod propertyMethod) {
        return null;
    }

    public <ReferencePropertyMetadata> ReferencePropertyMetadata createReferencePropertyMetadata(
            PropertyMethod propertyMethod) {
        return null;
    }

    public PropertyMetadata createPropertyMetadata(PropertyMethod propertyMethod) {
        final Property property = propertyMethod.getAnnotationOfProperty(Property.class);
        final String name = property != null ? property.value() : propertyMethod.getName();
        return new PropertyMetadata(name);
    }

    public <IndexedPropertyMetadata> IndexedPropertyMetadata createIndexedPropertyMetadata(PropertyMethod propertyMethod) {
        return null;
    }
}
