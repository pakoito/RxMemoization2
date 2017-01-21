/*
 * Copyright (c) pakoito 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pacoworks.rxmemoization2;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Function6;
import io.reactivex.functions.Function7;
import io.reactivex.functions.Function8;
import io.reactivex.functions.Function9;

/**
 * Helper class to memoize Functions to enable caching of results for same parameters.
 * <p/>
 * Every function wrapped adds a map check + one if branch for cache hit; or map check + if branch +
 * store value for a cache miss.
 *
 * @author pakoito
 */
public final class RxMemoization {
    private RxMemoization() {
        // No instances
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func0 function to wrap
     * @return function caching results
     */
    public static <R> Callable<R> memoize(final Callable<R> func0) {
        return new Callable<R>() {
            private R value;

            @Override
            public R call() throws Exception {
                if (null == value) {
                    synchronized (this) {
                        if (null == value) {
                            value = func0.call();
                        }
                    }
                }
                return value;
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func1 function to wrap
     * @return function caching results
     */
    public static <A, R> Function<A, R> memoize(final Function<A, R> func1) {
        final Map<A, R> results = new ConcurrentHashMap<A, R>();
        return new Function<A, R>() {
            @Override
            public R apply(A a) throws Exception {
                final R cached = results.get(a);
                if (null == cached) {
                    final R result = func1.apply(a);
                    results.put(a, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func2 function to wrap
     * @return function caching results
     */
    public static <A, B, R> BiFunction<A, B, R> memoize(final BiFunction<A, B, R> func2) {
        final Map<ArgStorage, R> results = new ConcurrentHashMap<ArgStorage, R>();
        return new BiFunction<A, B, R>() {
            @Override
            public R apply(A a, B b) throws Exception {
                final ArgStorage args = new ArgStorage(a, b);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func2.apply(a, b);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func3 function to wrap
     * @return function caching results
     */
    public static <A, B, C, R> Function3<A, B, C, R> memoize(final Function3<A, B, C, R> func3) {
        final Map<ArgStorage, R> results = new ConcurrentHashMap<ArgStorage, R>();
        return new Function3<A, B, C, R>() {
            @Override
            public R apply(A a, B b, C c) throws Exception {
                final ArgStorage args = new ArgStorage(a, b, c);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func3.apply(a, b, c);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func4 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, R> Function4<A, B, C, D, R> memoize(final Function4<A, B, C, D, R> func4) {
        final Map<ArgStorage, R> results = new ConcurrentHashMap<ArgStorage, R>();
        return new Function4<A, B, C, D, R>() {
            @Override
            public R apply(A a, B b, C c, D d) throws Exception {
                final ArgStorage args = new ArgStorage(a, b, c, d);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func4.apply(a, b, c, d);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func5 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, R> Function5<A, B, C, D, E, R> memoize(
            final Function5<A, B, C, D, E, R> func5) {
        final Map<ArgStorage, R> results = new ConcurrentHashMap<ArgStorage, R>();
        return new Function5<A, B, C, D, E, R>() {
            @Override
            public R apply(A a, B b, C c, D d, E e) throws Exception {
                final ArgStorage args = new ArgStorage(a, b, c, d, e);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func5.apply(a, b, c, d, e);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func6 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, F, R> Function6<A, B, C, D, E, F, R> memoize(
            final Function6<A, B, C, D, E, F, R> func6) {
        final Map<ArgStorage, R> results = new ConcurrentHashMap<ArgStorage, R>();
        return new Function6<A, B, C, D, E, F, R>() {
            @Override
            public R apply(A a, B b, C c, D d, E e, F f) throws Exception {
                final ArgStorage args = new ArgStorage(a, b, c, d, e, f);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func6.apply(a, b, c, d, e, f);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func7 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, F, G, R> Function7<A, B, C, D, E, F, G, R> memoize(
            final Function7<A, B, C, D, E, F, G, R> func7) {
        final Map<ArgStorage, R> results = new ConcurrentHashMap<ArgStorage, R>();
        return new Function7<A, B, C, D, E, F, G, R>() {
            @Override
            public R apply(A a, B b, C c, D d, E e, F f, G g) throws Exception {
                final ArgStorage args = new ArgStorage(a, b, c, d, e, f, g);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func7.apply(a, b, c, d, e, f, g);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func8 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, F, G, H, R> Function8<A, B, C, D, E, F, G, H, R> memoize(
            final Function8<A, B, C, D, E, F, G, H, R> func8) {
        final Map<ArgStorage, R> results = new ConcurrentHashMap<ArgStorage, R>();
        return new Function8<A, B, C, D, E, F, G, H, R>() {
            @Override
            public R apply(A a, B b, C c, D d, E e, F f, G g, H h) throws Exception {
                final ArgStorage args = new ArgStorage(a, b, c, d, e, f, g, h);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func8.apply(a, b, c, d, e, f, g, h);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func9 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, F, G, H, I, R> Function9<A, B, C, D, E, F, G, H, I, R> memoize(
            final Function9<A, B, C, D, E, F, G, H, I, R> func9) {
        final Map<ArgStorage, R> results = new ConcurrentHashMap<ArgStorage, R>();
        return new Function9<A, B, C, D, E, F, G, H, I, R>() {
            @Override
            public R apply(A a, B b, C c, D d, E e, F f, G g, H h, I i) throws Exception {
                final ArgStorage args = new ArgStorage(a, b, c, d, e, f, g, h, i);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func9.apply(a, b, c, d, e, f, g, h, i);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    private static final class ArgStorage {
        private final Object[] storage;

        private final int hashCode;

        ArgStorage(Object... storage) {
            this.storage = storage;
            this.hashCode = Arrays.hashCode(this.storage);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            ArgStorage that = (ArgStorage)o;
            return Arrays.equals(storage, that.storage);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
