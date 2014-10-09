package com.smbtec.xo.mongodb.impl;

import java.lang.annotation.Annotation;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
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
    private final DBCollection documentCollection;
    private final DBCollection referenceCollection;

    private MongoDbDocumentManager documentManager;
    private MongoDbRelationshipManager relationManager;

    public MongoDbDatastoreSessionImpl(DB database) {
        this.database = database;
        documentCollection = database.getCollection("documents");
        referenceCollection = database.getCollection("relationships");

        documentManager = new MongoDbDocumentManager(documentCollection);
        relationManager = new MongoDbRelationshipManager(documentCollection, referenceCollection);
    }

    public DB getDatabase() {
        return database;
    }

    public DatastoreTransaction getDatastoreTransaction() {
        return new MongoDbDatastoreTransaction();
    }

    public DatastoreEntityManager<Object, DBObject, DocumentMetadata, String, PropertyMetadata> getDatastoreEntityManager() {
        return documentManager;
    }

    public DatastoreRelationManager<DBObject, Object, DBObject, RelationshipMetadata, String, PropertyMetadata> getDatastoreRelationManager() {
        return relationManager;
    }

    public Class<? extends Annotation> getDefaultQueryLanguage() {
        return Query.class;
    }

    public <QL extends Annotation> DatastoreQuery<QL> createQuery(Class<QL> queryLanguage) {
        if (Query.class.equals(queryLanguage)) {
            return (DatastoreQuery<QL>) new JSONQuery(getDocuments());
        }
        throw new XOException("Unsupported query language: " + queryLanguage.getName());
    }

    public void close() {
    }

    public DBCollection getDocuments() {
        return this.documentCollection;
    }

    public DBCollection getReferences() {
        return this.referenceCollection;
    }

    private static class MongoDbDatastoreTransaction implements DatastoreTransaction {

        @Override
        public void begin() {
        }

        @Override
        public void commit() {
        }

        @Override
        public void rollback() {
        }

        @Override
        public boolean isActive() {
            return true;
        }
    }

}
