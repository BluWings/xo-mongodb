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
package com.smbtec.xo.mongodb.impl.metadata;

import java.util.Collection;
import java.util.Collections;

import com.buschmais.xo.spi.datastore.DatastoreEntityMetadata;
import com.smbtec.xo.mongodb.impl.IndexDefinition;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class DocumentMetadata implements DatastoreEntityMetadata<String> {

    private final String collectionName;

    private Collection<IndexDefinition> indexes = Collections.emptyList();

    public DocumentMetadata(String collectionName) {
        if (collectionName == null) {
            throw new IllegalArgumentException("Collection name must not be null");
        }
        this.collectionName = collectionName;
    }

    public DocumentMetadata(String collectionName, Collection<IndexDefinition> indexes) {
        this.collectionName = collectionName;
        this.indexes = indexes;
    }

    @Override
    public String getDiscriminator() {
        return collectionName;
    }

    public Collection<IndexDefinition> getIndexDefinitions() {
        return indexes;
    }

}