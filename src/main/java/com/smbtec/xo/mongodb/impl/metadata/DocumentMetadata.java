package com.smbtec.xo.mongodb.impl.metadata;

import com.buschmais.xo.spi.datastore.DatastoreEntityMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class DocumentMetadata implements DatastoreEntityMetadata<String> {

    private final String discriminator;

    public DocumentMetadata(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getDiscriminator() {
        return discriminator;
    }

}