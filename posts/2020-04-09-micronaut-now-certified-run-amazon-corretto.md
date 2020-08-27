---
title: Micronaut Is Now Certified to Run on Amazon Corretto
date: Apr 9, 2020  
description: Users can now be assured that Micronaut is certified to run on Amazon Corretto, and we will continue our partnership with Amazon to ensure users don't run into any issues in production.
author: Álvaro Sánchez-Mariscal
image: 2020-04-09.jpg
CSS: https://micronaut.io/stylesheets/prismjs.css
JAVASCRIPT: https://micronaut.io/javascripts/prismjs.js
---

# [%title]

[%author]

[%date] 

Tags: #aws #correto

One practice used by the [Micronaut development team](https://objectcomputing.com/products/2gm-team "Groovy, Grails, and Micronaut Team") is continuous integration (CI). In support of this, the [Micronaut framework](https://micronaut.io/ "Learn more about the Micronaut Framework")'s core has an extensive test suite executed via [GitHub Actions](https://github.com/features/actions), and since the beginning of 2020, tests have also been executed with [Amazon Corretto](https://aws.amazon.com/corretto/) 8 and 11.

Users can now be assured that Micronaut is certified to run on Amazon Corretto, and we will continue our partnership with Amazon to ensure users don't run into any issues in production.

Amazon Corretto is an OpenJDK distribution that provides free, long-term support with no pay-gated features or restrictions on how it's used in production. Corretto is used by thousands of Amazon workloads; for example, it's the JDK used by the [AWS Lambda `java11` runtime](https://aws.amazon.com/blogs/compute/java-11-runtime-now-available-in-aws-lambda/), which provides insights Amazon uses to push improvements upstream.

Since Micronaut offers [first-class support for AWS](https://micronaut-projects.github.io/micronaut-aws/latest/guide), particularly for Lambda functions, we thought it prudent to include a specific workflow to run Micronaut core tests with Corretto. We are also making sure that tests are run within an Amazon Linux 2 base image, which is the environment users will find in a java11 runtime-based Lambda.

To enable the Micronaut team to continuously test and support Micronaut running on Corretto, we use the official `amazoncorretto:8` and `amazoncorreto:11` Docker images, which are built on top of `amazonlinux:2`.

With the exception of some environment variables (which may be specific to the use case of running within GitHub Actions and within a Docker image), Corretto has proven to be a drop-in replacement for other OpenJDK distributions. It has not produced any difference compared to the main CI workflow, which is run with GitHub Actions' default Zulu Community distribution of OpenJDK. You can check the whole workflow file [here](https://github.com/micronaut-projects/micronaut-core/blob/master/.github/workflows/corretto.yml).

## Choosing Corretto

If you wish to use Corretto locally as your OpenJDK distribution, you can download it from [Amazon's downloads page](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html). Alternatively, installation can be greatly simplified using [SDKMAN!](https://sdkman.io/), which makes the process of switching between Java distributions and versions a piece of cake.

Once you have SDKMAN! Installed, you can install and use Corretto 11 by simply typing:

```
$ sdk install java 11.0.6-amzn
$ sdk use java 11.0.6-amzn
```

Then, check the version used:

```
$ java -version
openjdk version "11.0.6" 2020-01-14 LTS
OpenJDK Runtime Environment Corretto-11.0.6.10.1 (build 11.0.6+10-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.6.10.1 (build 11.0.6+10-LTS, mixed mode)
```

And you’re done!

## Looking Ahead

The Micronaut team is continually looking at ways to improve and validate the Micronaut framework against production cloud environments like Amazon Web Services.

While Amazon Corretto is not strictly a user-facing feature, users can forge ahead with Micronaut and AWS with the assurance that the technologies are certified to work together.

### Explore Further

Stefano Buliani (Amazon Web Services) and Sergio del Amo (Object Computing) recently hosted a webinar and demonstrated how the Micronaut Framework's built-in features enable seamless integration with AWS services. 

[View Webinar](https://objectcomputing.com/products/micronaut/resources/micronaut-and-aws "Combining Micronaut and AWS to Superpower Your Apps")

### Expand Your Expertise

The team regularly publishes [Micronaut Guides](https://guides.micronaut.io/ "Micronaut Guides"), tutorials that enable developers to take their skills to the next level and get the most out of the framework.

### Relevant Guides

[Micronaut Functions in GraalVM Native Images Deployed to AWS Lambda](https://guides.micronaut.io/micronaut-function-graalvm-aws-lambda-gateway/guide/index.html)

[Deploy to AWS ElasticBeanstalk](https://guides.micronaut.io/micronaut-elasticbeanstalk/guide/index.html)

[Micronaut Functions Deployed in AWS Lambda](https://guides.micronaut.io/micronaut-function-aws-lambda/guide/index.html)

