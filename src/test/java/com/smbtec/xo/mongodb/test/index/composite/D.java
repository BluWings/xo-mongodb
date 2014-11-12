package com.smbtec.xo.mongodb.test.index.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;
import com.smbtec.xo.mongodb.api.annotation.Index;
import com.smbtec.xo.mongodb.api.annotation.Indexes;

@Document
@Indexes({ @Index("{ 'value' : '-1' }"), @Index("{ 'name' : '1' }") })
public interface D {

    String getValue();

    void setValue(String value);

    String getName();

    void setName(String name);

}
