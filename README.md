# Gradle Site Plugin

Gradle plugin providing helpful tools and configuration used to create and manage a project website.

TODO: add travis + coveralls

## Install

Build script snippet for use in all Gradle versions:

```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.stehno.gradle:site:0.0.1"
  }
}

apply plugin: "com.stehno.gradle.site"
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:

```groovy
plugins {
  id "com.stehno.gradle.site" version "0.0.1"
}
```

## Configuration

> TBD...

You may want to simplify your `site` task to run any reports or other pre-requisites by adding dependencies in your `build.gradle` file, such as:

```groovy
tasks.site.dependsOn = ['build', 'jacocoTestReport', 'groovydoc']
```

added preview config

## Tasks

> TBD...

site
verifySite
checkVersion
updateVersion

This should go in a user guide