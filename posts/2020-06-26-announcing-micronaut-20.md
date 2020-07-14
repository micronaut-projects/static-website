title: Announcing Micronaut 2.0
date: June 26, 2020  
author: Graeme Rocher & James Kleeh
image:2020-06-26.jpg
---

# [%title]

[%author]

[%date]

Tags: #release

Today, the [Micronaut team](https://objectcomputing.com/products/2gm-team) at [Object Computing, Inc. (OCI)](https://objectcomputing.com/products/micronaut) is proud to announce the general [release](https://github.com/micronaut-projects/micronaut-core/releases/tag/v2.0.0) of Micronaut 2.0!

This release reflects the culmination of more than 6 months of extraordinary work from the Micronaut community. We have expanded our native image support, increased the reactive library support modules, [improved our threading model](https://objectcomputing.com/resources/publications/sett/june-2020-micronaut-2-dont-let-event-loops-own-you"), and added features that give you more service deployment options. With Micronaut 2, you can deploy nearly anywhere. From [Servlet](/blog/2020-03-20-micronaut-20-milestone-1-released.html) to [Serverless](/blog/2020-04-30-micronaut-20-m3-big-boost-serverless-and-micronaut-launch.html), and practically anything in-between.

There's a lot to discover in Micronaut 2.0.&nbsp; For a full list of the new features, please see the [_What's New_ guide](https://docs.micronaut.io/2.0.0/guide/index.html#whatsNew)

Here are some highlights of what's new in Micronaut 2.

## MICRONAUT LAUNCH

In Micronaut 2.0, we have completely rewritten the Micronaut CLI using Micronaut itself and simultaneously launched [Micronaut Launch](https://micronaut.io/launch/), a new tool for creating Micronaut applications that is built on the new CLI.

The new website and CLI are so cool, we wrote [a whole blog post](https://micronaut.io/launch/) to introduce them!

## JDK 14, GROOVY 3

Micronaut 2.0 now supports Java 14 and also adds support for Groovy 3.

## PERFORMANCE IMPROVEMENTS

Micronaut is intensely focused on performance, and we are always looking to improve startup time, memory consumption, and other performance metrics. Micronaut 2.0 comes with improvements in all metrics, with startup times improving by 20% on average.

## IMPROVED SERVERLESS SUPPORT

Micronaut 2.0 introduces support for writing applications that can be deployed to Google's upcoming [Cloud Function](https://micronaut.io/launch/) support for Java.

In addition to Google Cloud Function, we have also added support for [Azure Function](https://azure.microsoft.com/en-us/services/functions/) using the same approach. Check out the [Micronaut Azure Function](https://micronaut-projects.github.io/micronaut-azure/1.0.x/guide/#azureFunction) documentation for more information.

Micronaut 2.0's AWS module has also received a significant upgrade with GraalVM Native Image support for AWS SDK 2 and more.

## MICRONAUT MAVEN PLUGIN

Micronaut 2.0 features a [brand new Maven plugin](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/).

## SUPPORT FOR HTTP/2

[Support for HTTP/2](https://docs.micronaut.io/2.0.x/guide/index.html#http2Server) has been added and can be optionally enabled in both the Netty-based HTTP server and client.

## SERVLET SUPPORT

Micronaut can now go places that weren't possible before, including your favorite Servlet container! [Embedded servers for Jetty, Tomcat, and Undertow](https://github.com/micronaut-projects/micronaut-servlet) are included.

## GRAALVM IMPROVEMENTS

We are continually improving our support for the very popular project, GraalVM. In Micronaut 2.0, we have introduced support for automatic configuration for static resources, JDBC drivers, Hibernate, Flyway, AWS SDK 2, and other areas.

## GRADLE IMPROVEMENTS

Micronaut 2.0 has been upgraded to Gradle 6.5 and further improved our compile-time processors to be more compatible with incremental annotation processing. Java and Kotlin users should experience faster build times.

## SECURITY MODULE IMPROVEMENTS

The Micronaut Security module has [undergone significant changes](https://micronaut-projects.github.io/micronaut-security/2.0.x/guide/#whatsNew) to improve the extensibility of the API, as well as to provide new features.

## UPGRADING

For those upgrading from Micronaut 1, see the&nbsp;<a title="Breaking changes" href="https://docs.micronaut.io/2.0.x/guide/index.html#upgrading" target="_blank">upgrading to Micronaut 2.0 section</a>&nbsp;in our documentation to understand what has changed and how it may impact your application. There are several key changes that everyone upgrading should understand. The most impactful is the change to the default thread selection strategy. We have written a [blog post](https://objectcomputing.com/resources/publications/sett/june-2020-micronaut-2-dont-let-event-loops-own-you) that goes into detail about that change.</p>

## The Road To MICRONAUT 2

Working toward today's release, we published 3 milestones, each including a substantial portion of Micronaut 2's features.&nbsp; We invite you to revisit the path we took here by reviewing our milestone announcements:

- [Micronaut 2.0 M1 - Support for HTTP/2 and Servlets](/blog/2020-03-20-micronaut-20-milestone-1-released.html)
- [Micronaut 2.0 M2](/blog/2020-04-02-micronaut-20-milestone-2-massive-maven-improvements.html) - Massive Maven Improvements
- [Micronaut 2.0 M3](/blog/2020-04-30-micronaut-20-m3-big-boost-serverless-and-micronaut-launch.html) - A Big Boost for Serverless and Micronaut Launch

## The Future

The items listed above certainly are not the entirety of the new features, improvements, and bug fixes included in this release. For a more detailed description of what you can do with Micronaut 2, please see our&nbsp;<a title="Micronaut Documentation" href="https://micronaut.io/documentation.html" target="_blank">documentation</a>.

If you haven't given [Micronaut](https://micronaut.io/index.html) a try yet, now is a great time to [get started](https://micronaut.io/launch/)! We can't wait to see what you build.

Please report any issues or ideas for improvement to our [Github issue tracker](https://github.com/micronaut-projects/micronaut-core/issues). We are very excited for Micronaut 2 and the future of Micronaut!

Special thanks to all the tireless contributors to Micronaut who made this release happen and the excellent feedback from the community.
