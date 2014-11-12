package com.smbtec.xo.mongodb.test.example.composite;

import java.util.List;

import com.smbtec.xo.mongodb.api.annotation.Document;

@Document
public interface A extends Named {

    String getValue();

    void setValue(String value);

    A getParent();

    void setParent(A a);

    List<A> getChildren();
}
