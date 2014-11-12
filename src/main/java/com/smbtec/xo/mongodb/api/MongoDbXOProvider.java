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
package com.smbtec.xo.mongodb.api;

import java.net.URI;
import java.net.UnknownHostException;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.spi.bootstrap.XODatastoreProvider;
import com.buschmais.xo.spi.datastore.Datastore;
import com.github.fakemongo.Fongo;
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
            if (uri.getScheme().equals("fongodb")) {
                // in memory only for testing purposes
                Fongo fongo = new Fongo("test");
                return new MongoDbDatastore(fongo.getDB("xo"));
            } else {
                MongoClient client = new MongoClient(new MongoClientURI(uri.toString()));
                return new MongoDbDatastore(client.getDB("xo"));
            }
        } catch (UnknownHostException e) {
            throw new XOException("", e);
        }
    }
}
