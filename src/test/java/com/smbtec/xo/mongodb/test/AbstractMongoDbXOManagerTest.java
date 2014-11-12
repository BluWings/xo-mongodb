package com.smbtec.xo.mongodb.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.After;

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.test.AbstractXOManagerTest;
import com.mongodb.CommandFailureException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.smbtec.xo.mongodb.api.MongoDbDatastoreSession;
import com.smbtec.xo.mongodb.api.MongoDbXOProvider;

import static com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest.MongoDatabase.MEMORY;
import static com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest.MongoDatabase.REMOTE;

public abstract class AbstractMongoDbXOManagerTest extends AbstractXOManagerTest {

    protected enum MongoDatabase implements AbstractXOManagerTest.Database {

        REMOTE("mongodb://127.0.0.1:27017"), MEMORY("fongodb:///");

        private URI uri;

        private MongoDatabase(String uri) {
            try {
                this.uri = new URI(uri);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public URI getUri() {
            return uri;
        }

        public Class<?> getProvider() {
            return MongoDbXOProvider.class;
        }

    }

    protected AbstractMongoDbXOManagerTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    protected static Collection<Object[]> xoUnits(Class<?>... types) {
        return xoUnits(Arrays.asList(MEMORY), Arrays.asList(types), Collections.<Class<?>> emptyList(),
                ValidationMode.AUTO, ConcurrencyMode.SINGLETHREADED, Transaction.TransactionAttribute.NONE);
    }

    protected static Collection<Object[]> xoUnits(List<? extends Class<?>> types,
            List<? extends Class<?>> instanceListeners, ValidationMode validationMode, ConcurrencyMode concurrencyMode,
            Transaction.TransactionAttribute transactionAttribute) {
        return xoUnits(Arrays.asList(MEMORY), types, instanceListeners, validationMode, concurrencyMode,
                transactionAttribute);
    }

    @After
    public void cleanUp() {
        XOManager manager = getXoManager();
        MongoDbDatastoreSession session = manager.getDatastoreSession(MongoDbDatastoreSession.class);
        DB database = session.getDatabase();
        for (String collectionName : database.getCollectionNames()) {
            try {
                DBCollection collection = database.getCollection(collectionName);
                collection.dropIndexes();
                collection.drop();
            } catch (CommandFailureException failure) {
            }
        }
    }

    @Override
    protected void dropDatabase() {
    }
}