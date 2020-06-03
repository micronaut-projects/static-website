title: Micronaut AOP: Awesome Flexibility Without the Complexity
date: October 7, 2019  
description: Learn more about Micronaut's AOP system and how easy it is to use compared to competitors
author: Graeme Rocher
---

# [%title]

**By [%author]**

[%date] 

With the recent release of [Micronaut Data 1.0 M3](https://github.com/micronaut-projects/micronaut-data/releases/tag/v1.0.0.M2), I got to work on a couple of features for Micronaut Data that really highlighted to me what we have achieved with [Micronaut's aspect-oriented programming (AOP) API](https://docs.micronaut.io/latest/guide/index.html#aop) and the fantastic simplifications it offers, while at the same time ensuring optimal performance. I thought I would take the time to do a write-up of how Micronaut AOP works and why it is one of my favorite Micronaut features compared to the competitors.

## Introduction to AOP

For those unfamiliar, AOP has a long history in the Java community, with a variety of different implementations, including a custom Java language extension called [AspectJ](https://www.eclipse.org/aspectj/). The basic idea is that in a Java application, you often want to apply cross-cutting logic to a method invocation. The way you apply this cross-cutting logic could be expressed in a custom language like AspectJ; however most developers are exposed to AOP via annotations, where you explicitly apply AOP "advice" to a method.

The most famous example of this in the Java community is probably Spring's `@Transactional` annotation, which allows you to demarcate a method as running within the context of a declared transaction. This is what is known as "Around Advice," where you decorate a method invocation with new behavior that implements a cross-cutting concern.

Around Advice is just one type of AOP Advice supported by Micronaut. The following advice types are supported:

*   **[Around Advice](https://docs.micronaut.io/latest/guide/index.html#aroundAdvice).** As described previously, you decorate an existing method with new behavior.
*   **[Introduction Advice](https://docs.micronaut.io/latest/guide/index.html#introductionAdvice).** Introduction Advice differs, in that it allows you to introduce new behavior to an existing class. A great example of this is, in fact, Micronaut Data (and Spring Data), which allows you to declare an interface that the compiler implements for you by introducing new behavior.
*   **[Adapter Advice](https://docs.micronaut.io/latest/guide/index.html#adapterAdvice).** Adapter Advice is, I believe, unique to Micronaut in that it allows you to introduce a new bean that implements SAM type, an interface with a single abstract method, and delegates to any method definition. This may sound confusing, but shortly I will present a concrete example of this in action.

So why does Micronaut implement its own AOP mechanism, rather than rely on something already out there?

Existing implementations for both Java/Jakarta EE and Spring rely heavily on a mixture of runtime reflection, JDK proxies, and byte code generation with something like CGLib or Bytebuddy (even Quarkus currently only implements reflection-free DI and not AOP).

Micronaut dependency injection is completely reflection free, so it made sense for Micronaut's AOP mechanism to be reflection free as well.

## Micronaut AOP Setup

Micronaut AOP is incredibly simple to use when compared to other implementations out there. It is literally just a compiler feature. There is no need to set up complex ProxyFactoryBean implementations or rely on a runtime container. The minimum set of requirements to get going with Micronaut AOP is to add the Micronaut annotation processors and declare a dependency on micronaut-aop in your build.

The following is the Gradle configuration required:

```groovy {.line-numbers}
dependencies {
     annotationProcessor "io.micronaut:micronaut-inject-java:$micronautVersion"
     compile "io.micronaut:micronaut-aop:$micronautVersion"
}
```

And the equivalent Maven config:

```xml {.line-numbers}
<dependencies>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-aop</artifactId>
      <scope>compile</scope>
      <version>${micronaut.version}</version>
    </dependency>
....
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.7.0</version>
          <configuration>
            <source>${jdk.version}</source>
            <target>${jdk.version}</target>
            <encoding>UTF-8</encoding>
            <compilerArgs>
              <arg>-parameters</arg>
            </compilerArgs>
            <annotationProcessorPaths>
                  <path>
                    <groupId>io.micronaut</groupId>
                    <artifactId>micronaut-inject-java</artifactId>
                    <version>${micronaut.version}</version>
                  </path>
            </annotationProcessorPaths>
```

The Maven configuration requires you declare the micronaut-inject-java dependency in your annotation processors paths from the Maven compiler plugin.

## Micronaut Around Advice Put to Use

So as mentioned at the beginning of the article, I got to use some of the Micronaut AOP features with Micronaut Data, since we wanted to support transaction management, as well as transactional events, without the need to pull in Spring, which adds overhead by introducing an additional 4mb of dependencies, makes extensive use of reflection and runtime proxies, which impact memory consumption, and negatively impacts GraalVM native support.

The first step was to add support for `javax.transaction.Transactional` so you could use the standard Java annotation to declare transaction boundaries. To achieve this, I created a new annotation called [TransactionalAdvice](https://github.com/micronaut-projects/micronaut-data/blob/master/data-tx/src/main/java/io/micronaut/transaction/interceptor/annotation/TransactionalAdvice.java), which I declared as a meta-annotation (an annotation that can be used only on other annotations):

```java {.line-numbers}
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Around
@Type(TransactionalInterceptor.class)
@Internal
public @interface TransactionalAdvice {
    /**
     * Alias for {@link #transactionManager}.
     *
     * @return The transaction manager
     * @see #transactionManager
     */
    String value() default "";
    
    // Remaining members omitted for clarity 

}
```

The `TransactionalAdvice` annotation is itself annotated with [@Around](https://docs.micronaut.io/latest/api/io/micronaut/aop/Around.html) and `@Type(TransactionalInterceptor.class)`, which indicate the interceptor type that will handle the method invocation.

To make it possible to activate the `TransactionAdvice` behavior every time someone uses `javax.transaction.Transactional`, I then added a `AnnotationMapper` that the compiler will use to map the `javax.transaction.Transactional` annotation to the `TransactionAdvice` annotation:

```java {.line-numbers}
public class JtaTransactionalMapper implements NamedAnnotationMapper {
    @NonNull
    @Override
    public String getName() {
        return "javax.transaction.Transactional";
    }

    @Override
    public List<AnnotationValue<?>> map(AnnotationValue<Annotation> annotation, VisitorContext visitorContext) {
       
        AnnotationValueBuilder<Annotation> builder =
                AnnotationValue.builder("io.micronaut.transaction.interceptor.annotation.TransactionalAdvice");

        // Member processing omitted for brevity....

        return Collections.singletonList(
                builder.build()
        );
    }
}
```

The above annotation mapper will be triggered every time a `@Transactional` annotation is declared in code. The map method returns the meta annotation that the annotation maps to.

The motivation to use a meta-annotation and map existing annotations is that you can support any annotation type, including Spring's. Additionally, if one day `javax.transaction` is renamed to `jakarta.transaction`, we just add a new mapper that has zero runtime overhead. It allows Micronaut to completely decouple itself from the source code annotation DSL used.

So what about the [TransactionInterceptor](https://github.com/micronaut-projects/micronaut-data/blob/master/data-tx/src/main/java/io/micronaut/transaction/interceptor/TransactionalInterceptor.java) implementation?

Micronaut AOP defines an interface called [MethodInterceptor](https://docs.micronaut.io/latest/api/io/micronaut/aop/MethodInterceptor.html) that features a single method called `intercept` that all interceptors need to implement. The intercept method receives a reference to the [MethodInvocationContext](https://docs.micronaut.io/latest/api/io/micronaut/aop/MethodInvocationContext.html) that holds a reference to the [ExecutableMethod](https://docs.micronaut.io/latest/api/io/micronaut/inject/ExecutableMethod.html) that you can use to proceed and invoke the original implementation.

The received `ExecutableMethod` is a compile-time-produced handle that allows you to invoke the original method without using reflection. It also contains a reference to the [AnnotationMetadata](https://docs.micronaut.io/latest/api/io/micronaut/core/annotation/AnnotationMetadata.html), which allows you to inspect and retrieve annotation values and stereotypes without using reflection. The benefits of this include massively reduced stack trace sizes, improved performance, and reduced memory consumption.

The following is the implementation taken from the `TransactionInterceptor`:

```php {.line-numbers} {highlight=4,13,19,27}
public Object intercept(MethodInvocationContext<Object, Object> context) {
    final TransactionInvocation transactionInvocation = transactionInvocationMap
            .computeIfAbsent(context.getExecutableMethod(), executableMethod -> {
        final String qualifier = executableMethod.stringValue(TransactionalAdvice.class).orElse(null);
        SynchronousTransactionManager transactionManager =
                beanLocator.getBean(SynchronousTransactionManager.class, qualifier != null ? Qualifiers.byName(qualifier) : null);
        final TransactionAttribute transactionAttribute = resolveTransactionDefinition(executableMethod);

        return new TransactionInvocation(transactionManager, transactionAttribute);
    });
    final TransactionAttribute definition = transactionInvocation.definition;
    final SynchronousTransactionManager transactionManager = transactionInvocation.transactionManager;
    final TransactionInfo transactionInfo = createTransactionIfNecessary(
            transactionManager,
            definition,
            definition.getName());
    Object retVal;
    try {
        retVal = context.proceed();
    } catch (Throwable ex) {
        completeTransactionAfterThrowing(transactionInfo, ex);
        throw ex;
    } finally {
        cleanupTransactionInfo(transactionInfo);
    }
    commitTransactionAfterReturning(transactionInfo);
    return retVal;
}
```

The important aspects start on line 4, where the annotation metadata is inspected to figure out which transaction manager to look up to apply transaction management.

On line 13, a transaction is created. We then proceed within the method invocation on line 19 and finally return the value on line 27\. The code here is largely based on the equivalent code in Spring's `TransactionAspectSupport`, but without needing all of the runtime proxy and reflection complexity.

And with that, whenever you declare `@Transactional` in your code, you get automatic transaction management. Nothing else to configure. How simple is that?

## Micronaut Introduction Advice Put to Use

Another feature requested by Micronaut users is the ability get a reference to a `java.sql.Connection` that is aware of the currently executing transaction. The solution for this in Spring is [TransactionAwareDataSourceProxy](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/datasource/TransactionAwareDataSourceProxy.html), which proxies your entire `java.sql.DataSource`, making sure whenever you retrieve a connection, it is associated with the current transaction. But this adds the overhead of runtime reflection and proxying to all methods of the `DataSource` interface.

To support this use case without proxying the `DataSource`, with Micronaut Data we decided to allow users to inject a transaction-aware `java.sql.Connection` in a similar way you can inject a transaction-aware `EntityManager` in JPA that uses Hibernate's `getCurrentSession()` feature.

This turned out to be really simple with Micronaut AOP. The first step was to define an interface that extended `java.sql.Connection`, which I called [TransactionalConnection](https://github.com/micronaut-projects/micronaut-data/blob/master/data-tx/src/main/java/io/micronaut/transaction/jdbc/TransactionalConnection.java):

```java {.line-numbers} {highlight=7,8}
package io.micronaut.transaction.jdbc;

import io.micronaut.context.annotation.EachBean;
import javax.sql.DataSource;
import java.sql.Connection;

@EachBean(DataSource.class)
@ConnectionAdvice
public interface TransactionalConnection extends Connection {
}
```

The interface is annotated on line 7 with another of my favorite Micronaut features, the [@EachBean](https://docs.micronaut.io/latest/api/io/micronaut/context/annotation/EachBean.html) annotation, which says that for every `DataSource` bean configured in the application, create an associated `TransactionalConnection` bean. This effectively means if you have multiple data sources, you get a `TransactionalConnection` bean configured for each one automatically and use a `javax.inject.Named` qualifier to inject the one you want.

The Introduction Advice is then defined on line 8 using a new annotation called `@ConnectionAdvice`, which is a package private internal annotation and looks like the following:

```php {.line-numbers} {highlight=2,3}
@Retention(RUNTIME)
@Introduction
@Type(ConnectionInterceptor.class)
@Internal
@interface ConnectionAdvice {
}
```

On line 3, we define the annotation as [@Introduction](https://docs.micronaut.io/latest/api/io/micronaut/aop/Introduction.html) Advice, which means it can implement abstract behavior.

On line 4, we define the interceptor that is going to provide the implementation at runtime, which in this case is [ConnectionInterceptor](https://github.com/micronaut-projects/micronaut-data/blob/master/data-tx/src/main/java/io/micronaut/transaction/jdbc/ConnectionInterceptor.java), which is shown below and is really simple:

```php {.line-numbers} {highlight=6,12,14,16}
public final class ConnectionInterceptor implements MethodInterceptor<Connection, Object> {

    private final DataSource dataSource;

    ConnectionInterceptor(BeanContext beanContext, Qualifier<DataSource> qualifier) {
        this.dataSource = beanContext.getBean(DataSource.class, qualifier);
    }

    public Object intercept(MethodInvocationContext<Connection, Object> context) {
        Connection connection;
        try {
            connection = DataSourceUtils.getConnection(dataSource, false);
        } catch (CannotGetJdbcConnectionException e) {
            throw new NoTransactionException("No current transaction present. Consider declaring @Transactional on the surrounding method", e);
        }
        return context.getExecutableMethod().invoke(connection, context.getParameterValues());
    }
}
```

On line 6 we used the injected qualifier for the interceptor instance to look up the associated `DataSource`. Then on line 12 we look up the connection associated with the current transaction. If no connection is present, we throw `NoTransactionException` on line 14\. Otherwise we proceed and invoke the method on the connection on line 16.

With that in place, all a user has to do is inject a Connection instance and use it directly, rather than having to manually look up the connection associated with the current transaction, which simplifies the code quite nicely:

```java {.line-numbers}
@Inject
Connection connection;

@Transactional
void insertWithTransaction() throws Exception {
    try (PreparedStatement ps = connection
            .prepareStatement("insert into book (pages, title) values(100, 'The Stand')")) {
        ps.execute();
    }
}
```

The underlying connection is managed by the transaction and cleaned up and closed after the transaction commits, so there is no need to close it manually. If one were to remove the `@Transactional` definition, a `NoTransactionException` would occur.

## Micronaut Adapter Advice Put to Use

Both Micronaut and Spring allow the publication of events that can be consumed by either synchronous or asynchronous event listeners.

One missing feature from Micronaut, which users have been asking for a lot, is the ability to define transactional event listeners. That is, event listeners that are only triggered when a particular transaction phase completes (typically users are most interested in triggering behavior after a successful commit). The use cases here are things like sending an email or publishing a Kafka message only if a transaction commits successfully.

Making this work in Micronaut was actually remarkably simple thanks to [Adapter advice](https://docs.micronaut.io/latest/guide/index.html#adapterAdvice).

The first step was defining a [@TransactionalEventListener](https://github.com/micronaut-projects/micronaut-data/blob/master/data-tx/src/main/java/io/micronaut/transaction/annotation/TransactionalEventListener.java) annotation:

```java {.line-numbers} {highlight=4,5}
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Adapter(ApplicationEventListener.class) 
@TransactionalEventAdvice
public @interface TransactionalEventListener {
    TransactionPhase value() default TransactionPhase.AFTER_COMMIT;
}
```

The first important part is the definition in the [@Adapter](https://docs.micronaut.io/latest/api/io/micronaut/aop/Adapter.html) annotation on line 4, which takes the interface we want to adapt, in this case [ApplicationEventListener](https://docs.micronaut.io/latest/api/io/micronaut/context/event/ApplicationEventListener.html).

Interfaces passed to the [@Adapter](https://docs.micronaut.io/latest/api/io/micronaut/aop/Adapter.html) annotation have to contain a Single Abstract Method (often called SAM types). At compilation time, Micronaut will create an additional bean that implements the interface and delegates to the method that declares the annotation. So in this case a new `ApplicationEventListener` bean will be created that invokes the method that [@TransactionalEventListener](https://github.com/micronaut-projects/micronaut-data/blob/master/data-tx/src/main/java/io/micronaut/transaction/annotation/TransactionalEventListener.java) is declared on.

The nice thing is that if you try to declare `@TransactionalEventListener` on a method that doesn't conform to the signature of the `ApplicationEventListener` interface, you will get a compilation error. This is part of Micronaut's nice ability to compile time check what you are doing with the framework is correct.

In addition to the Adapter advice, the `@TransactionalEventListener` annotation also declares an `@Around` advice implementation on line 5 called `@TransactionalEventAdvice`, which looks like:

```java {.line-numbers} {highlight=1,3}
@Around
@Type(TransactionalEventInterceptor.class)
@Internal
public @interface TransactionalEventAdvice {
}
```

As you can see, on line 1, the `@TransactionEventAdvice` is declared as `@Around` advice (which remember allows decorating existing behavior), and on line 3, the type of advice is defined as `TransactionalEventInterceptor`.

The implementation of the `TransactionalEventInterceptor` is also really simple and intercepts the invocation of the event listener:

```java {.line-numbers} {highlight=2,5,7,21,26,31,12,38}
public Object intercept(MethodInvocationContext<Object, Object> context) {
    final TransactionalEventListener.TransactionPhase phase = context
            .enumValue(TransactionalEventListener.class, TransactionalEventListener.TransactionPhase.class)
            .orElse(TransactionalEventListener.TransactionPhase.AFTER_COMMIT);
    if (TransactionSynchronizationManager.isSynchronizationActive() &&
            TransactionSynchronizationManager.isActualTransactionActive()) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

            @Override
            public void beforeCommit(boolean readOnly) {
                if (phase == TransactionalEventListener.TransactionPhase.BEFORE_COMMIT) {
                    context.proceed();
                }
            }

            @Override
            public void afterCompletion(@NonNull Status status) {
                switch (status) {
                    case ROLLED_BACK:
                        if (phase == TransactionalEventListener.TransactionPhase.AFTER_ROLLBACK) {
                            context.proceed();
                        }
                    break;
                    case COMMITTED:
                        if (phase == TransactionalEventListener.TransactionPhase.AFTER_COMMIT) {
                            context.proceed();
                        }
                    break;
                    default:
                        if (phase == TransactionalEventListener.TransactionPhase.AFTER_COMPLETION) {
                            context.proceed();
                        }
                }
            }
        });
    } else {
        if (LOG.isDebugEnabled()) {
            LOG.debug("No active transaction, skipping event {}", context.getParameterValues()[0]);
        }
    }
    return null;
}
```

On line 2, the transaction phase the event applies to is looked up from the annotation metadata. Then on line 5, the interceptors checks if there is an active transaction. If there is no active transaction, the event listener is skipped on line 38\. Otherwise on line 7, a new transaction synchronization is registered. Depending on the phase, the listener is only invoked via the proceed method when the appropriate transaction event occurs. For example, on transaction commit, the method will be invoked on line 26.

And with that we have working transactional events!

The following shows an example in action:

```java {.line-numbers} {highlight=6,11,18}
@Singleton
public class BookManager {
    private final BookRepository bookRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BookManager(BookRepository bookRepository, ApplicationEventPublisher eventPublisher) {
        this.bookRepository = bookRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    void saveBook(String title, int pages) {
        final Book book = new Book(title, pages);
        bookRepository.save(book);
        eventPublisher.publishEvent(new NewBookEvent(book)); 
    }

    @TransactionalEventListener
    void onNewBook(NewBookEvent event) {
        System.out.println("book = " + event.book);
    }

}
```

First, on line 6, the constructor of the class gets a reference to the [ApplicationEventPublisher](https://docs.micronaut.io/latest/api/index.html). The method on line 11 is declared as transactional, saving an object and then publishing the event.

The method on line 17 is declared with [@TransactionalEventListener](https://github.com/micronaut-projects/micronaut-data/blob/master/data-tx/src/main/java/io/micronaut/transaction/annotation/TransactionalEventListener.java) and for the moment just prints out the event. If the transaction fails for whatever reason, however, the event will never be received and the output never printed.

## Summary

There is a definite lack of information on how compelling the Micronaut AOP implementation is, and I hope this blog post goes some way to resolving that. Micronaut AOP has some really nice features and makes it really simple to define AOP advice without the need to involve complex runtime bytecode generation solutions that create enormous stack traces and place additional memory strain on your application.

My recent experience using Micronaut AOP to build Micronaut Data further highlighted to me the advances we have made in this area, pushing developers to avoid the use of reflection, which optimizes memory consumption and makes it so much easier to go native with GraalVM substrate.