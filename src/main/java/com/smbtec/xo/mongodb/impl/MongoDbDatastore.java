package com.smbtec.xo.mongodb.impl;

import java.util.Collection;

import com.buschmais.xo.spi.datastore.Datastore;
import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
import com.mongodb.DB;
import com.smbtec.xo.mongodb.api.MongoDbDatastoreSession;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;
import com.smbtec.xo.mongodb.impl.metadata.RelationshipMetadata;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbDatastore implements
		Datastore<MongoDbDatastoreSession, DocumentMetadata, String, RelationshipMetadata, String> {

	private DB database;

	private final MongoDbMetadataFactory metadataFactory = new MongoDbMetadataFactory();

	public MongoDbDatastore(DB database) {
		this.database = database;
	}

	public void init(Collection<TypeMetadata> registeredMetadata) {
	}

	public DatastoreMetadataFactory<DocumentMetadata, String, RelationshipMetadata, String> getMetadataFactory() {
		return metadataFactory;
	}

	public MongoDbDatastoreSession createSession() {
		return new MongoDbDatastoreSessionImpl(database);
	}

	public void close() {
	}

}
