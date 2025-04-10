#! /bin/bash

docker build -t libmpv-android .

docker run -it --rm -v ./aar-output:/output libmpv-android bash  -c "cp /libmpv/libmpv/build/outputs/aar/libmpv-release.aar /output/libmpv.aar"