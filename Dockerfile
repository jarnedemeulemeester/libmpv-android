FROM mingc/android-build-box

RUN apt update && apt upgrade -y

RUN apt install -y git curl wget sudo

COPY ./ /libmpv

WORKDIR /libmpv/buildscripts

RUN bash download.sh

RUN bash patch.sh

RUN bash build.sh mpv-android