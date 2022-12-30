cmake_minimum_required(VERSION 3.22.1)

project("libmpv")

add_library(
        player
        SHARED
        main.cpp render.cpp log.cpp jni_utils.cpp property.cpp event.cpp
)

add_library(
        mpv
        SHARED
        IMPORTED
)

set_target_properties(
        mpv
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libmpv.so
)

add_library(
        avcodec
        SHARED
        IMPORTED
)

set_target_properties(
        avcodec
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/../../../../buildscripts/prefix/${ANDROID_ABI}/lib/libavcodec.so
)

include_directories( ${CMAKE_CURRENT_SOURCE_DIR}/../../../../buildscripts/prefix/${ANDROID_ABI}/include )

target_link_libraries( player mpv avcodec log )