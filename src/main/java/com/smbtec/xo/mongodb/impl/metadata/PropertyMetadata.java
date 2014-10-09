package com.smbtec.xo.mongodb.impl.metadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class PropertyMetadata {

	private final String name;

	public PropertyMetadata(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}