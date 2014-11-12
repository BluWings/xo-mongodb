/*
 * eXtended Objects - MongoDB Binding
 *
 * Copyright (C) 2014 SMB GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.smbtec.xo.mongodb.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.smbtec.xo.mongodb.api.IndexDirection;

/**
 * Marks a field of a document to be indexed.
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Indexed {

    /**
     *
     * @return
     */
    boolean unique() default false;

    /**
     *
     * @return
     */
    IndexDirection direction() default IndexDirection.ASCENDING;

    /**
     *
     * @return
     */
    String name() default "";

    /**
     *
     * @return
     */
    boolean dropDups() default false;

    /**
     *
     * @return
     */
    boolean background() default false;

    /**
     *
     * @return
     */
    boolean sparse() default false;

    /**
     *
     * @return
     */
    int expireAfterSeconds() default -1;
}
