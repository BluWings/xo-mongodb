package com.smbtec.xo.mongodb.test.id;

import com.buschmais.xo.api.CompositeObject;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest;
import com.smbtec.xo.mongodb.test.id.composite.A;
import com.smbtec.xo.mongodb.test.id.composite.B;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URISyntaxException;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class IdTest extends AbstractMongoDbXOManagerTest {

    public IdTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, B.class);
    }

    @Test
    public void id() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        B b = xoManager.create(B.class);
        Object aId = xoManager.getId(a);
        assertThat(aId, notNullValue());
        assertThat(((CompositeObject) a).getId(), equalTo(aId));
        Object bId = xoManager.getId(b);
        assertThat(bId, notNullValue());
        assertThat(((CompositeObject) b).getId(), equalTo(bId));
    }
}
