package com.smbtec.xo.mongodb.impl.metadata;

import com.buschmais.xo.spi.datastore.DatastoreRelationMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class RelationshipMetadata implements DatastoreRelationMetadata<String> {

    private final String label;

    public RelationshipMetadata(final String label) {
        this.label = label;
    }

    public String getDiscriminator() {
        return label;
    }

}