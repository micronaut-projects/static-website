title: Back to the Future with Micronaut Servlet
date: Mar 3, 2020  
description: Micronaut Servlet Announcement
author: Graeme Rocher
image: 2020-03-03.jpg
---

# [%title]

[%author]

[%date] 

Tags: #servlet #jetty #undertow

The [Micronaut Team](https://objectcomputing.com/products/2gm-team "Groovy, Grails, and Micronaut Team") at [Object Computing, Inc.](https://objectcomputing.com/) (OCI) is pleased to announce the first milestone of [Micronaut Servlet](https://github.com/micronaut-projects/micronaut-servlet), a new implementation of Micronaut that runs on traditional Java servlet containers.

Micronaut Servlet provides support for replacing the Netty-based HTTP server that comes with Micronaut with either [Jetty](https://www.eclipse.org/jetty/), [Tomcat](http://tomcat.apache.org/), or [Undertow](http://undertow.io), which may be of interest to those who are already familiar with the thread-per-request model of traditional servlet containers and have an existing large investment in the servlet ecosystem.

Additionally, the [Jetty](https://micronaut-projects.github.io/micronaut-servlet/1.0.x/guide/#jetty) and [Tomcat](https://micronaut-projects.github.io/micronaut-servlet/1.0.x/guide/#tomcat) implementations can be used with [GraalVM native image](https://www.graalvm.org/) to produce native servlet applications that start in milliseconds and consume very little memory.

[Grails](https://grails.org/) users also stand to benefit in the future by allowing Micronaut to run side-by-side, embedded within an existing Grails servlet-based application.

See the [documentation for Micronaut Servlet](https://micronaut-projects.github.io/micronaut-servlet/1.0.x/guide/#introduction) for more information.
