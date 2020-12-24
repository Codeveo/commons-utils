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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class LambdaUtils {

    private LambdaUtils() {
    }

    @FunctionalInterface
    public interface ConsumerThrowable<ParameterT, ExceptionT extends Exception> extends Consumer<ParameterT> {

        @Override
        default void accept(final ParameterT parameter) {
            try {
                applyThrows(parameter);
            } catch (final Exception exception) {
                throw new CodeveoUtilsException(exception);
            }
        }

        void applyThrows(ParameterT parameter) throws ExceptionT;
    }


    @FunctionalInterface
    public interface FunctionThrowable<ParameterT, ResultT, ExceptionT extends Exception>
            extends Function<ParameterT, ResultT> {

        @Override
        default ResultT apply(final ParameterT parameter) {
            try {
                return applyThrows(parameter);
            } catch (final Exception exception) {
                throw new CodeveoUtilsException(exception);
            }
        }

        ResultT applyThrows(ParameterT parameter) throws ExceptionT;
    }


    @FunctionalInterface
    public interface PredicateThrowable<ParameterT, ExceptionT extends Exception> extends Predicate<ParameterT> {

        @Override
        default boolean test(final ParameterT parameter) {
            try {
                return applyThrows(parameter);
            } catch (final Exception exception) {
                throw new CodeveoUtilsException(exception);
            }
        }

        boolean applyThrows(ParameterT parameter) throws ExceptionT;
    }


    @FunctionalInterface
    public interface SupplierThrowable<ReturnT, ExceptionT extends Exception> extends Supplier<ReturnT> {

        @Override
        default ReturnT get() {
            try {
                return getThrows();
            } catch (final Exception exception) {
                throw new CodeveoUtilsException(exception);
            }
        }

        ReturnT getThrows() throws ExceptionT;
    }

    public static <ConsumableT, ExceptionT extends Exception> Consumer<ConsumableT> consumerThrowable(
            final ConsumerThrowable<ConsumableT, ExceptionT> consumer) {
        return consumer;
    }

    public static <ParameterT, ResultT, ExceptionT extends Exception> Function<ParameterT, ResultT> functionThrowable(
            final FunctionThrowable<ParameterT, ResultT, ExceptionT> function) {
        return function;
    }

    public static <ParameterT, ExceptionT extends Exception> Predicate<ParameterT> predicateThrowable(
            final PredicateThrowable<ParameterT, ExceptionT> predicate) {
        return predicate;
    }

    public static <ReturnT, ExceptionT extends Exception> Supplier<ReturnT> supplierThrowable(
            final SupplierThrowable<ReturnT, ExceptionT> supplier) {
        return supplier;
    }
}
