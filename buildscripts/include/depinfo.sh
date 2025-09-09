#!/bin/bash -e

## Dependency versions

v_platform=android-36
v_sdk=13114758_latest
v_ndk=28.2.13676358
v_sdk_build_tools=36.0.0
v_cmake=4.1.1

v_lua=5.2.4
v_libass=0.17.4
v_harfbuzz=11.4.5
v_fribidi=1.0.16
v_freetype=2-14-0
v_mbedtls=3.6.4
v_libplacebo=7.351.0
v_dav1d=1.5.1
v_ffmpeg=7.1.1
v_mpv=0.40.0


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

