#!/bin/bash -e

## Dependency versions

v_sdk=10406996_latest
v_ndk=26.1.10909125
v_sdk_build_tools=34.0.0

v_lua=5.2.4
v_libass=0.17.1
v_harfbuzz=8.3.0
v_fribidi=1.0.13
v_freetype=2-13-2
v_mbedtls=3.5.1
v_libplacebo=6.338.1
v_dav1d=1.3.0
v_ffmpeg=6.1
v_mpv=0.37.0


## Dependency tree
# I would've used a dict but putting arrays in a dict is not a thing

dep_mbedtls=()
dep_dav1d=()
dep_ffmpeg=(mbedtls dav1d)
dep_freetype2=()
dep_fribidi=()
dep_harfbuzz=()
dep_libass=(freetype fribidi harfbuzz)
dep_lua=()
dep_libplacebo=()
dep_mpv=(ffmpeg libass lua libplacebo)
dep_mpv_android=(mpv)

