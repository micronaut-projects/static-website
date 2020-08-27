---
title: Micronaut 2.0 RC1 Released
date: Jun 16, 2020  
description: Users can now be assured that Micronaut is certified to run on Amazon Corretto, and we will continue our partnership with Amazon to ensure users don't run into any issues in production.
author: Graeme Rocher & James Kleeh
image: 2020-06-16.jpg
---

# [%title]

[%author]

[%date]

Tags: #release

The Micronaut team at Object Computing (OCI) is super excited to announce [the first release candidate](https://github.com/micronaut-projects/micronaut-core/releases/tag/v2.0.0.RC1) on our journey to Micronaut 2.0.

Micronaut 2.0 includes so many great new features, some of which have been detailed in the blog posts for each of the milestone releases:

- [Micronaut 2.0 M1 - Support for HTTP/2 and Servlets](/blog/2020-03-20-micronaut-20-milestone-1-released.html)
- [Micronaut 2.0 M2 - Massive Maven Improvements](/blog/2020-04-02-micronaut-20-milestone-2-massive-maven-improvements.html)
- [Micronaut 2.0 M3 - A Big Boost for Serverless and Micronaut Launch](/blog/2020-04-30-micronaut-20-m3-big-boost-serverless-and-micronaut-launch.html)

For more information about all the new features, see the "What's New" guide.

Here are some of the highlights of Micronaut 2.0:

## Micronaut Launch

In Micronaut 2.0, we have completely rewritten the Micronaut CLI using Micronaut itself and simultaneously launched [Micronaut Launch](https://micronaut.io/launch/), a new tool for creating Micronaut applications that is built on the new CLI.

The new website and CLI are so cool, we wrote [a whole blog post](/blog/2020-04-30-introducing-micronaut-launch.html) to introduce them!

## JDK 14, Groovy 3

Micronaut 2.0 now supports Java 14 and also adds support for Groovy 3.

## Performance Improvements

Micronaut is intensely focused on performance, and we are always looking to improve startup time, memory consumption, and other performance metrics. Micronaut 2.0 comes with improvements in all metrics, with startup times improving by 20% on average.

## Improved Serverless Support

Micronaut 2.0 introduces support for writing applications that can be deployed to Google's upcoming [Cloud Function](https://cloud.google.com/functions) support for Java.

In addition to Google Cloud Function, we have also added support for [Azure Function](https://azure.microsoft.com/en-us/services/functions/) using the same approach. Check out the [Micronaut Azure Function](https://micronaut-projects.github.io/micronaut-azure/1.0.x/guide/#azureFunction) documentation for more information.

Micronaut 2.0's AWS module has also received a significant upgrade with GraalVM Native Image support for AWS SDK 2 and more.

## Micronaut Maven Plugin

Micronaut 2.0 features a [brand new Maven plugin](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/).

## Support for HTTP/2

[Support for HTTP/2](https://docs.micronaut.io/2.0.x/guide/index.html#http2Server) has been added and can be optionally enabled in both the Netty-based HTTP server and client.

## Servlet Support

Micronaut can now go places that weren't possible before, including your favorite Servlet container! [Embedded servers for Jetty, Tomcat, and Undertow](https://github.com/micronaut-projects/micronaut-servlet) are included.

## GraalVM Improvements

We are continually improving our support for the very popular project, GraalVM. In Micronaut 2.0, we have introduced support for automatic configuration for static resources, JDBC drivers, Hibernate, Flyway, AWS SDK 2, and other areas.

## Gradle Improvements

Micronaut 2.0 has been upgraded to Gradle 6.5 and further improved our compile-time processors to be more compatible with incremental annotation processing. Java and Kotlin users should experience faster build times.

## Security Module Improvements

The Micronaut Security module has [undergone significant changes](https://micronaut-projects.github.io/micronaut-security/2.0.x/guide/#whatsNew) to improve the extensibility of the API, as well as to provide new features.

## Upgrading

For those upgrading from Micronaut 1, see the [upgrading to Micronaut 2.0 section](https://docs.micronaut.io/2.0.x/guide/index.html#upgrading) in our documentation to understand what has changed and how it may impact your application. There are several key changes that everyone upgrading should understand. The most impactful is the change to the default thread selection strategy. We have written a [blog post](https://objectcomputing.com/resources/publications/sett/june-2020-micronaut-2-dont-let-event-loops-own-you) that goes into detail about that change.

## Micronaut 2 GA

The items listed above certainly are not the entirety of the new features, improvements, and bug fixes included in this release. For a more detailed description of what's new, see our [documentation](https://docs.micronaut.io/2.0.0.RC1/guide/index.html#whatsNew).

With the first release candidate behind us, we look forward to the release of Micronaut 2. This release candidate marks a significant milestone in the progress we have made over the last 6 months. We hope you give this release candidate a try.

Please report any issues or ideas for improvement to our [Github issue tracker](https://github.com/micronaut-projects/micronaut-core/issues). We are very excited for Micronaut 2 and the future of Micronaut!
