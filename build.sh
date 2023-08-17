#!/bin/bash -e

PROJECT_DIR="$PWD"
DESTINATION_DIR="$PROJECT_DIR/callisto"
if [[ ! -d $DESTINATION_DIR ]]; then
    mkdir -p $DESTINATION_DIR
fi

function log() {
    yel=$'\e[1;33m'
    end=$'\e[0m'
    message=$1
    printf "\n${yel}$message${end}\n"
}

function copy_built_jars() {
    outputs_rel_path=$1
    dest_rel_path=$2
    
    dest_dir="$DESTINATION_DIR/$dest_rel_path"
    if [[ ! -d $dest_dir ]]; then mkdir -p $dest_dir; fi

    module_outputs_dir="$PROJECT_DIR/$outputs_rel_path"
    libs_path="$module_outputs_dir/libs"
    version_pattern="*$VERSION*"

    find $libs_path \
        -type f -name '*.jar' -name $version_pattern \
        -exec cp {} $dest_dir \;
}


log "Assigning Callisto version"
props_version=$(egrep "^version=" gradle.properties | cut -d'=' -f2)
input_version=$1
if [[ ! -z "$input_version" ]]; then
    VERSION="$input_version"
    log "Assigned version from input: $VERSION"
else
    VERSION="${props_version}"
    log "Assigned version from 'gradle.properties': $VERSION"
fi


if [[ $VERSION != $props_version ]]; then
    log "Replacing versions in project to '$VERSION'"

    # Replace version in 'gradle.properties' files
    find $PROJECT_DIR \
        -type f -name 'gradle.properties' \
        -exec sed -i '' "s/version=$props_version/version=$VERSION/g" {} \;

    # Replace version in Callisto version catalog
    sed -i '' "s/callisto = \"$props_version\"/callisto = \"$VERSION\"/g" $PROJECT_DIR/gradle/libs.versions.toml
fi


log "Building Callisto Gradle artifacts"
./gradlew -Pcallisto.projectVersion=$VERSION \
    :callisto-gradle:version-catalog:generateCatalogAsToml \
    :callisto-gradle:tyrell-conventions:build \
    :callisto-gradle:plugins:base:build \
    :callisto-gradle:plugins:java:build \
    :callisto-gradle:plugins:java-library:build \
    :callisto-gradle:plugins:kotlin:build \
    :callisto-gradle:plugins:kotlin-library:build


log "Building Callisto library artifacts"
./gradlew build -Pcallisto.projectVersion=$VERSION

if [[ $VERSION != $props_version ]]; then
    log "Replacing versions in project back to '$props_version'"

    # Replace version in 'gradle.properties' files
    find $PROJECT_DIR \
        -type f -name 'gradle.properties' \
        -exec sed -i '' "s/version=$VERSION/version=$props_version/g" {} \;

    # Replace version in Callisto version catalog
    sed -i '' "s/callisto = \"$VERSION\"/callisto = \"$props_version\"/g" $PROJECT_DIR/gradle/libs.versions.toml
fi


log "Copying Callisto Gradle artifacts"
mkdir -p $DESTINATION_DIR/callisto-gradle/version-catalog/build/version-catalog \
    && mv \
        $PROJECT_DIR/callisto-gradle/version-catalog/build/version-catalog/libs.versions.toml \
        $DESTINATION_DIR/callisto-gradle/version-catalog/
copy_built_jars "callisto-gradle/tyrell-conventions/build" "callisto-gradle/tyrell-conventions"
copy_built_jars "callisto-gradle/plugins/base/build" "callisto-gradle/plugins/base"
copy_built_jars "callisto-gradle/plugins/java/build" "callisto-gradle/plugins/java"
copy_built_jars "callisto-gradle/plugins/java-library/build" "callisto-gradle/plugins/java-library"
copy_built_jars "callisto-gradle/plugins/kotlin/build" "callisto-gradle/plugins/kotlin"
copy_built_jars "callisto-gradle/plugins/kotlin-library/build" "callisto-gradle/plugins/kotlin-library"


log "Copying Callisto library artifacts"
copy_built_jars "build/base-commons" "base-commons"
copy_built_jars "build/test-commons" "test-commons"
copy_built_jars "build/data-commons" "data-commons"
copy_built_jars "build/signal-commons/signal-commons-core" "signal-commons/signal-commons-core"
copy_built_jars "build/signal-commons/signal-commons-unix" "signal-commons/signal-commons-unix"
copy_built_jars "build/hibernate-commons" "hibernate-commons"
copy_built_jars "build/blob-commons/blob-commons-core" "blob-commons/blob-commons-core"
copy_built_jars "build/blob-commons/blob-filesystem-extension" "blob-commons/blob-filesystem-extension"
copy_built_jars "build/blob-commons/blob-hibernate-extension" "blob-commons/blob-hibernate-extension"
copy_built_jars "build/api-commons" "api-commons"
copy_built_jars "build/app-commons" "app-commons"


log "Creating distribution archive"
archive_name="callisto-$VERSION"
archive_filename="$archive_name.tar.gz"

cd $(dirname $DESTINATION_DIR)
tar czf $archive_filename $(basename $DESTINATION_DIR)
cd -


log "Cleaning Callisto build directories distribution archive"
rm -r $DESTINATION_DIR
rm -rf $DESTINATION_DIR/callisto-gradle/version-catalog/build
./gradlew clean \
    :callisto-gradle:tyrell-conventions:clean \
    :callisto-gradle:plugins:base:clean \
    :callisto-gradle:plugins:java:clean \
    :callisto-gradle:plugins:java-library:clean \
    :callisto-gradle:plugins:kotlin:clean \
    :callisto-gradle:plugins:kotlin-library:clean
