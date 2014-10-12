package com.smbtec.xo.mongodb.test.delegate.composite;

import com.smbtec.xo.mongodb.api.annotation.Relation;
import com.smbtec.xo.mongodb.api.annotation.Relation.Incoming;
import com.smbtec.xo.mongodb.api.annotation.Relation.Outgoing;

@Relation("RELATION")
public interface A2B {

    @Outgoing
    A getA();

    @Incoming
    B getB();
}
