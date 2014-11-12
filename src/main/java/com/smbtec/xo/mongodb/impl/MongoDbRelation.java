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

import java.util.Objects;

/**
 *
 * @author lars
 *
 */
public abstract class MongoDbRelation extends MongoDbObject {

    private MongoDbDocument source;

    private String label;

    MongoDbRelation(MongoDbDocument source) {
        super();
        if (Objects.isNull(source)) {
            throw new IllegalArgumentException("Source must not be null");
        }
        this.source = source;
    }

    MongoDbRelation(MongoDbDocument source, String label) {
        this(source);
        this.label = label;
    }

    /**
     * Return the source of the relation.
     */
    public MongoDbDocument getSource() {
        return this.source;
    }

    /**
     * Return the label associated with the relation.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Return the target of the relation.
     */
    abstract MongoDbDocument getTarget();

}
