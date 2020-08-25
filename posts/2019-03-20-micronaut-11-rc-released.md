title: Micronaut 1.1 RC1 Released
date: Mar 20, 2019  
description: Micronaut 1.1 includes a number of significant refinements! Learn more about the release of Micronaut 1.1 RC1.
author: Graeme Rocher
image: 2019-03-20.png
---

# [%title]

[%author]

[%date] 

Tags: #release

The [Micronaut Team](https://objectcomputing.com/products/2gm-team) at [Object Computing, Inc.](https://objectcomputing.com/) (OCI) is pleased to announce the release of [Micronaut 1.1 RC1](https://github.com/micronaut-projects/micronaut-core/releases/tag/v1.1.0.RC1).

Micronaut 1.1 includes a number of significant refinements since Micronaut's groundbreaking 1.0 release including:

*   Support for [GRPC](https://grpc.io)
*   Support for [GraphQL](https://micronaut-projects.github.io/micronaut-graphql/latest/guide/index.html)
*   Support for [Google Cloud Platform (GCP) and Stackdriver Trace](https://micronaut-projects.github.io/micronaut-gcp/latest/guide/) for distributed tracing.
*   [Message-Driven Microservices with RabbitMQ](https://micronaut-projects.github.io/micronaut-rabbitmq/latest/guide/)
*   A new [BeanIntrospection API](https://docs.micronaut.io/snapshot/guide/index.html#introspection) for reflection-free Bean introspection
*   Support for [AWS API Gateway](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#apiProxy) and [GraalVM Custom Runtime ](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#customRuntimes)
*   [AWS Alexa Lambda](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#alexa) support
*   Native File Watch and Fast Server Restart
*   New test templates for [Micronaut Test](https://micronaut-projects.github.io/micronaut-test/latest/guide/index.html)
*   Even faster cold start time and performance optimizations
*   ... and [many more features and refinements](https://docs.micronaut.io/1.1.x/guide/index.html#whatsNew)

In addition, Micronaut 1.1 RC1 includes massive improvements to support creating GraalVM native images including:

*   **Framework Improvements.** Framework-level dynamic classloading and reflection has been completely removed, making it easier to get applications running on GraalVM `native-image` and reducing the number of customizations necessary.
*   **Build Time Reflection Data.** Thanks to the aforementioned feature, the remaining reflective cases needed for third-party libraries have been replaced by build-time generation of `reflection-config.json` for classes that do require it.
*   **Simplified Image Generation.** You can now generate a native image with just `native-image --class-path myjar.jar` without any additional flags; Micronaut now computes the appropriate GraalVM configuration at compilation time.

I will be talking about Micronaut and these new features at [Greach 2019](https://www.greachconf.com) in Madrid, followed by [Oracle Code Rome](https://developer.oracle.com/code/rome-april-2019). See you there!
