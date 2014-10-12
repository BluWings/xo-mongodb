package com.smbtec.xo.mongodb.test.delegate.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;

@Document
public interface A {

    A2B getA2B();

}