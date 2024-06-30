#!/bin/bash -e

## Dependency versions

v_sdk=11076708_latest
v_ndk=26.3.11579264
v_sdk_build_tools=35.0.0

v_lua=5.2.4
v_libass=0.17.2
v_harfbuzz=9.0.0
v_fribidi=1.0.15
v_freetype=2-13-2
v_mbedtls=3.5.2
v_libplacebo=6.338.2
v_dav1d=1.4.3
v_ffmpeg=7.0.1
v_mpv=0.38.0


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

