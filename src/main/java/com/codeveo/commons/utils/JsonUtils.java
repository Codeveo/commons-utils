/*
 * Copyright 2020 Codeveo Ltd.
 *
 * Written by Ladislav Klenovic <lklenovic@codeveo.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.codeveo.commons.utils;

import com.codeveo.commons.utils.exception.CodeveoUtilsException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = createDefaultObjectMapper();

    private JsonUtils() {
    }

    /**
     * Create JSON for a given input. Default object mapper is used for marshalling.
     *
     * @param object input object
     * @return Created JSON
     */
    public static String toJson(final Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new CodeveoUtilsException("Error while converting object to JSON", e);
        }
    }

    public static <T> T fromJson(final String json, final Class<T> targetClass) {
        try {
            return OBJECT_MAPPER.readValue(json, targetClass);
        } catch (final JsonProcessingException e) {
            throw new CodeveoUtilsException("Error while converting object from JSON", e);
        }
    }

    public static <T> T fromJson(final String json, final TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (final JsonProcessingException e) {
            throw new CodeveoUtilsException("Error while converting object from JSON", e);
        }
    }

    /**
     * Get default object mapper with the following features:
     * <ul>
     * <li>enable {@link DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES}</li>
     * <li>enable {@link DeserializationFeature#READ_UNKNOWN_ENUM_VALUES_AS_NULL}</li>
     * <li>enable {@link DeserializationFeature#READ_ENUMS_USING_TO_STRING}</li>
     * <li>enable {@link DeserializationFeature#USE_BIG_DECIMAL_FOR_FLOATS}</li>
     * <li>disabled {@link SerializationFeature#WRITE_DATES_AS_TIMESTAMPS}</li>
     * <li>inclusion {@link Include#NON_ABSENT}</li>
     * <li>inclusion {@link Include#NON_EMPTY}</li>
     * <li>registered module {@link ParameterNamesModule}</li>
     * <li>registered module {@link Jdk8Module}</li>
     * <li>registered module {@link JavaTimeModule}</li>
     * </ul>
     *
     * @return object mapper
     */
    public static ObjectMapper createDefaultObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
                .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setSerializationInclusion(Include.NON_ABSENT)
                .setSerializationInclusion(Include.NON_EMPTY)
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }
}
