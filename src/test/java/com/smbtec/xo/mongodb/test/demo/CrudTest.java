package com.smbtec.xo.mongodb.test.demo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.mongodb.api.annotation.Document;
import com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest;

@RunWith(Parameterized.class)
public class CrudTest extends AbstractMongoDbXOManagerTest {

    public CrudTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class);
    }

    @Test
    public void create() throws InterruptedException {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        a.setName("Foo");
        xoManager.flush();
        a = xoManager.createQuery("{ name : 'Foo' }", A.class).execute().getSingleResult();
        assertThat(a.getName(), equalTo("Foo"));
        a.setName("Bar");
        xoManager.flush();
        A result = xoManager.createQuery("{ name : 'Bar' }", A.class).execute().getSingleResult();
        xoManager.delete(result);
        try {
            xoManager.createQuery("{}").execute().getSingleResult();
            Assert.fail("An exception is expected.");
        } catch (XOException e) {
        }
    }

    @Document(collection = "A")
    public interface A {

        String getName();

        void setName(String name);

    }
}