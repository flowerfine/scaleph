/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.common.exception;

import java.util.function.Function;
import java.util.function.Supplier;

public enum Rethrower {
    ;

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwAs(Throwable t) throws T {
        throw (T) t;
    }

    public static void toIllegalArgument(final Procedure voidCallable) {
        castCheckedToRuntime(voidCallable, IllegalArgumentException::new);
    }

    private static void castCheckedToRuntime(final Procedure voidCallable,
                                             final Function<Exception, RuntimeException> exceptionFactory) {
        try {
            voidCallable.call();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw exceptionFactory.apply(e);
        }
    }

    public static void checkArgument(boolean expression, Supplier<Object> errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage.get().toString());
        }
    }

    public static <T extends Object> T checkNotNull(T reference, Supplier<Object> errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage.get().toString());
        }
        return reference;
    }

    @FunctionalInterface
    public interface Procedure {
        void call() throws Exception;
    }
}
