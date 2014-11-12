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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.buschmais.xo.spi.annotation.RelationDefinition;

/**
 * Marks a field of an entity to be stored using a {@link com.mongodb.DBRef}.
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
@Documented
@RelationDefinition
@Retention(RUNTIME)
@Target({ METHOD })
public @interface DBRef {

    /**
     *
     */
    boolean lazy() default true;

    /**
     *
     */
    boolean idOnly() default false;

}
