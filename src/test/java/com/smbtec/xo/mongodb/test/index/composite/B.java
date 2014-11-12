package com.smbtec.xo.mongodb.test.index.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;
import com.smbtec.xo.mongodb.api.annotation.Indexed;

@Document
public interface B {

    @Indexed(name = "myIdx")
    String getValue();

    void setValue(String value);

}
