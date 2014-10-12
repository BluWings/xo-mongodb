package com.smbtec.xo.mongodb.test.delegate.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;

@Document("B")
public interface B {

    A2B getA2B();

}