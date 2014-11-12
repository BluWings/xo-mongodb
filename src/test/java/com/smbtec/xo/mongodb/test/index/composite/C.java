package com.smbtec.xo.mongodb.test.index.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;
import com.smbtec.xo.mongodb.api.annotation.Index;

@Document
@Index(value = "{ 'value' : '-1' }")
public interface C {

    String getValue();

    void setValue(String value);

}
