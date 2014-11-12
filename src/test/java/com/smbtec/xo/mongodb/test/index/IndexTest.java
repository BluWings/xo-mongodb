package com.smbtec.xo.mongodb.test.index;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.smbtec.xo.mongodb.api.MongoDbDatastoreSession;
import com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest;
import com.smbtec.xo.mongodb.test.index.composite.A;
import com.smbtec.xo.mongodb.test.index.composite.B;
import com.smbtec.xo.mongodb.test.index.composite.C;
import com.smbtec.xo.mongodb.test.index.composite.D;

@RunWith(Parameterized.class)
public class IndexTest extends AbstractMongoDbXOManagerTest {

    public IndexTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, B.class, C.class, D.class);
    }

    @Test
    public void index() {
        XOManager xoManager = getXoManager();
        MongoDbDatastoreSession datastoreSession = xoManager.getDatastoreSession(MongoDbDatastoreSession.class);
        DB database = datastoreSession.getDatabase();
        List<DBObject> indexInfo = database.getCollection("A").getIndexInfo();
        assertThat(indexInfo, hasSize(2));
    }
}
