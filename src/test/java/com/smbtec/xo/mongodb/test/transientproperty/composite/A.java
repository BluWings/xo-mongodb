package com.smbtec.xo.mongodb.test.transientproperty.composite;

import com.buschmais.xo.api.annotation.Transient;
import com.smbtec.xo.mongodb.api.annotation.Document;

@Document
public interface A {

    String getValue();

    void setValue(String value);

    @Transient
    String getTransientValue();

    void setTransientValue(String transientValue);
}
