package com.smbtec.xo.mongodb.test.label;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest;
import com.smbtec.xo.mongodb.test.label.composite.ExplicitLabel;
import com.smbtec.xo.mongodb.test.label.composite.ImplicitLabel;

@RunWith(Parameterized.class)
public class LabelTest extends AbstractMongoDbXOManagerTest {

    public LabelTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(ImplicitLabel.class, ExplicitLabel.class);
    }

    @Test
    public void implicitLabel() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        ImplicitLabel implicitLabel = xoManager.create(ImplicitLabel.class);
        assertThat(
                xoManager
                        .createQuery("{ \"_xo_discriminator_ImplicitLabel\": \"ImplicitLabel\" }", ImplicitLabel.class)
                        .execute().getSingleResult(), is(implicitLabel));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void explicitLabel() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        ExplicitLabel explicitLabel = xoManager.create(ExplicitLabel.class);
        assertThat(
                xoManager
                        .createQuery("{ \"_xo_discriminator_EXPLICIT_LABEL\": \"EXPLICIT_LABEL\" }",
                                ExplicitLabel.class).execute().getSingleResult(), is(explicitLabel));
        xoManager.currentTransaction().commit();
    }
}