#!/bin/bash -e

PATCHES=(patches/*)
ROOT=$(pwd)

for dep_path in "${PATCHES[@]}"; do
    patches=($dep_path/*)
    dep=$(echo $dep_path |cut -d/ -f 2)
    cd deps/$dep
    echo Patching $dep
    git reset --hard
    for patch in "${patches[@]}"; do
        echo Applying $patch
        git apply "$ROOT/$patch"
    done
    cd $ROOT
done

exit 0
