package com.smbtec.xo.mongodb.test.delegate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.CompositeObject;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.mongodb.impl.MongoDbDocument;
import com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest;
import com.smbtec.xo.mongodb.test.delegate.composite.A;
import com.smbtec.xo.mongodb.test.delegate.composite.B;

@RunWith(Parameterized.class)
public class DelegateTest extends AbstractMongoDbXOManagerTest {

    public DelegateTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, B.class);
    }

    @Test
    public void document() {
        XOManager xoManager = getXoManager();
        MongoDbDocument dbObject = ((CompositeObject) xoManager.create(A.class)).getDelegate();
        assertThat(dbObject.getLabel(), equalTo(A.class.getSimpleName()));
    }

}
