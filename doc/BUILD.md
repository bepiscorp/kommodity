# Building Callisto

## Build tool

The project is built with Gradle 7.4+.

The project is built using [Gradle 7.4+](https://gradle.org/releases).
There is no need to install _Gradle_ manually as _Castor_ build uses _Gradle Wrapper_

_[Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html)_ is a script that invokes
a declared version of _Gradle_, downloading it beforehand if necessary.
As a result, developers can get up and running with a _Gradle_ project quickly
without having to follow manual installation processes.

Run Gradle to build the project and to run the tests using the following command on Unix/macOS:
```shell
./gradlew <tasks-and-options>
```

... or the following command on Windows:

```shell
gradlew <tasks-and-options>
```

Examples in this document will rely on Unix/macOS environments.

## Building library artifacts

Execute the following command to build _Callisto library artifacts_:

```shell
./gradlew build
```

The outputs will be located in given locations (see spoiler):

<details>
<summary><b>Result of library artifacts build</b> (spoiler)</summary>

```text
.
└── build
    ├── api-commons
    │   └── libs
    │       ├── api-commons-{{version}}-javadoc.jar
    │       ├── api-commons-{{version}}-sources.jar
    │       ├── api-commons-{{version}}-test-fixtures.jar
    │       └── api-commons-{{version}}.jar
    ├── app-commons
    │   └── libs
    │       ├── app-commons-{{version}}-javadoc.jar
    │       ├── app-commons-{{version}}-sources.jar
    │       ├── app-commons-{{version}}-test-fixtures.jar
    │       └── app-commons-{{version}}.jar
    ├── base-commons
    │   └── libs
    │       ├── base-commons-{{version}}-javadoc.jar
    │       ├── base-commons-{{version}}-sources.jar
    │       ├── base-commons-{{version}}-test-fixtures.jar
    │       └── base-commons-{{version}}.jar
    ├── blob-commons
    │   ├── blob-commons-core
    │   │   ├── libs
    │   │   │   ├── blob-commons-core-{{version}}-javadoc.jar
    │   │   │   ├── blob-commons-core-{{version}}-sources.jar
    │   │   │   ├── blob-commons-core-{{version}}-test-fixtures.jar
    │   │   │   └── blob-commons-core-{{version}}.jar
    │   ├── blob-filesystem-extension
    │   │   ├── libs
    │   │   │   ├── blob-filesystem-extension-{{version}}-javadoc.jar
    │   │   │   ├── blob-filesystem-extension-{{version}}-sources.jar
    │   │   │   ├── blob-filesystem-extension-{{version}}-test-fixtures.jar
    │   │   │   └── blob-filesystem-extension-{{version}}.jar
    │   └── blob-hibernate-extension
    │       └── libs
    │           ├── blob-hibernate-extension-{{version}}-javadoc.jar
    │           ├── blob-hibernate-extension-{{version}}-sources.jar
    │           ├── blob-hibernate-extension-{{version}}-test-fixtures.jar
    │           └── blob-hibernate-extension-{{version}}.jar
    ├── data-commons
    │   └── libs
    │       ├── data-commons-{{version}}-javadoc.jar
    │       ├── data-commons-{{version}}-sources.jar
    │       ├── data-commons-{{version}}-test-fixtures.jar
    │       └── data-commons-{{version}}.jar
    ├── hibernate-commons
    │   └── libs
    │       ├── hibernate-commons-{{version}}-javadoc.jar
    │       ├── hibernate-commons-{{version}}-sources.jar
    │       ├── hibernate-commons-{{version}}-test-fixtures.jar
    │       └── hibernate-commons-{{version}}.jar
    ├── signal-commons
    │   ├── signal-commons-core
    │   │   └── libs
    │   │       ├── signal-commons-core-{{version}}-javadoc.jar
    │   │       ├── signal-commons-core-{{version}}-sources.jar
    │   │       ├── signal-commons-core-{{version}}-test-fixtures.jar
    │   │       └── signal-commons-core-{{version}}.jar
    │   └── signal-commons-unix
    │       └── libs
    │           ├── signal-commons-unix-{{version}}-javadoc.jar
    │           ├── signal-commons-unix-{{version}}-sources.jar
    │           ├── signal-commons-unix-{{version}}-test-fixtures.jar
    │           └── signal-commons-unix-{{version}}.jar
    └── test-commons
        └── libs
            ├── test-commons-{{version}}-javadoc.jar
            ├── test-commons-{{version}}-sources.jar
            ├── test-commons-{{version}}-test-fixtures.jar
            └── test-commons-{{version}}.jar
```

</details>

## Building Gradle artifacts

Execute the following command to build _Callisto Gradle artifacts_:

```shell
./gradlew build \
    :callisto-gradle:version-catalog:generateCatalogAsToml \
    :callisto-gradle:tyrell-conventions:build \
    :callisto-gradle:plugins:base:build \
    :callisto-gradle:plugins:java:build \
    :callisto-gradle:plugins:java-library:build \
    :callisto-gradle:plugins:kotlin:build \
    :callisto-gradle:plugins:kotlin-library:build
```

The outputs will be located in given locations:

<details>
<summary><b>Result of Gradle artifacts build</b> (spoiler)</summary>

```text
.
└── callisto-gradle
    ├── plugins
    │   ├── base
    │   │   ├── build
    │   │   │   ├── libs
    │   │   │   │   ├── base-{{version}}-javadoc.jar
    │   │   │   │   ├── base-{{version}}-sources.jar
    │   │   │   │   ├── base-{{version}}-test-fixtures.jar
    │   │   │   │   └── base-{{version}}.jar
    │   ├── java
    │   │   ├── build
    │   │   │   ├── libs
    │   │   │   │   ├── java-{{version}}-javadoc.jar
    │   │   │   │   ├── java-{{version}}-sources.jar
    │   │   │   │   └── java-{{version}}.jar
    │   ├── java-library
    │   │   ├── build
    │   │   │   ├── libs
    │   │   │   │   ├── java-library-{{version}}-javadoc.jar
    │   │   │   │   ├── java-library-{{version}}-sources.jar
    │   │   │   │   └── java-library-{{version}}.jar
    │   ├── kotlin
    │   │   ├── build
    │   │   │   ├── libs
    │   │   │   │   ├── kotlin-{{version}}-javadoc.jar
    │   │   │   │   ├── kotlin-{{version}}-sources.jar
    │   │   │   │   └── kotlin-{{version}}.jar
    │   └── kotlin-library
    │       │   ├── libs
    │       │   │   ├── kotlin-library-{{version}}-javadoc.jar
    │       │   │   ├── kotlin-library-{{version}}-sources.jar
    │       │   │   └── kotlin-library-{{version}}.jar
    ├── tyrell-conventions
    │   │   ├── libs
    │   │   │   ├── tyrell-conventions-{{version}}-javadoc.jar
    │   │   │   ├── tyrell-conventions-{{version}}-sources.jar
    │   │   │   └── tyrell-conventions-{{version}}.jar
    └── version-catalog
            └── version-catalog
                └── libs.versions.toml
```

</details>

---

# Publishing

Artifacts of Callisto can be published into:
- Maven Local (`$HOME/.m2/repository/`)
- [Cybernation Snapshots][cybernation-snapshots] in Tyrell Nexus
- [Cybernation Releases][cybernation-releases] in Tyrell Nexus

## Publishing to _Maven Local_

Publish _library artifacts_ to _Maven Local_:

```shell
./gradlew publishToMavenLocal
`````

Publish _Gradle artifacts_ to _Maven Local_:

```shell
./gradlew \
    :callisto-gradle:version-catalog:publishToMavenLocal \
    :callisto-gradle:tyrell-conventions:publishToMavenLocal \
    :callisto-gradle:plugins:base:publishToMavenLocal \
    :callisto-gradle:plugins:java:publishToMavenLocal \
    :callisto-gradle:plugins:java-library:publishToMavenLocal \
    :callisto-gradle:plugins:kotlin:publishToMavenLocal \
    :callisto-gradle:plugins:kotlin-library:publishToMavenLocal
```

## Publishing to remote repository

If `pre-release` term (in terms of [Semantic Versioning 2.0.0][semantic versioning]) of current project version
equals to `SNAPSHOT` then artifacts will be published to _[Cybernation Snapshots][cybernation-snapshots]_,
otherwise artifacts will be published to _[Cybernation Releases][cybernation-releases]_.

Publish _library artifacts_ to remote repository:

```shell
./gradlew publish
```

Publish _Gradle artifacts_ to remote repository:

```shell
./gradlew \
    :callisto-gradle:version-catalog:publish \
    :callisto-gradle:tyrell-conventions:publish\ 
    :callisto-gradle:plugins:base:publish \ 
    :callisto-gradle:plugins:java:publish \ 
    :callisto-gradle:plugins:java-library:publish \
    :callisto-gradle:plugins:kotlin:publish \
    :callisto-gradle:plugins:kotlin-library:publish
```

[cybernation-snapshots]: https://nexus.cybernation.com/#browse/browse:cybernation-snapshots
[cybernation-releases]: https://nexus.cybernation.com/#browse/browse:cybernation-releases
[semantic versioning]: https://semver.org/spec/v2.0.0.html
