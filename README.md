# RxMemoization2

RxMemoization2 is a library to allow [memoization](https://en.wikipedia.org/wiki/Memoization) on RxJava function interfaces.

For the RxJava 1.X version please go to [RxMemoization](https://github.com/pakoito/RxMemoization).

## Rationale

Memoization stores the results of a function for the same set of parameters, which makes it useful for caching heavy computations that are called often with the same values.

Storage scales linearly inside a `ConcurrentHashMap`, and it gets garbage collected when the function object goes out of scope. Caching is thread safe but a calculation can very rarely happen twice due to [`ConcurrentHashMap`](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ConcurrentHashMap.html) limitations.

## Usage

`RxMemoization` contains one class with a set of `memoize()` methods to do memoization for any FunctionN from `Callable`, `Function`, `BiFunction`, and `Function3` up to `Function9`.

```java
Function<String, Integer> parser = 
        RxMemoization.memoize((String s) ->
                                {   System.out.println(s);
                                    return Integer.parseInt(s); });

// All the results for the same parameter correspond to the same object

parser.call("0"); // Integer@65E46F
// Log: "0"
parser.call("0"); // Integer@65E46F
parser.call("0"); // Integer@65E46F
parser.call("1"); // Integer@335A8B
// Log: "1"
parser.call("4"); // Integer@564E21
// Log: "4"
parser.call("1"); // Integer@335A8B
parser.call("4"); // Integer@564E21
parser.call("0"); // Integer@65E46F
parser.call("2"); // Integer@564E21
// Log: "2"
```

## Distribution

Add as a dependency to your `build.gradle`
```groovy
repositories {
    ...
    maven { url "https://jitpack.io" }
    ...
}
    
dependencies {
    ...
    compile 'com.github.pakoito:RxMemoization2:1.0.0'
    ...
}
```
or to your `pom.xml`

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.pakoito</groupId>
    <artifactId>RxMemoization2</artifactId>
    <version>1.0.0</version>
</dependency>
```

## License

Copyright (c) pakoito 2017

The Apache Software License, Version 2.0

See LICENSE.md
