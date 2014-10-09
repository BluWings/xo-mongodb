package com.smbtec.xo.mongodb.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.xo.spi.annotation.QueryDefinition;

/**
 *
 * @author Lars Martin
 *
 */
@QueryDefinition
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

	/**
	 * The JSON string that defines the query to be executed.
	 */
	String value() default "";

	/**
	 * The fields that should be returned.
	 */
	String fields() default "";
}
