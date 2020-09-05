---
title: Practical Performance Comparison of Spring Boot, Micronaut 1.3, Micronaut 2.0
date: Apr 28, 2020
description: Modern Java frameworks are challenging each other to keep pushing features and performance into previously inconceivable areas. Here are the results of our most recent Spring Boot, Micronaut 1.3, and Micronaut 2.0 comparisons.
author: Jason Schindler (OCI Partner and Practice Manager of Grails and Micronaut)
image: 2020-04-28.png
---

# [%title]

[%author]

[%date]

Tags: #springboot

Have you taken a look at the Micronaut 2.0 pre-releases yet?

The [Micronaut Team](https://objectcomputing.com/products/2gm-team) at [Object Computing](https://objectcomputing.com/") has been putting a lot of hard work into making Micronaut even better with a number of exciting features and improvements including:

- JDK 14 support
- [Servlet](/blog/2020-03-03-back-future-micronaut-servlet.html) container support
- [HTTP/2 support](https://docs.micronaut.io/2.0.x/guide/index.html#http2Server)
- A [new Maven plugin](/blog/2020-04-02-micronaut-20-milestone-2-massive-maven-improvements.html)
- An improved threading model
- … [and much more](https://docs.micronaut.io/2.0.x/guide/index.html#whatsNew)

We haven’t put the final bow on Micronaut 2.0.0 yet. Stay tuned in the coming weeks for additional feature announcements and enhancements.

## Performance Improvements

Performance has been a critical consideration of the [Micronaut Framework](https://micronaut.io/) since its inception. Micronaut out-performs traditional Java frameworks like Spring Boot by leaps and bounds. Still, we are always looking for ways to improve Micronaut's start-up time, memory consumption, and performance.

Whether your application is deployed on a resource-constrained edge device, or you're looking for ways to process more requests with fewer cloud resources, modern application frameworks have to take performance seriously in order to stay competitive. Micronaut 2.0 includes some significant performance improvements over the 1.3 release, and we'd like to share some data with you comparing the performance of Micronaut 1.3.4, Micronaut 2.0.0 M2, and Spring Boot 2.2.

## Source Code and Instructions

We are, of course, a bit biased towards our framework, so we would like to invite you to try these tests out yourself. Source code and instructions for building the projects and gathering your own results are available [here](https://github.com/micronaut-projects/micronaut-comparisons/).

## Test Hardware

*   HP ZBook Laptop
*   Intel(R) Core(TM) i7-6700HQ CPU @ 2.60GHz
*   16GB of RAM
*   OpenJDK Runtime Environment (build 1.8.0_252-b09)
*   Fedora Linux 31

## Performance Measurements

----
###### UPDATE 05/29/2020

A community member [pointed out](https://github.com/micronaut-projects/micronaut-comparisons/issues/1) that the configurations for the Spring Boot JHipster Sample application and the Micronaut JHipster Sample application differed in two ways, which put Spring Boot at a disadvantage. First, the Spring Boot application had a logging level of DEBUG, causing a lot more messages to be logged. Second, the Spring Boot application was writing audit records to the database in addition to the records being created as part of the load test. We have [changed the configurations](https://github.com/micronaut-projects/micronaut-comparisons/pull/2) and, as expected, observed an improvement in the performance of the Spring Boot application. This blog post has been updated to reflect these observations. Thank you for your feedback Christian!

###### END UPDATE
-----

For this comparison, we investigated application start-up time, time to first response, and throughput and memory consumption of an application under load.

Start-up time and time to first response were measured using a simple “Hello World” REST application.

For the throughput and memory consumption measurements, we wanted to use a more substantial application than a basic Hello World, so we reached for the sample projects available through the [JHipster](https://www.jhipster.tech/) project. These applications include authentication and authorization, input validation, database interactions, and a number of other configurations that place them closer to the feature set of an application that you might encounter in a production environment.

All code was tested by executing a pre-built jar file and setting the maximum heap allocation to 128m. While Micronaut 2.0 supports JDK 14, all tests were run using OpenJDK 1.8.0_242.

## Start-Up Time

To measure start-up time, we created three simple “Hello World” REST services using Spring Boot 2.2.6, Micronaut 1.3.4, and Micronaut 2.0.0 M2.

Micronaut and Spring Boot both report start-up time after initialization, and we used the numbers reported by the frameworks for this measurement. We started each service five times in sequence and recorded the best time for each.

<table>
<thead>
<tr><th>Spring Boot 2.2.6</th><th>Micronaut 1.3.4</th><th>Micronaut 2.0.0 M2</th></tr>
</thead>
<tbody>
<tr><td>1997 ms</td><td>955 ms</td><td><b>813 ms</b></td></tr>
</tbody>
</table>

As you can see, Micronaut 1.3.4 started up in a little under half the time of the comparable Spring Boot application, and Micronaut 2.0.0 M2 showed a nearly 15% improvement over 1.3.4!

## Time to First Response

Once an application has started, it isn’t always ready to start serving requests. The time to first response includes start-up time and any other time required to get the application ready to respond to requests.

To measure time to first response, we included a Node script that started a provided jar file and began issuing HTTP requests to a Hello World endpoint until one succeeded. Just as we did for start-up time, we performed this test against each service five times and recorded the best result.

<table>
<thead>
<tr><th>Spring Boot 2.2.6</th><th>Micronaut 1.3.4</th><th>Micronaut 2.0.0 M2</th></tr>
</thead>
<tbody>
<tr><td>2,741 ms</td><td>1,496 ms</td><td><b>1,295 ms</b></td></tr>
</tbody>
</table>

Here we see similar results to the start-up time test. Micronaut 1.3.4 saved about 45% of the time required by Spring Boot, and Micronaut 2.0.0 M2 shaved another 200 milliseconds off that!

## Application Performance Under Load

Start-up time is important, but load testing is much more fun. As mentioned above, rather than load test simple bare bones applications, we felt this comparison would be much more relevant if we tested applications that perform some work that a production app would do – things like performing database calls and validating input.

Luckily, the JHipster project includes sample applications with various configurations. For this comparison, we used the [JHipster Micronaut Sample Application](https://github.com/jhipster/jhipster-sample-app-micronaut) and the [JHipster Spring Boot Sample Application](https://github.com/jhipster/jhipster-sample-app). Additionally, we included a version of the Micronaut JHipster Sample Application that had been upgraded to Micronaut 2.0.0 M2.

We did modify the JHipster sample applications in a significant way for this test. We replaced the code performing bcrypt encoding and verification with a no-op implementation.

Bcrypt, by design, is a computationally expensive calculation. That's a good thing when it comes to hashing user passwords for storage and verifying that current hashes match provided input. When it comes to measuring performance of a framework under load, it makes the results a bit less interesting.

To simulate load on our sample applications, we created a [Gatling](https://gatling.io/) test suite project that simulates 1,000 users over a fixed period of 60 seconds. The scenario includes:

- Performing a health check
- Retrieving user account information
- Creating a new bank account record in the database

For each application, we recorded the number of requests completed during the 60 second test, the mean response time of all requests, and the mean requests per second that the application handled. After the load tests were completed, we recorded the amount of RSS memory consumed by the Java process running the application under load.

<table>
<thead>
<tr><th></th><th>Spring Boot 2.2.5</th><th>Micronaut 1.3.4</th><th>Micronaut 2.0.0 M2</th></tr>
</thead>
<tbody>
<tr><td>Total Requests in 60s</td><td>111,137</td><td>132,391</td><td><b>153,557</b></td></tr>
<tr><td>Mean Response Time</td><td>444 ms</td><td>375 ms</td><td><b>323 ms</b></td></tr>
<tr><td>Mean Requests/Second</td><td>2020.673</td><td>2407.109</td><td><b>2791.945</b></td></tr>
<tr><td>RSS Memory After Load Test</td><td>473.328 MB</td><td>435.092 MB</td><td><b>424.560 MB</b></td></tr>
</tbody>
</table>

As you can see, under load, Micronaut 1.3.4 was able to process over 20,000 additional requests as the Spring Boot application while utilizing less memory. Micronaut 2.0.0 M2 improved on this further by shaving an additional 50 milliseconds off of the mean response time while consuming less memory than the Micronaut 1.3.4 application.

## Summary

This is an exciting time to be a developer in the Java ecosystem. Modern Java frameworks are challenging each other to keep pushing features and performance into previously inconceivable areas.

The Micronaut Team at Object Computing, works every day to improve Micronaut. We are dedicated to creating a framework that can perform under pressure and provide developers with the features and functionality they need to create fast, reliable, well-tested, and integrated applications. If you haven’t taken a look at Micronaut 2.0 yet, now is a great time to investigate what’s new in your favorite framework.
