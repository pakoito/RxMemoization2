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

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Function6;
import io.reactivex.functions.Function7;
import io.reactivex.functions.Function8;
import io.reactivex.functions.Function9;
import io.reactivex.schedulers.Schedulers;

public class RxMemoizationTest {
    private static final MyObject INSTANCE = new MyObject();

    private static final List<MyObject> INSTANCES = Observable.range(0, 1000)
            .map(new Function<Integer, MyObject>() {
                @Override
                public MyObject apply(Integer integer) {
                    return new MyObject(integer);
                }
            }).toList().blockingGet();

    @Test
    public void testMemoize0() {
        final AtomicInteger count = new AtomicInteger(0);
        final Callable<MyObject> memoized = RxMemoization.memoize(new Callable<MyObject>() {
            @Override
            public MyObject call() {
                count.incrementAndGet();
                return INSTANCE;
            }
        });
        /* Extra tests on memoize for func0 to check the double locking implementation is solid */
        final int threadCount = 10000;
        final int maxDelay = 100;
        Iterable<Observable<Long>> observableList = Observable.range(0, threadCount)
                .map(new Function<Integer, Observable<Long>>() {
                    @Override
                    public Observable<Long> apply(final Integer integer) {
                        return Observable
                                .timer((long)(Math.random() * maxDelay), TimeUnit.MILLISECONDS)
                                .doOnNext(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        memoized.call();
                                        memoized.call();
                                        memoized.call();
                                        memoized.call();
                                        memoized.call();
                                    }
                                }).doOnComplete(new Action() {
                                    @Override
                                    public void run() {
                                        System.out.println("Test-" + integer);
                                    }
                                }).subscribeOn(Schedulers.newThread());
                    }
                }).toList().blockingGet();
        Observable.merge(observableList).blockingForEach(new Consumer<Long>() {
            @Override
            public void accept(Long o) {
            }
        });
        Assert.assertEquals(1, count.get());
    }

    @Test
    public void testMemoize1() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        Function<Integer, MyObject> memoized = RxMemoization.memoize(new Function<Integer, MyObject>() {
            @Override
            public MyObject apply(Integer integer) {
                count.incrementAndGet();
                return INSTANCES.get(integer);
            }
        });
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(1));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(30));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(30));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(30));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize2() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        BiFunction<Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new BiFunction<Integer, Integer, MyObject>() {
                    @Override
                    public MyObject apply(Integer integer, Integer integer2) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(15, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(15, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(15, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize3() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        Function3<Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Function3<Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject apply(Integer integer, Integer integer2, Integer integer3) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize4() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        Function4<Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Function4<Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject apply(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize5() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        Function5<Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Function5<Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject apply(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize6() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        Function6<Integer, Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Function6<Integer, Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject apply(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5, Integer integer6) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5
                                + integer6);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize7() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        Function7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Function7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject apply(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5, Integer integer6, Integer integer7) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5
                                + integer6 + integer7);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize8() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        Function8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Function8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject apply(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5, Integer integer6, Integer integer7,
                            Integer integer8) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5
                                + integer6 + integer7 + integer8);
                    }
                });
        // +1
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0));
        // +1
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 0, 0, 1));
        // +1
        Assert.assertEquals(INSTANCES.get(45), memoized.apply(15, 0, 15, 0, 0, 0, 0, 15));
        // +1
        Assert.assertEquals(INSTANCES.get(45), memoized.apply(0, 15, 15, 0, 0, 0, 0, 15));
        // +1
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 0, 15, 0, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(5, count.get());
    }

    @Test
    public void testMemoize9() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        Function9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Function9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject apply(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5, Integer integer6, Integer integer7,
                            Integer integer8, Integer integer9) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5
                                + integer6 + integer7 + integer8 + integer9);
                    }
                });
        // +1
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0, 0));
        // +1
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0, 1));
        // +1
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(15, 0, 0, 0, 0, 0, 0, 0, 15));
        // +1
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 15, 0, 0, 0, 0, 0, 0, 15));
        // +1
        Assert.assertEquals(INSTANCES.get(30), memoized.apply(0, 0, 15, 0, 0, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.apply(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(5, count.get());
    }

    private static final class MyObject {
        private final int number;

        public MyObject() {
            number = Integer.MIN_VALUE;
        }

        public MyObject(int number) {
            this.number = number;
        }
    }
}
