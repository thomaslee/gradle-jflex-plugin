# gradle-jflex-plugin

## Status

Beta

## Overview

A Gradle plugin for [JFlex](http://jflex.de), a scanner generator.

## Usage

First, install the plugin to your local repository using `gradle install`.
(I'll get this puppy up on Maven Central sometime soon.)

Then:

    apply plugin: 'java'
    apply plugin: 'jflex'

    buildscript {
        repositories {
            mavenLocal()
        }

        dependencies {
            classpath 'co.tomlee.gradle.plugins:gradle-jflex-plugin:0.0.1'
        }
    }

    dependencies {
        jflex 'de.jflex:jflex:1.6.0'
    }

Once everything's wired up, Gradle will look for your JFlex specs in
`src/main/beaver/*.l`.

## License

Apache 2.0

## Support

Please log defects and feature requests using the issue tracker on [github](http://github.com/thomaslee/gradle-jflex-plugin/issues).

## About

gradle-jflex-plugin was written by [Tom Lee](http://tomlee.co).

Follow me on [Twitter](http://www.twitter.com/tglee) or
[LinkedIn](http://au.linkedin.com/pub/thomas-lee/2/386/629).


