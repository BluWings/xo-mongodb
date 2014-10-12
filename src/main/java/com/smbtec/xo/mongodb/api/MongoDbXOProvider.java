package com.smbtec.xo.mongodb.api;

import java.net.URI;
import java.net.UnknownHostException;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.spi.bootstrap.XODatastoreProvider;
import com.buschmais.xo.spi.datastore.Datastore;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.smbtec.xo.mongodb.impl.MongoDbDatastore;
import com.smbtec.xo.mongodb.impl.metadata.DocumentMetadata;
import com.smbtec.xo.mongodb.impl.metadata.RelationshipMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbXOProvider implements XODatastoreProvider<DocumentMetadata, String, RelationshipMetadata, String> {

    public Datastore<MongoDbDatastoreSession, DocumentMetadata, String, RelationshipMetadata, String> createDatastore(
            XOUnit xoUnit) {
        if (xoUnit == null) {
            throw new IllegalArgumentException("XOUnit must not be null");
        }
        final URI uri = xoUnit.getUri();
        if (uri == null) {
            throw new XOException("No URI is specified for the store.");
        }

        try {
            MongoClient client = new MongoClient(new MongoClientURI(uri.toString()));
            return new MongoDbDatastore(client.getDB("xo"));
        } catch (UnknownHostException e) {
            throw new XOException("", e);
        }
    }
}
