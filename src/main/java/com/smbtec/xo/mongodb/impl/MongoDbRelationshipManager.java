package com.smbtec.xo.mongodb.impl;

import java.util.Map;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata.Direction;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.mongodb.impl.metadata.RelationshipMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbRelationshipManager extends AbstractMongoDbPropertyManager<DBObject> implements
        DatastoreRelationManager<DBObject, Object, DBObject, RelationshipMetadata, String, PropertyMetadata> {

    private DBCollection relationships;
    private DBCollection documents;

    public MongoDbRelationshipManager(DBCollection documents, DBCollection relationships) {
        this.documents = documents;
        this.relationships = relationships;
    }

    @Override
    DBCollection getDBCollection() {
        return relationships;
    }

    public boolean isRelation(Object o) {
        return DBObject.class.isAssignableFrom(o.getClass());
    }

    public String getRelationDiscriminator(DBObject relation) {
        return (String) relation.get(XO_DISCRIMINATORS_PROPERTY);
    }

    public DBObject createRelation(DBObject source, RelationTypeMetadata<RelationshipMetadata> metadata,
            Direction direction, DBObject target,
            Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        final String name = metadata.getDatastoreMetadata().getDiscriminator();
        BasicDBObject relation = new BasicDBObject();
        relationships.insert(relation.append("_in_document", source.get(MONGODB_ID))
                .append("_out_document", target.get(MONGODB_ID)).append(XO_DISCRIMINATORS_PROPERTY, name));
        return relation;
    }

    public void deleteRelation(DBObject relation) {
        relationships.remove(relation);
    }

    public Object getRelationId(DBObject relation) {
        return relation.get(MONGODB_ID);
    }

    public void flushRelation(DBObject relation) {
        getDBCollection().save(relation);
    }

    public boolean hasSingleRelation(DBObject source, RelationTypeMetadata<RelationshipMetadata> metadata,
            Direction direction) {
        DBObject query = getQueryObject(source, metadata, direction);
        return relationships.find(query).count() > 0;
    }

    public DBObject getSingleRelation(DBObject source, RelationTypeMetadata<RelationshipMetadata> metadata,
            Direction direction) {
        DBObject query = getQueryObject(source, metadata, direction);
        DBCursor matches = relationships.find(query);
        if (!matches.hasNext()) {
            return null;
        }
        if (matches.count() > 1) {
            throw new XOException("More than one relationship found");
        }
        return matches.next();
    }

    public Iterable<DBObject> getRelations(DBObject source, RelationTypeMetadata<RelationshipMetadata> metadata,
            Direction direction) {
        DBObject query = getQueryObject(source, metadata, direction);
        DBCursor matches = relationships.find(query);
        return matches.toArray();
    }

    public DBObject getFrom(DBObject relation) {
        return documents.findOne(QueryBuilder.start(MONGODB_ID).is(relation.get("_in_document")).get());
    }

    public DBObject getTo(DBObject relation) {
        return documents.findOne(QueryBuilder.start(MONGODB_ID).is(relation.get("_out_document")).get());
    }

    private DBObject getQueryObject(DBObject source, RelationTypeMetadata<RelationshipMetadata> metadata,
            Direction direction) {
        switch (direction) {
        case FROM:
            return getQueryObject("_in_document", source, metadata.getDatastoreMetadata().getDiscriminator());
        case TO:
            return getQueryObject("_out_document", source, metadata.getDatastoreMetadata().getDiscriminator());
        default:
            throw new XOException("Unkown direction '" + direction.name() + "'.");
        }

    }

    private DBObject getQueryObject(String inOut, DBObject source, String discriminator) {
        return QueryBuilder
                .start()
                .and(QueryBuilder.start(inOut).is(source.get(MONGODB_ID)).get(),
                        QueryBuilder.start(XO_DISCRIMINATORS_PROPERTY).is(discriminator).get()).get();

    }

}
