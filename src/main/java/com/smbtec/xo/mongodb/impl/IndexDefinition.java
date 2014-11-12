/**
 *
 */
package com.smbtec.xo.mongodb.impl;

import com.mongodb.DBObject;

/**
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public interface IndexDefinition {

    DBObject getIndexKeys();

    DBObject getIndexOptions();

}
