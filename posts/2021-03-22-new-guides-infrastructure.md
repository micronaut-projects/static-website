---
title: Improving the Micronaut Guides infrastructure
date:  March 22, 2021
description: xxxxxxxxxx
author: Iván López
image: xxxxx.jpg
---

# [%title]

[%author]

[%date]

Tags: #micronaut2 #guides

Since Micronaut 1.0 we have been heavily invested in the [Micronaut Guides](https://guides.micronaut.io/). We know it is a very good way to show different aspects of the framework with a working application that users can run and modify to their needs.
In more than three years we have published around 50 guides, most of them written in Java, Groovy and Kotlin. We've put a lot of effort and time to keep them upgraded with the different Micronaut versions, but we've reached a point that keeping them up to date it's a huge task. Besides, even if all the guides have tests, we didn't run them unless we did some changes in the guide itself, so we were wasting a very good opportunity to have more tests for Micronaut itself and the different modules.
So we needed another way...


## A new infrastructure for the guides

We wanted to create a new infrastructure for the guides with different goals in mind:

- Automatic migration of all the guides everytime there is a new Micronaut version
- Test the guides not only with the current Micronaut version but also with next Micronaut patch and minor snapshot versions
- Keep all the guides in the same repository. With our current approach every guide has its own repository, and even when we have the three languages version of the guide we have three different repositories.
- Generate guides for Gradle and Maven users. We love Gradle, and we use it in every single Micronaut module, so it made sense to build our guides with Gradle. To be realistic we know that a lot of our users prefer Maven, so we also want to generate the guides using their favourite build tool to make things easier for them.
- Simplify how we write the guides. At this moment we created a _table of content_ file than include different Asciidoctor files and combine them all when rendering the guide. We wanted to write the whole guide in one single Asciidoctor file.


We have been working hard in creating a new infrastructure for the guides with all those goals in mind. Let's discuss how we've solved them in more detail.

### Automatic migration of the guides

This is the most important thing we wanted to improve. Keeping the guides up to date with every Micronaut version is important because new users will find working examples with the latest version of the framework.
To fix this problem we are leveraging in [Micronaut Launch](https://micronaut.io/launch/). It's not that we are calling the RESTful endpoints exposed by Launch, but that we are using the code that powers the website directly in our new guides. What we do is generate programmatically a project for the desired Micronaut version with the every combination of language, build tool, and the features needed for that guide. Then we copy some specific code from the guide and package every project as a zip file ready for users to download them.
When a new Micronaut version is out we only need to update the version in one file, submit a new pull request, and if all the test pass, merge it, and we have automatically migrated all the guides to that version. Awesome, isn't it? :-)

Speaking about tests, how do we test the guides? During the guides generation process we create on-the-fly the file `build/code/test.sh` that contains the necessary code to execute either `./gradlew test` or `./mvnw test` in every generated guide.


### Test Micronaut snapshot versions

We want to be able to detect potential issues with future Micronaut releases before it's too late for us to fix them. We have setup a weekly cronjob in Github Actions that will run all the tests with the next Micronaut patch and minor snapshot versions. If something fails we know that we have introduced an unnoticed regression, or a breaking change that we need to avoid and fix it.


### Use the same repository

Having every single guide in a different repository is not the best approach to keep things easier to maintain neither for us, nor for the users willing to submit pull requests to improve the guides. Now every guide sits in a directory in the same repository and inside it, we have the code for the three languages. This way the text in the guides is the same and if there is a typo, and we fix it, it's fixed for all the versions of the guide.


### Guides for Gradle and Maven

As I've mentioned we love Gradle, and we use it intensively in Micronaut, but not all of our users use it, so we wanted to generate the guides also with Maven. Using Micronaut Launch infrastructure allows us to generate the same project with both Gradle and Maven in a very simple way. With this change instead of creating three versions of the guide (one per language) we will have six (one per language and build tool). This makes even more important to be able to generate new guides automatically and test them before new Micronaut releases.

Generating Maven projects for the guides has already paid the effort we've put into this. We've discovered and reported an issue in the Groovy compiler (that is fixed now) that only affects Maven users. To be fair I personally don't think that there will be any users out there using Groovy and Maven, but you never know...



### Simplify guides writing

With the current approach we defined the guide structure in a yaml file in which we describe the name of the Asciidoctor files we use for the guide and the different titles for every section. Then build process automatically process that yaml file and generates a unique Asciidoctor file that it's finally rendered as the HTML users can use. The problem is that it's not straight forward to have a clear picture of the complete guide because all the text is scattered across different files. We also have some common text snippets that we use in all the guides, but those snippets live in another repository, so it makes hard for us to maintain them and even harder for users to find them when they want to send improvements.

Now we write every guide as a single Asciidoctor file that include some text from common snippets that live in the same directory. This way we have a clear overview of the guide, and it's easier to find those snippets and modify them when needed.

This unique Asciidoctor file per guide also needs to be language and build tool agnostic, meaning that the same source file needs to generate all the six different combinations of guides. We have created custom _kind-of_ Asciidoctor macros that helps use to include code snippets in the guides and make things easy to write to make the guides agnostic to language and build tool. Let's see with an example:

Imagine the [Consul and Micronaut](https://guides.micronaut.io/micronaut-microservices-services-discover-consul/guide/index.html) guide. This guide has three microservices, and it is written in the three languages (remember, in other guides and repositories). To include `Book.java` from the `bookcatalogue` microservice in the guide, we do: 

```
[source,java]
.bookcatalogue/src/main/java/example/micronaut/bookcatalogue/Book.java
----
include::{sourceDir}/bookcatalogue/src/main/java/example/micronaut/bookcatalogue/Book.java[]
----
```

What about the same file but from the `bookrecommendation` microservice?

```
[source,java]
.bookrecommendation/src/main/java/example/micronaut/bookrecommendation/Book.java
----
include::{sourceDir}/bookrecommendation/src/main/java/example/micronaut/bookrecommendation/Book.java[]
----
```

And what about the same file but in the Groovy guide?

```
[source,groovy]
.bookrecommendation/src/main/groovy/example/micronaut/bookrecommendation/Book.groovy
----
include::{sourceDir}/bookrecommendation/src/main/groovy/example/micronaut/bookrecommendation/Book.groovy[]
----
```

Can you spot the problems? The first one is that to include the same class but from different languages we need different Asciidoctor snippets. And even to include the same file but from different microservices, we also need to modify the path. What about this?

```
source:Book[app=bookcatalogue]
```

We've written a custom macro that will expand the previous snippet into the right one taking care of the language and the application so it will generate any of the previously shown snippets appropriately for each case. We also have macros to include test files, resources, to define dependencies in a common way and render them differently for Gradle and Maven.

With this process we generate the guide in two steps. In the first one we pre-process the guide Asciidoctor file to "expand" our own macros and resolve and include our common snippets. This generates a temporary Asciidoctor file that is rendered to get the final HTML of the guide.


## Leveraging the Micronaut Community

As you can imagine migrating all the guides is a huge effort, and we possibly have made some mistakes. We want to ask the community to help us to review the new guides and improve or fix anything that is not correct or good enough. We have created [issues to review the guides](https://github.com/micronaut-projects/micronaut-guides-poc/issues?q=is%3Aissue+label%3Areview+) so if you want to help us, please read the generated guides, try them and see if everything makes sense. If you think a guide is good enough just put a _thumbs-up_ on their issue and if you find something, please comment directly in the issues.
We will raffle some Micronaut swag among all the people that help us doing the review.

Please go to the [work-in-progress index](https://micronaut-projects.github.io/micronaut-guides-poc/latest/) of the new guides and give them a try. We haven't finished the look and feel of them but that won't change the content. 


## Generating the guides locally

If you are interested in giving it a try locally, please clone [this repository](https://github.com/micronaut-projects/micronaut-guides-poc) and run `./gradlew build` to generate all the guides. You will find them in the `build/dist` directory.
