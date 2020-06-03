title: Micronaut 2.0 Milestone 1 Released
date: March 20, 2020  
description: Micronaut 2.0 Milestone 1 release announcement
author: Graeme Rocher
---

# [%title]

**By [%author]**

[%date] 

The [Micronaut Team](https://objectcomputing.com/products/2gm-team "Groovy, Grails, and Micronaut Team") at [Object Computing, Inc.](https://objectcomputing.com/) (OCI) is pleased to announce the [first milestone of Micronaut 2.0](https://github.com/micronaut-projects/micronaut-core/releases/tag/v2.0.0.M1)!

Micronaut 2.0 represents the next evolution of Micronaut and focuses on the following key new features:

## Support for HTTP/2

Support for HTTP/2 has been added and can be optionally enabled in both the Netty-based HTTP server and client.

Getting started with HTTP/2 support is as simple as enabling it in your application configuration:

```bash
# application.yml
micronaut:
  server:
      http-version: 2.0
```

## Servlet Support

Micronaut can now go places that weren't possible before, including your favorite servlet container! [Embedded servers for Jetty, Tomcat, and Undertow](https://github.com/micronaut-projects/micronaut-servlet) are included and can be activated via the command line:

```bash
$ mn create-app myapp --features jetty-server
```

Using Micronaut's support for GraalVM, you can generate a native image of any Tomcat or Jetty application!

## Threading Model Improvements

A big change from Micronaut 1.x are the improvements to the threading model, including the ability to explicitly configure and share Netty EventLoopGroup instances and more effectively take advantage of the EventLoop programming model.

The improvements to the threading model allow Micronaut to more effectively conserve resources in environments where resources are limited, such as IoT devices.

## Server-Side Content Negotiation Improvements

Micronaut's support for server-side content negotiation has been greatly improved, allowing users to more effectively support both XML and JSON responses.

## And Lots More

There are many more improvements in this release, from support for [Jdbi](https://jdbi.org/) to improved support for Cloud Foundry. We recommend you checkout the [release notes in full](https://docs.micronaut.io/2.0.x/guide/index.html#whatsNew) for all the details.

## More to Come

Micronaut 2.0 Milestone 1 is just the first milestone on Micronaut's journey towards 2.0\. It gives users the opportunity to take important new features like HTTP/2 support out for spin and provide feedback to make the GA release as solid as possible.

Thanks to the community for your great contributions so far; your input helped make this release happen. Enjoy!
