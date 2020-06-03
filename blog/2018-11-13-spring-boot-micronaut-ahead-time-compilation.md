title: Spring Boot to Micronaut with Ahead-of-Time Compilation
date: November 13, 2018  
description: Demonstrates how to turn a Spring Boot application into a Micronaut application at compilation time and run the application on GraalVM
author: Graeme Rocher
---

# [%title]

**By [%author]**

[%date] 

Along with the release of [Micronaut 1.0.1](https://github.com/micronaut-projects/micronaut-core/releases/tag/v1.0.1), the [Micronaut team](https://objectcomputing.com/products/2gm-team) at [Object Computing, Inc.](https://objectcomputing.com/) (OCI) is excited to announce the release of [Micronaut for Spring 1.0 M1](https://github.com/micronaut-projects/micronaut-spring).

Micronaut for Spring adds the ability to use the long-established Spring annotation-based programming model to build Micronaut applications and libraries that work with both Micronaut and Spring.

The provided [example application](https://github.com/micronaut-projects/micronaut-spring/tree/master/examples/greeting-service) is at the source level a Spring Boot application. Using ahead-of-time (AOT) compilation, Micronaut is able to compute and interpret the Spring annotation programming model and produce a valid Micronaut application without adding any runtime overhead. The Micronaut application is then executable on GraalVM:

[![Micronaut for Spring](./2018-11-13-img01.gif)](https://youtu.be/JvzD2SEw0-E "Micronaut for Spring")

The way this works is fairly simple.

Micronaut has a set of annotations that are used at runtime to implement the framework. Spring has another set that map pretty simply onto Micronaut's annotations. You could consider these annotations as a source-code level Domain Specific Language (DSL).

At compilation time, the annotation metadata is interpreted and the Spring annotation mapped to the equivalent Micronaut annotation metadata, and just like that, Micronaut can run a Spring application.

Note that only a subset of Spring is supported, but it's enough to build real applications and libraries that work whether they're included in a Spring Boot application or a Micronaut application.

## What's In It for Micronaut Developers?

Although including Spring in the application dependencies has some downsides for Micronaut developers (notably a JAR size increase from 13MB to 29MB), using the Spring annotation programming model also has some interesting upsides, including:

*   **Tooling Support.** If you import the application into a Spring-aware IDE, such as IntelliJ IDEA or STS 4.0, the Spring features "just work." This makes sense because IDEs operate on the source code, so as far as IDEs are concerned, the application is a Spring application, even if at runtime the application is in fact a Micronaut application. The same would be the case for any source-code level tooling in the Spring ecosystem.
*   **Spring and Grails Compatibility.** By using the Spring annotation programming model, it is possible to build auto-configurations, endpoints, controllers, and libraries that work with Spring Boot, Grails, and Micronaut. 
*   **Easier Migration to Micronaut.** While not every feature of Spring is supported, the vast majority of the important aspects are. This makes it easier to train new developers, migrate existing code, and embrace Micronaut.

Note that if your source code only references Spring annotations and not Spring interfaces, Spring can actually be a "compile-only" dependency, which would shrink the JAR size back to 13MB and bring all of the benefits mentioned above.

## What's In It for Spring Developers?

For Spring developers, the benefits are numerous too.

Micronaut has the ability to take the Spring programming model places it was never able to go before, due to performance or memory consumption constraints:

*   **Internet of Things (IoT).** Micronaut runs great in IoT scenarios, including on the baby Raspberry Pi. This is because the memory profile of a Micronaut application has nothing to do with a Spring or Jakarta EE application, with significant savings to be had.
*   **GraalVM.** Micronaut is the first framework to bring the true Spring programming model to GraalVM in any kind of realistic way. You can run what is essentially a Spring Boot application at the source code level on GraalVM today and implement lightning-fast functions and low-memory footprint microservices.
*   **Android.** I remember during the early days of my time at SpringSource, the ambition to get the Spring programming model onto Android. It never actually happened; only small Spring components like RestTemplate ever make it to Android. The Micronaut core container is [already running on Android](https://docs.micronaut.io/latest/guide/index.html#android) and has the potential to bring the entire Spring programming model to Android. We have significant ambition for Micronaut on Android.
*   **AOT Compilation.** Micronaut can be thought of as a framework for AOT, with a complete API for performing many AOT tasks across language implementations. For example, the [referenced example](https://github.com/micronaut-projects/micronaut-spring/tree/master/examples/greeting-service) application is able to compute the Swagger API metadata at compile time thanks to Micronaut, even though no Micronaut API is actually referenced.
*   **Serverless.** With Micronaut's faster cold startup and lower memory costs, it just got a whole lot easier to build efficient applications that use the Spring programming model.
*   **Library Compatibility.** Projects like JHipster and Spring Boot Admin can now include Micronaut for Spring as a compilation-time annotation processor and be made to work with both Spring Boot and Micronaut. This is huge news for the library ecosystem.
*   **Micronaut Features in Spring.** Since annotation mapping works in any class, you get a [Spring annotation-based compile-time declarative HTTP client](https://github.com/micronaut-projects/micronaut-spring/blob/master/examples/greeting-service/src/test/java/greeting/example/GreetingClient.java) and can use any other Micronaut feature out of the box.

As mentioned, with Micronaut for Spring, it is also now technically possible to write Spring Boot auto configurations that work across Spring Boot, Micronaut, and Grails. The Spring team at Pivotal could even take this library and make much of **spring-boot-autoconfigure** work with either Spring Boot or Micronaut.

The way this would work is that Spring Boot computes the auto-configuration at runtime, while if the compilation-time metadata is there, Micronaut loads it automatically without requiring additional runtime computation.

Since Micronaut can also be used as a parent application context for Spring Boot, the core of Spring Boot could even be updated to use Micronaut for internal wiring and bring GraalVM compatibility to Spring Boot. If the Pivotal developers are interested [we are happy to chat](https://gitter.im/micronautfw).

## What's In It for Grails Developers?

The primary reason we developed Micronaut for Spring is, in fact, for Grails 4.0.

I have begun working on Grails 4.0, and as part of the planning, we want to enable Grails developers to benefit from the investment we have made in Micronaut over the past year.

In Grails 4.0, Micronaut will take over as the parent context for Grails applications, and much of the internal wiring of Grails will be based on Micronaut instead of Spring, so that we can reduce memory consumption and improve startup time. This, combined with the improvements the Spring team has already made in Spring Boot 2.1, will result in significant improvements to Grails 4.x applications in terms of memory consumption and startup time.

Since Micronaut will be the parent context for Grails 4.0 applications, it also means that every feature we develop for Micronaut will be usable in a Grails 4.0 application, from the compile-time clients – like the [HTTP client](https://docs.micronaut.io/latest/guide/index.html#clientAnnotation) and [Kafka client](https://docs.micronaut.io/latest/guide/index.html#kafkaClient) – to features like service discovery and client-side load balancing.

## What About MicroProfile?

I had the pleasure to chat with some folks involved in [MicroProfile](https://microprofile.io "MicroProfile"), and while the ambition of the project is interesting, any model that is based on the runtime analysis of annotations via reflection is unfortunately going to suffer from memory consumption issues, hence my doubts regarding current implementations. 

Having said that and having read the specification, Micronaut's ability to support any annotation set at compilation time using AOT means that Micronaut could technically support MicroProfile using the same approach as Micronaut for Spring.

In other words, there is no reason a JAX-RS / CDI-based implementation of MicroProfile could not be implemented by mapping the annotations and providing a few interface bridges. If the MicroProfile folks are interested in chatting with us about that, [we are around and available to chat](https://gitter.im/micronautfw).

## Summary

When I first introduced Micronaut at Greach earlier this year, I mentioned that Micronaut is far more than just another HTTP server implementation (a new HTTP server written in Java seems to pop up on Github every week!). Micronaut has the potential to revolutionize how applications are built for the JVM by completely changing the runtime characteristics of a typical Spring and/or Jakarta EE application, while retaining largely the same feature set that developers know and love.

By making AOT accessible to JVM users and bringing compatibility to AOT across languages (Java, Kotlin, and Groovy currently), Micronaut is able to decouple the source code from the runtime environment like no other framework has done before. That, ladies and gentlemen, is [The Power of Ahead of Time Compilation](https://objectcomputing.com/news/2018/09/30/micronaut-1-rc1).

If you want to hear more, I will deliver a [complimentary webinar](https://objectcomputing.com/resources/events/webinars/introduction-to-micronaut "Complimentary Webinar") on Micronaut on Wednesday, November 14, 2018, and I will be speaking about Micronaut at [Devoxx Belgium](https://dvbe18.confinabox.com/talk/BZV-3566/Introduction_to_Micronaut:_Lightweight_Microservices_with_Ahead_of_Time_Compilation) on Thursday, November 15, 2018\. See you there!
