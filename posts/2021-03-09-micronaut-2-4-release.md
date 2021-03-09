---
title: Micronaut 2.4 Released!
date:  March 9, 2021 
description: We're pleased to announce the release of Micronaut 2.4. 
author: James Kleeh
image: 2021-03-09.jpg
---

# [%title]

[%author]

[%date] 

Tags: #release #micronaut2

The Micronaut team is excited to announce the release of Micronaut 2.4! This release features support for Jakarta inject annotations, improvements for AOP interceptor declarations, easier control over error responses, Micronaut Data improvements, and more!

## Jakarta Inject

The Micronaut framework now supports using 'jakarta.inject' annotations as an alternative to 'javax.inject'. The javax annotations still come with the Micronaut framework by default, so in order to use the Jakarta annotations a dependency will need to be added to the build for the 'jakarta.inject-api'. 

Future versions of the Micronaut framework will stop including javax annotations by default, and the Jakarta annotations will be included by default instead. Both annotations will continue to be supported, however.

## Nullability Annotations

With Micronaut 2.4, we've introduced our own nullability annotations. Due to several problems with findbugs and spotbugs, no external dependency with nullable annotations will ship with future versions of the Micronaut framework. We recommend using our annotations or adding an explicit dependency on a library of your choosing for '@NonNull' and '@Nullable' annotations.

## Interceptor Bindings

In previous versions of the Micronaut framework, an AOP annotation was responsible for declaring which interceptor should apply to it. That style of declaration is still possible with 2.4; however it is now deprecated. The new way is to allow the interceptor to define which annotations it should apply to. This has the benefit of being able to apply to multiple annotations and to annotations which may not be under your control to add the Micronaut specific AOP advice to. See the documentation on [@InterceptorBean](https://docs.micronaut.io/2.4.0/guide/index.html#aop) for more information.

## Error Responses

Micronaut 2.4 makes it much easier to control responses for errors. Currently all errors are rendered as 'vnd.error' by default. If you want to use [JSON API](https://jsonapi.org/format/#errors) or [Problem](https://tools.ietf.org/html/rfc7807), it is now much easier to implement that support. Future versions of the Micronaut framework may add integrations for those formats or others. 

A common interface [ErrorResponseProcessor](https://docs.micronaut.io/2.4.0/api/io/micronaut/http/server/exceptions/response/ErrorResponseProcessor.html) is now responsible for creating response bodies for all provided exception handlers. See the documentation on [error handling](https://docs.micronaut.io/2.4.0/guide/index.html#errorHandling) for all of the details.

## Micronaut Data Improvements

Micronaut Data now supports JDK 14+ records and persistence event methods on entities.

## Oracle Coherence CE

Micronaut 2.4 comes with the first milestone release of Micronaut integration with Oracle Coherence Community Edition, which makes implementation of Micronaut applications with a Coherence back end a breeze. This integration comes with a ton of features including:

* Micronaut Data support
* Dependency injection of Coherence-managed objects
* Listeners for Coherence events
* Messaging with Coherence topics
* Caching support
* Distributed configuration
* HTTP sessions support

## Many Others

This list does not represent the full extent of changes, updates, and improvements. Visit the [whats new](https://docs.micronaut.io/2.4.0/guide/index.html#whatsNew) section of our docs for more information. 

We're proud of the 2.4 release, and we can't wait for everyone to start using these exciting features. As always, if you encounter any problems, please create an issue in our [Github](https://github.com/micronaut-projects/micronaut-core).