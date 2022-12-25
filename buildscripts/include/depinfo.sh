#!/bin/bash -e

## Dependency versions

v_sdk=9123335_latest
v_ndk=r25b
v_sdk_build_tools=33.0.1

v_lua=5.2.4
v_libass=0.17.0
v_harfbuzz=6.0.0
v_fribidi=1.0.12
v_freetype=2-12-1
v_mbedtls=2.28.2
v_libplacebo=5.229.1
v_dav1d=1.0.0
v_ffmpeg=5.1.2
v_mpv=0.35.0


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
dep_shaderc=()
dep_libplacebo=(shaderc)
dep_mpv=(ffmpeg libass lua libplacebo)
dep_mpv_android=(mpv)

