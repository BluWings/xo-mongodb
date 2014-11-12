package com.smbtec.xo.mongodb.test.bootstrap;

import org.junit.Test;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.XOManagerFactory;
import com.buschmais.xo.api.bootstrap.XO;
import com.smbtec.xo.mongodb.test.bootstrap.composite.A;

public class MongoDbBootstrapTest {

    @Test
    public void bootstrap() {
        XOManagerFactory xoManagerFactory = XO.createXOManagerFactory("MongoDb");
        XOManager xoManager = xoManagerFactory.createXOManager();
        A a = xoManager.create(A.class);
        a.setName("Test");
        xoManager.close();
        xoManagerFactory.close();
    }
}
