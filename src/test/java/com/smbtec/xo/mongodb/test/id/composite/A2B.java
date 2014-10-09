package com.smbtec.xo.mongodb.test.id.composite;

import com.smbtec.xo.mongodb.api.annotation.Relation;

@Relation
public interface A2B {

	@Relation.Outgoing
	A getA();

	@Relation.Incoming
	B getB();

}