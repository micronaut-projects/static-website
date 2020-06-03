title: Micronaut 1.1 Released
date: April 11, 2019  
description: Micronaut 1.1 Release Announcement
author: Graeme Rocher
---

# [%title]

**By [%author]**

[%date] 

Joining the many exciting announcements happening at [Google Cloud Next](https://cloud.withgoogle.com/next/sf), the [Micronaut Team](https://objectcomputing.com/products/2gm-team) at [Object Computing, Inc.](https://objectcomputing.com/) (OCI) is pleased to announce the release of [Micronaut 1.1](https://github.com/micronaut-projects/micronaut-core/releases/tag/v1.1.0) GA.

Micronaut 1.1 includes a number of significant refinements since Micronaut's groundbreaking 1.0 release including:

*   Support for [GRPC](https://grpc.io)
*   Support for [GraphQL](https://micronaut-projects.github.io/micronaut-graphql/latest/guide/index.html)
*   Support for [Google Cloud Platform (GCP) and Stackdriver Trace](https://micronaut-projects.github.io/micronaut-gcp/latest/guide/) for distributed tracing
*   [Message-Driven Microservices with RabbitMQ](https://micronaut-projects.github.io/micronaut-rabbitmq/latest/guide/)
*   A new [BeanIntrospection API](https://docs.micronaut.io/snapshot/guide/index.html#introspection) for reflection-free Bean introspection
*   Support for [AWS API Gateway](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#apiProxy) and [GraalVM Custom Runtime ](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#customRuntimes)
*   [AWS Alexa Lambda](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#alexa) support
*   Native File Watch and Fast Server Restart
*   New test templates for [Micronaut Test](https://micronaut-projects.github.io/micronaut-test/latest/guide/index.html)
*   Even faster cold start time and performance optimizations
*   ... and [many more features and refinements](https://docs.micronaut.io/1.1.x/guide/index.html#whatsNew)

Micronaut 1.1 makes it even simpler to build efficient, cloud-ready applications that are simultaneously easy to test, easy to containerize, and efficient. 

Micronaut can be deployed to both [Google Cloud Run](https://cloud.google.com/run/) and [Google App Engine Standard for Java 11](https://cloud.google.com/appengine/docs/java/), and with Micronaut 1.1, we have dedicated support for [Google Stackdriver Trace](https://cloud.google.com/trace/).

To help you get started with Google Cloud Run we have prepared a couple of [sample applications](https://github.com/micronaut-projects/micronaut-gcp/tree/master/examples): 

*   A [Hello World sample application](https://github.com/micronaut-projects/micronaut-gcp/tree/master/examples/hello-world-cloud-run) that is ready to be deployed to Cloud Run using [Jib](https://github.com/GoogleContainerTools/jib) and [Google Container Registry](https://cloud.google.com/container-registry/).
*   A [second application that uses GraalVM native and Google Cloud Build](https://github.com/micronaut-projects/micronaut-gcp/tree/master/examples/hello-world-cloud-run-graal) to build and deploy a native image to Google Cloud Run.

Thank you to all who provided feedback during the extended RC phase of Micronaut 1.1, enjoy the release!