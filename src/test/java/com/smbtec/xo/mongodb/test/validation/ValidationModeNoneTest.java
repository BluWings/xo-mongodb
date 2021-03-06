package com.smbtec.xo.mongodb.test.validation;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest;
import com.smbtec.xo.mongodb.test.validation.composite.A;
import com.smbtec.xo.mongodb.test.validation.composite.B;

@RunWith(Parameterized.class)
public class ValidationModeNoneTest extends AbstractMongoDbXOManagerTest {

    public ValidationModeNoneTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(asList(A.class, B.class), Collections.<Class<?>> emptyList(), ValidationMode.NONE,
                ConcurrencyMode.SINGLETHREADED, Transaction.TransactionAttribute.NONE);
    }

    @Test
    public void validationOnCommitAfterInsert() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        assertThat(a.getName(), nullValue());
    }

}
