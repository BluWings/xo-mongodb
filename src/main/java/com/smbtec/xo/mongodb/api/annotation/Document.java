package com.smbtec.xo.mongodb.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.xo.spi.annotation.EntityDefinition;

/**
 * This annotation marks a vertex as an entity.
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
@EntityDefinition
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Document {

    String DEFAULT_VALUE = "";

    /**
     * @return Returns the name of the type as {@link String}.
     */
    String value() default "";

}
