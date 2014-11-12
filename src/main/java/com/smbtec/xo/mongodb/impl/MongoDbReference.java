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

import com.mongodb.DBRef;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class MongoDbReference extends MongoDbRelation {

    private MongoDbDocument target;
    private DBRef dbref;

    public MongoDbReference(String label, MongoDbDocument source, DBRef dbref) {
        super(source);
        this.dbref = dbref;
    }

    public MongoDbReference(String label, MongoDbDocument source, MongoDbDocument target) {
        super(source, label);
        this.target = target;
    }

    @Override
    public MongoDbDocument getTarget() {
        if (target == null) {
            target = new MongoDbDocument(dbref.fetch(), dbref.getRef());
        }
        return target;
    }

}
