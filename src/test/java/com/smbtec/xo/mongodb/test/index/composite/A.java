package com.smbtec.xo.mongodb.test.index.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;
import com.smbtec.xo.mongodb.api.annotation.Indexed;

@Document
public interface A {

    @Indexed
    String getValue();

    void setValue(String value);

}
