package com.smbtec.xo.mongodb.test.migration.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;

@Document
public interface A {

    String getValue();

    void setValue(String value);

}