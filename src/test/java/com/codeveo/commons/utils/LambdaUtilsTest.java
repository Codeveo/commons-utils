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

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
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
        Consumer<Integer> consumer = consumerThrowable(arg -> {
            throw new CheckedTestException("test");
        });

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> consumer.accept(1));

        assertEquals(CheckedTestException.class, thrown.getCause().getClass());
        assertEquals("test", thrown.getCause().getMessage());
    }

    @Test
    void testFunctionThrowable() {
        Function<Integer, Integer> function = functionThrowable(arg -> {
            throw new CheckedTestException("test");
        });

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> function.apply(1));

        assertEquals(CheckedTestException.class, thrown.getCause().getClass());
        assertEquals("test", thrown.getCause().getMessage());
    }

    @Test
    void testBiFunctionThrowable() {
        BiFunction<Integer, Integer, Integer> biFunction = LambdaUtils.biFunctionThrowable((x1, x2) -> {
            throw new CheckedTestException("test");
        });

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> biFunction.apply(1, 2));

        assertEquals(CheckedTestException.class, thrown.getCause().getClass());
        assertEquals("test", thrown.getCause().getMessage());
    }

    @Test
    void testPredicateThrowable() {
        Predicate<Integer> predicate = predicateThrowable(arg -> {
            throw new CheckedTestException("test");
        });

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> predicate.test(1));

        assertEquals(CheckedTestException.class, thrown.getCause().getClass());
        assertEquals("test", thrown.getCause().getMessage());
    }

    @Test
    void testSupplierThrowable() {
        Supplier<Integer> supplier = supplierThrowable(() -> {
            throw new CheckedTestException("test");
        });

        RuntimeException thrown = assertThrows(RuntimeException.class, supplier::get);

        assertEquals(CheckedTestException.class, thrown.getCause().getClass());
        assertEquals("test", thrown.getCause().getMessage());
    }

    private static class CheckedTestException extends Exception {
        public CheckedTestException(String message) {
            super(message);
        }
    }
}