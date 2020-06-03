title: Micronaut 1.2 RC1 Released
date: June 5, 2019 
description: Micronaut 1.2 release announcement
author: Graeme Rocher
---

# [%title]

**By [%author]**

[%date] 

The [Micronaut team](https://objectcomputing.com/products/2gm-team) at [Object Computing, Inc.](https://objectcomputing.com/) (OCI) is excited to announce the release of [Micronaut 1.2 RC1](https://github.com/micronaut-projects/micronaut-core/releases/tag/v1.2.0.RC1). This is the first release candidate of Micronaut's 1.2 line and includes a number of significant new features including:

*   Micronaut Security 1.2 with Support for [OAuth 2.0 and OpenID Connect](https://micronaut-projects.github.io/micronaut-security/1.2.x/guide/#oauth)
*   Improved Validation Support with [Native, Reflection-free Bean Validation](https://docs.micronaut.io/1.2.x/guide/index.html#beanValidation)
*   Environment Detection for [Oracle Cloud](https://cloud.oracle.com/home)
*   [Distributed Configuration with HashiCorp Vault](https://docs.micronaut.io/1.2.x/guide/index.html#distributedConfigurationVault) 
*   ... and [many more improvements](https://docs.micronaut.io/1.2.x/guide/index.html#whatsNew)

The most significant new feature is the updated security module, which includes OpenID connect support that works with a variety of providers. Check out the [Securing a Micronaut App with Okta guide](https://guides.micronaut.io/micronaut-oauth2-okta/guide/index.html), which demonstrates how to use [Okta](https://www.okta.com) as the OpenID provider.

Validation also gets a significant upgrade with native support for bean-validation annotations without needing to pull in Hibernate Validator. This has numerous benefits, including reducing the JAR size of a Micronaut application, improving startup time, and eliminating reflection. Plus the new validation module now supports reactive flows! 

Thanks to all those who contributed to the release, and we look forward to your feedback as we progress Micronaut 1.2 toward a GA release.