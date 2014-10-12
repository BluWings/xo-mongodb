package com.smbtec.xo.mongodb.test.migration.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;

@Document
public interface C {

    String getName();

    void setName(String name);

}
