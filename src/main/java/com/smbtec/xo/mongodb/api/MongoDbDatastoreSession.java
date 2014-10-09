package com.smbtec.xo.mongodb.api;

import com.buschmais.xo.spi.datastore.DatastoreSession;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;
import com.smbtec.xo.mongodb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.mongodb.impl.metadata.RelationshipMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public interface MongoDbDatastoreSession
        extends
        DatastoreSession<Object, DBObject, DocumentMetadata, String, Object, DBObject, RelationshipMetadata, String, PropertyMetadata> {

    public DB getDatabase();

    public DBCollection getReferences();

    public DBCollection getDocuments();
}
