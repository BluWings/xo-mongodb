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

import com.buschmais.xo.spi.datastore.DatastoreRelationMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class RelationshipMetadata implements DatastoreRelationMetadata<String> {

    private String label;
    private boolean isCollectionType;

    public RelationshipMetadata(String discriminator, boolean isCollectionType) {
        this.label = discriminator;
        this.isCollectionType = isCollectionType;
    }

    @Override
    public String getDiscriminator() {
        return label;
    }

    /**
     *
     * @return
     */
    public boolean isCollectionType() {
        return isCollectionType;
    }
}