---
title: How do we test Micronaut and GraalVM integration?
date: Oct 12, 2020
author: Iván López
image: xxxxxxx
---

# [%title]

[%author]

[%date]

Tags: #micronaut #graalvm #test #gitlabci

Micronaut and [GraalVM](https://www.graalvm.org) go way back. Ever since our 1.0 release back in October of 2018, Micronaut has proudly included out-of-the-box support for GraalVM and was one of the first large Java frameworks to do so.

I still remember the _early days_ when our integration would work exceptionally for GraalVM 1.0-RC6, but then something would change in 1.0-RC7 and things would break.  A while later, 1.0-RC8 would be released and everything would work great again... until it didn't.

We needed to make sure we were able to test our integration before the GraalVM team released a new version.  By testing early, any issues and regressions we discover could be reported and fixed before the next release.

It was January 2019 (less than three months after we [released Micronaut 1.0](https://micronaut.io/blog/2018-10-23-micronaut-10-ga-released.html))
and we were still using Travis CI for testing Micronaut. Travis had a lot of constraints that didn't fit well with
GraalVM.  Specifically, a really low memory limit, prohibitive options for creating complex CI workflows, and no support for creating artifacts for use in later CI pipeline jobs.  We needed another way.

## Introducing Gitlab CI

[Gitlab CI](https://docs.gitlab.com/ee/ci/) is part of [Gitlab](https://about.gitlab.com/) (although you don't need to use GitLab proper to use the CI). GitLab CI was an easy choice for me because I used it a lot in a previous job, and I really love working with it.

The idea was to create a CI pipeline with the following objectives in mind:

- It should execute periodically instead of at each commit.
- It should only run if there are changes in either Micronaut or GraalVM.
- It should compile GraalVM from `master` branch, so we could detect any issue or regression as soon before the next GraalVM release.
- It should be able to test different Micronaut applications easily.
- It should support different CI stages and jobs with some dependencies between them.

After experimenting with a few different approaches in [a new repository](https://gitlab.com/micronaut-projects/micronaut-graal-tests), we were able to get a passing build using this [CI pipeline](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/pipelines/43802345):

![First CI pipeline](2020-10-12-first-ci-pipeline.png)

It has four stages that we still use:

- **Log-commits**: Saves the commits that triggered the build for both Micronaut and GraalVM. If the build fails, we can
check those commits and see why.
- **Build-graal**: Clones the GraalVM repository and builds it from `master` branch. The GraalVM JDK distribution generated in
this stage is saved as an _artifact_ for use in the next stage.
- **Micronaut**: Clones various Micronaut test applications and builds a native image using the GraalVM distribution created
in the previous stage. The native images are saved again as artifacts, so they can be used for the tests.
- **Test**: Starts the native images generated in the previous stage and runs some tests to make sure everything works as expected.
These tests are `curl` requests to different endpoints and we check the response.


For keeping track of new commits in Micronaut and GraalVM, and running the scheduled jobs, we have
[another companion project](https://gitlab.com/micronaut-projects/micronaut-graal-tests-scheduler) in which we store the
 latest commit for both projects and decide if we should trigger the CI in the main repository.


## Test applications

Since our original pipeline, a lot of things have changed.  Our two initial applications have become 26 test applications with 39
different jobs. This is because some of them test the same things, but with different options. For example in the
[Micronaut JDBC](https://github.com/micronaut-graal-tests/micronaut-data-jdbc-graal) test application, there are branches
for the all the databases supported: H2, Postgres, Oracle, MariaDB, SQL Server, and MySQL.

Also, the number of jobs have been multiplied by four because we now test GraalVM for JDK 8 and JDK 11, and we also test
 against two Micronaut versions (currently `2.0.x` and `2.1.x`). This is how the CI pipeline looks like today:


![CI pipeline today](2020-10-12-ci-pipeline-today.png)

Very long an beautiful list, right? ;-)

The list includes test applications for RabbitMQ, gRPC, Service Discovery with Consul and Eureka, Security, Micronaut
Data, Flyway, Elasticsearch, Redis, and many more. We are increasing the list with new applications, and we are
committed to have more and more things supported in every release.


## Own runners on AWS

Some months ago, we hit a limit on the Gitlab CI shared runners. A few jobs were failing when building the native
images because of an out of memory exception. Basically, they didn't have enough memory to build the native image and the
 process failed.

Gitlab CI provides a way to use [your own runners](https://docs.gitlab.com/runner/) so we setup an
[auto-scaling runner](https://docs.gitlab.com/runner/configuration/runner_autoscale_aws/) infrastructure on AWS for
the jobs that required additional memory.

In order to choose the right instance type, I did some tests without limiting the number of instances launched during the auto-scale.

| Instance | Specs | Time to run pipeline | CPU usage |
| --- | --- | --- | --- |
| c5.xlarge | 4 CPUs - 8 GB RAM | 17 mins | 60-70% |
| c5.2xlarge | 8 CPUs - 16 GB RAM | 13 mins | 30-40% |
| c5.24xlarge | 96 CPUs - 192 GB RAM | 43 mins | 10% |

You may wonder why using a huge instance like `c5.24xlarge` takes almost 3 times longer to run the CI pipeline than
the others. The answer is you can only allocate up the 512 CPUs on AWS without asking AWS support for an increase, so choosing the biggest instance only allowed us to run 5 of them in parallel.

With those numbers it felt right to choose `c5.xlarge` because the time difference with `c5.2xlarge` was minimal, and
the former maximized the cpu usage and costs half of the latter.

![EC2 instances auto-scaling](2020-10-12-auto-scaling.png)


Eventually, I moved some other jobs to run on our AWS runners because of the native image build time.
The AWS runners are so fast compared to the shared runners that they can build almost three native images in the time the shared runners
build one. With the current mix of shared and dedicated runners, the build takes around 35-40 minutes to finish.

Here are some of the time improvements we witnessed:

| App | Shared runners | Own runners |
| --- | --- | --- |
| GraalVM | [23 min](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/jobs/548418789) | [7 min 29 sec](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/jobs/561601061) |
| MS SQL | [14 min 18 sec](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/jobs/568011290) | [5 min 33 sec](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/jobs/569072355) |
| gRPC (server + client) | [35 min](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/jobs/686515921) | [7 min 3 sec](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/jobs/686618254) |


Configuring a job to run on our own runner is pretty simple with Gitlab. It's a matter of tagging it with `aws`, `memory`, or `speed`. I use tag combinations like `aws` + `memory`, or `aws` + `speed` as a way to document why
a specific job is running on our own runners.

![Runners tags](2020-10-12-runners-tag.png)


## Test dev-release versions

Some time ago the GraalVM team started to publish [dev-releases](https://github.com/graalvm/graalvm-ce-dev-builds/releases)
every few days for the community to test against so you wouldn't have to compile everything from source yourself. They asked
us if we could verify that Micronaut was working with the latest _dev-release_ just a few days before releasing a final
GraalVM version.
As we have a very granular and flexible CI pipeline, it was easy for us to add a new branch and automate the tests for
those _dev-releases_.
Now, we have a scheduled job that is executed every 2 or 3 days that automatically downloads the latest _dev-release_ available
and runs all the tests with it.
For the curious, everything remains the same, and the only change is in the [GraalVM build script](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/blob/2.1.x_dev-preview/build-graalvm.sh#L9)
 that now downloads the latest _dev-release_ version.


## Was it worth the effort?

What started as a small thing has evolved a lot in the past 21 months. Time has proved that it was really worth the effort
we put into this. For example, just a few days ago the [gRPC test application failed](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/jobs/774292619)
and I was able to track down the commit that broke the build and [report an issue](https://github.com/oracle/graal/issues/2896)
to the GraalVM team. Then, just a few days later they fixed the issue and our build [is back to green](https://gitlab.com/micronaut-projects/micronaut-graal-tests/-/pipelines/199942162) :-)

Speaking about reporting issues, we have been working together with them to improve the GraalVM support in Micronaut,
and I've reported a lot of issues to make GraalVM better.

![Tweet Thomas Wuerthinger](2020-10-12-tweet-thomas.png)


We, the Micronaut team at Object Computing, will continue to improve our GraalVM integration in every Micronaut release.  We will also do everything we can to make integrating your Micronaut applications with GraalVM as easy as possible.

Our support has improved a lot in Micronaut 2.1 with the new [Micronaut Gradle plugin](https://github.com/micronaut-projects/micronaut-gradle-plugin).
Take a look at [this video](https://micronaut.io/blog/2020-10-08-micronaut-gradle-plugin.html) in which Graeme Rocher
does a demo of it.

For Micronaut 2.2 we also plan to release an improved version of our current [Micronaut Maven plugin](https://github.com/micronaut-projects/micronaut-maven-plugin)
which will include these new features as well.
