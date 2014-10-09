package com.smbtec.xo.mongodb.test.bootstrap.composite;

import com.smbtec.xo.mongodb.api.annotation.Document;

@Document
public interface A {

	String getName();

	void setName(String name);
}
