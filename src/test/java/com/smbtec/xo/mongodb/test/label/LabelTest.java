package com.smbtec.xo.mongodb.test.label;

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
        ImplicitLabel implicitLabel = xoManager.create(ImplicitLabel.class);
        MongoDbDocument mongoDocument = (MongoDbDocument) ((CompositeObject) implicitLabel).getDelegate();
        assertThat(mongoDocument.getLabel(), equalTo("ImplicitLabel"));
    }

    @Test
    public void explicitLabel() {
        XOManager xoManager = getXoManager();
        ExplicitLabel explicitLabel = xoManager.create(ExplicitLabel.class);
        MongoDbDocument mongoDocument = (MongoDbDocument) ((CompositeObject) explicitLabel).getDelegate();
        assertThat(mongoDocument.getLabel(), equalTo("EXPLICIT_LABEL"));
    }
}