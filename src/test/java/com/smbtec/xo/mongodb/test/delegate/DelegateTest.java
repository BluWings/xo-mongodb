package com.smbtec.xo.mongodb.test.delegate;

import com.buschmais.xo.api.CompositeObject;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.mongodb.DBObject;
import com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest;
import com.smbtec.xo.mongodb.test.delegate.composite.A;
import com.smbtec.xo.mongodb.test.delegate.composite.A2B;
import com.smbtec.xo.mongodb.test.delegate.composite.B;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.buschmais.xo.api.Query.Result;
import static com.buschmais.xo.api.Query.Result.CompositeRowObject;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class DelegateTest extends AbstractMongoDbXOManagerTest {

    public DelegateTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, B.class, A2B.class);
    }

    @Test
    public void entity() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        DBObject dbObject = ((CompositeObject) xoManager.create(A.class)).getDelegate();
        assertThat(dbObject.get("_xo_discriminator_" + A.class.getSimpleName()), equalTo(A.class.getSimpleName()));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void relation() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        A a = xoManager.create(A.class);
        B b = xoManager.create(B.class);
        xoManager.create(a, A2B.class, b);
        List<A> r = executeQuery("{ _xo_discriminator_A : 'A' }").getColumn("result");
        assertThat(r.size(), equalTo(1));
        DBObject relation = ((CompositeObject) r.get(0).getA2B()).getDelegate();
        assertThat(relation.get("_xo_discriminator_"), equalTo("RELATION"));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void row() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        A a = xoManager.create(A.class);
        Result<CompositeRowObject> row = xoManager.createQuery("{ _xo_discriminator_A : 'A' }").execute();
        Map<String, Object> delegate = row.getSingleResult().getDelegate();
        assertThat(delegate, IsMapContaining.<String, Object> hasEntry("result", a));
        xoManager.currentTransaction().commit();
    }
}
