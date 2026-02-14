#!/bin/bash -e

## Dependency versions

v_platform=android-36
v_sdk=13114758_latest
v_ndk=29.0.14206865
v_sdk_build_tools=36.1.0
v_cmake=4.1.2

v_lua=5.2.4
v_libunibreak=6.1
v_libass=0.17.4
v_harfbuzz=12.3.2
v_fribidi=1.0.16
v_freetype=2.14.1
v_mbedtls=3.6.5
v_libplacebo=7.360.0
v_dav1d=1.5.3
v_ffmpeg=8.0.1
v_mpv=0.41.0


## Dependency tree
# I would've used a dict but putting arrays in a dict is not a thing

dep_mbedtls=()
dep_dav1d=()
dep_ffmpeg=(mbedtls dav1d)
dep_freetype2=()
dep_fribidi=()
dep_harfbuzz=()
dep_libunibreak=()
dep_libass=(freetype fribidi harfbuzz libunibreak)
dep_lua=()
dep_libplacebo=()
dep_mpv=(ffmpeg libass lua libplacebo)
dep_mpv_android=(mpv)

