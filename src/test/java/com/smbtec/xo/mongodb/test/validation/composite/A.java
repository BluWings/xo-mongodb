package com.smbtec.xo.mongodb.test.validation.composite;

import javax.validation.constraints.NotNull;

import com.smbtec.xo.mongodb.api.annotation.Document;

@Document("A")
public interface A {

    @NotNull
    String getName();

    void setName(String name);

    @NotNull
    B getB();

    void setB(B b);
}
