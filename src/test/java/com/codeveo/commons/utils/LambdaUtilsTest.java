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

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static com.codeveo.commons.utils.LambdaUtils.consumerThrowable;
import static com.codeveo.commons.utils.LambdaUtils.functionThrowable;
import static com.codeveo.commons.utils.LambdaUtils.predicateThrowable;
import static com.codeveo.commons.utils.LambdaUtils.supplierThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LambdaUtilsTest {

    @Test
    void testConsumerThrowable() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> Optional.of("test")
                .ifPresent(consumerThrowable(arg -> {
                    throw new CheckedTestException(arg);
                })));

        assertEquals(CheckedTestException.class, thrown.getCause().getClass());
        assertEquals("test", thrown.getCause().getMessage());
    }

    @Test
    void testFunctionThrowable() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> Optional.of("test")
                .map(functionThrowable(arg -> {
                    throw new CheckedTestException(arg);
                }))
                .orElseThrow(() -> new IllegalStateException("testFailed")));

        assertEquals(CheckedTestException.class, thrown.getCause().getClass());
        assertEquals("test", thrown.getCause().getMessage());
    }

    @Test
    void testPredicateThrowable() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> Optional.of("test")
                .filter(predicateThrowable(arg -> {
                    throw new CheckedTestException(arg);
                }))
                .orElseThrow(() -> new IllegalStateException("testFailed")));

        assertEquals(CheckedTestException.class, thrown.getCause().getClass());
        assertEquals("test", thrown.getCause().getMessage());
    }

    @Test
    void testSupplierThrowable() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> Optional.empty()
                .orElseGet(supplierThrowable(() -> {
                    throw new CheckedTestException("test");
                })));

        assertEquals(CheckedTestException.class, thrown.getCause().getClass());
        assertEquals("test", thrown.getCause().getMessage());
    }

    private static class CheckedTestException extends Exception {
        public CheckedTestException(String message) {
            super(message);
        }
    }
}