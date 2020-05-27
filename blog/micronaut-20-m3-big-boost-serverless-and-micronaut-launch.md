title: Micronaut 2.0 M3 - A Big boost for serverless and Micronaut Launch
date: Apr 30, 2020
description: The Micronaut team is super excited to announce the final milestone on our journey to Micronaut 2.0.
author: Graeme Rocher
author title: OCI Grails & Micronaut Product Lead and Principal Software Engineer
---

# [%title]

**By [%author]**, [%author title]

[%date] 

The Micronaut team at Object Computing is super excited to announce the [final milestone](https://github.com/micronaut-projects/micronaut-core/releases/tag/v2.0.0.M2) on our journey to Micronaut 2.0.

This milestone focuses on two important areas of the framework: the command line interface (CLI) and serverless support.

For more information about all the new features, see the ["What's New" guide](https://docs.micronaut.io/2.0.0.M3/guide/index.html#whatsNew).

## Introducing Micronaut Launch

The big news with this announcement is the rearchitecture of Micronaut's venerable `mn` CLI for generating new applications from terminal, which is receiving a big upgrade.

In Micronaut 2.0, we have completely rewritten the CLI in Micronaut itself and simultaneously launched [Micronaut Launch](https://micronaut.io/launch/), a new tool for creating Micronaut applications that is built on the new CLI.

The new website and CLI are so cool, we wrote a [whole blog post](https://objectcomputing.com/news/2020/04/30/introducing-micronaut-launch) to introduce them!

## Google Cloud Function Support

Micronaut 2.0 introduces support for writing applications that can be deployed to Google's upcoming [Cloud Function](https://cloud.google.com/functions) support for Java.

You can either write [simple functions](https://micronaut-projects.github.io/micronaut-gcp/2.0.x/guide/#simpleFunctions) directly or write regular Micronaut controllers and have Micronaut's function support [route requests to these controllers](https://micronaut-projects.github.io/micronaut-gcp/2.0.x/guide/#httpFunctions). This latter approach allows you to use the regular HTTP server and also deploy the same application to Cloud Function.

Try it out at [Micronaut Launch](https://micronaut.io/launch/) by choosing the `google-cloud-function` feature!

## Microsoft Azure Function Support

In addition to Google Cloud Function, we have also added support for [Azure Function](https://azure.microsoft.com/en-us/services/functions/) using the same approach. You can either write simple functions that directly use Azure's native annotations, or you can write controllers and have Micronaut route requests from the Azure function trigger.

Check out the documentation for [Micronaut Azure Function](https://micronaut-projects.github.io/micronaut-azure/1.0.x/guide/#azureFunction) for more information.

Try it out at Micronaut Launch by choosing the "azure-function" feature!

## The Road Ahead

Micronaut 2.0 M3 is the final milestone, and we will soon start issuing release candidates, so try it out now and let us know what you think!

Thanks to the community for your great contributions so far; your input helped make this release happen. Enjoy!