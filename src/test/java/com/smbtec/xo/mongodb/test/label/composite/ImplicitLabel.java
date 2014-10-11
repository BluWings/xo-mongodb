package com.smbtec.xo.mongodb.test.label.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;

@Document
public interface ImplicitLabel {

    String getString();

    void setString(String string);

}
