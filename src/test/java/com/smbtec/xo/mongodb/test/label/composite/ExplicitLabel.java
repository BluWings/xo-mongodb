package com.smbtec.xo.mongodb.test.label.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;

@Document("EXPLICIT_LABEL")
public interface ExplicitLabel {

    String getString();

    void setString(String string);

}
