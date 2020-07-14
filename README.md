# Micronaut Website

[![Build Status](https://github.com/micronaut-projects/static-website/workflows/Publish/badge.svg)](https://github.com/micronaut-projects/static-website/actions)

This project builds the Micronaut website. A static website build with [Gradle](https://gradle.org).

## What to change when a new release is published.

Please, modify `main/src/resources/releases.yml`

## Generating the MAIN site

[https://micronaut.io](https://micronaut.io)

```bash
./gradlew build
```

The output can be found in the `build/dist` directory.

# Generating the GUIDES site

[https://guides.micronaut.io](http://guides.micronaut.io)

```bash
./gradlew buildGuide
```

The output can be found in the `build/dist` directory.

## Generate Micronaut Launch Front end

Checkout in `launch` branch.
