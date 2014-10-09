package com.smbtec.xo.mongodb.test;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.test.AbstractXOManagerTest;
import com.mongodb.BasicDBObject;
import com.smbtec.xo.mongodb.api.MongoDbDatastoreSession;
import com.smbtec.xo.mongodb.api.MongoDbXOProvider;

import static com.smbtec.xo.mongodb.test.AbstractMongoDbXOManagerTest.MongoDatabase.MEMORY;

public abstract class AbstractMongoDbXOManagerTest extends AbstractXOManagerTest {

	protected enum MongoDatabase implements AbstractXOManagerTest.Database {

		MEMORY("mongodb:///");

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

	@BeforeClass
	public static void startServer() {
		// GraphDatabaseService graphDatabaseService = new
		// TestGraphDatabaseFactory().newImpermanentDatabase();
		// server = new WrappingNeoServer((GraphDatabaseAPI)
		// graphDatabaseService);
		// server.start();
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

	@AfterClass
	public static void stopServer() {
		// server.stop();
	}

	protected void dropDatabase() {
		XOManager manager = getXoManager();
		manager.currentTransaction().begin();
		MongoDbDatastoreSession session = manager.getDatastoreSession(MongoDbDatastoreSession.class);
		session.getDocuments().remove(new BasicDBObject());
		session.getDocuments().dropIndexes();
		session.getReferences().remove(new BasicDBObject());
		session.getReferences().dropIndexes();
		manager.currentTransaction().commit();
	}
}