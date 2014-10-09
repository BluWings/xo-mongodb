package com.smbtec.xo.mongodb.impl;

import java.util.HashMap;
import java.util.Map;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.smbtec.xo.mongodb.api.annotation.Query;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class JSONQuery implements DatastoreQuery<Query> {

	private DBCollection collection;

	public JSONQuery(DBCollection collection) {
		this.collection = collection;
	}

	public ResultIterator<Map<String, Object>> execute(String query, Map<String, Object> parameters) {
		return _execute(query, parameters);
	}

	public ResultIterator<Map<String, Object>> execute(Query query, Map<String, Object> parameters) {
		return _execute(query.value(), parameters);
	}

	private ResultIterator<Map<String, Object>> _execute(final String json, final Map<String, Object> parameters) {
		// TODO
		final DBCursor matches = collection.find((DBObject) JSON.parse(json));

		return new ResultIterator<Map<String, Object>>() {

			public boolean hasNext() {
				return matches.hasNext();
			}

			public Map<String, Object> next() {
				DBObject next = matches.next();
				final Map<String, Object> result = new HashMap<String, Object>();
				result.put("result", next);
				return result;
			}

			public void remove() {
				throw new XOException("Remove operation is not supported for query results.");
			}

			public void close() {
				matches.close();
			}
		};

	}
}
