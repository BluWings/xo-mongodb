package com.smbtec.xo.mongodb.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.smbtec.xo.mongodb.api.IndexDirection;
import com.smbtec.xo.mongodb.api.annotation.Indexed;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class Index implements IndexDefinition {

    private final Map<String, IndexDirection> keys = new LinkedHashMap<>();

    private String name;

    private boolean unique = false;

    private boolean dropDuplicates = false;

    private boolean sparse = false;

    private boolean background = false;

    private long expire = -1;

    private Index(String name, boolean unique, boolean background, boolean dropDuplicates, boolean sparse, long expireAfterSeconds) {
        if (!Strings.isNullOrEmpty(name)) {
            named(name);
        }
        if (unique) {
            unique();
        }
        if (background) {
            background();
        }
        if (dropDuplicates) {
            dropDuplicates();
        }
        if (sparse) {
            sparse();
        }
        if (expireAfterSeconds > -1) {
            expireAfterSeconds(expireAfterSeconds);
        }
    }

    public Index(String field, Indexed indexed) {
        this(indexed.name(), indexed.unique(), indexed.background(), indexed.dropDups(), indexed.sparse(), indexed.expireAfterSeconds());
        on(field, indexed.direction());
    }

    public Index(com.smbtec.xo.mongodb.api.annotation.Index index) {
        this(index.name(), index.unique(), index.background(), index.dropDups(), index.sparse(), index.expireAfterSeconds());
        DBObject keys = (DBObject) JSON.parse(index.value());
        for (String key : keys.keySet()) {
            on (key, IndexDirection.byValue(Integer.valueOf((String)keys.get(key))));
        }
    }

    public Index on(String field, IndexDirection direction) {
        keys.put(field, direction);
        return this;
    }

    public Index named(String name) {
        this.name = name;
        return this;
    }

    public Index unique() {
        this.unique = true;
        return this;
    }

    public Index background() {
        this.background = true;
        return this;
    }

    public Index dropDuplicates() {
        this.dropDuplicates = true;
        return this;
    }

    public Index sparse() {
        this.sparse = true;
        return this;
    }

    public Index expireAfterSeconds(long seconds) {
        this.expire = seconds;
        return this;
    }

    @Override
    public DBObject getIndexKeys() {
        BasicDBObjectBuilder builder = new BasicDBObjectBuilder();
        for (Map.Entry<String, IndexDirection> entry : keys.entrySet()) {
            builder.add(entry.getKey(), entry.getValue().equals(IndexDirection.ASCENDING) ? 1 : -1);
        }
        return builder.get();
    }

    @Override
    public DBObject getIndexOptions() {
        BasicDBObjectBuilder builder = new BasicDBObjectBuilder();
        if (!Strings.isNullOrEmpty(this.name)) {
            builder.add("name", this.name);
        }
        if (this.unique) {
            builder.add("unique", true);
        }
        if (this.background) {
            builder.add("background", true);
        }
        if (this.dropDuplicates) {
            builder.add("dropDups", true);
        }
        if (this.sparse) {
            builder.add("sparse", true);
        }
        if (this.expire > -1) {
            builder.add("expireAfterSeconds", this.expire);
        }
        return builder.get();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(Index.class).add("keys", keys).add("name", name).add("unique", unique).add("background", background)
                .add("dropDuplicates", dropDuplicates).add("sparse", sparse).add("expire", expire).omitNullValues().toString();
    }

}
