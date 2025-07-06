#!/bin/bash -e

. ../../include/depinfo.sh
. ../../include/path.sh

if [ "$1" == "build" ]; then
	true
elif [ "$1" == "clean" ]; then
	rm -rf local include libs
	exit 0
else
	exit 255
fi

builddir=$PWD

abi=armeabi-v7a
[[ "$ndk_triple" == "aarch64"* ]] && abi=arm64-v8a
[[ "$ndk_triple" == "x86_64"* ]] && abi=x86_64
[[ "$ndk_triple" == "i686"* ]] && abi=x86

# build using the NDK's scripts, but keep object files in our build dir
ndk-build -j$cores \
	NDK_PROJECT_PATH=. APP_BUILD_SCRIPT=Android.mk \
	APP_PLATFORM=android-26 APP_STL=c++_shared APP_ABI=$abi \
	NDK_APP_OUT="$builddir" NDK_APP_LIBS_OUT="$builddir/libs" \
	libshaderc_combined

cd "$builddir"
cp -r include/* "$prefix_dir/include"
cp libs/*/$abi/libshaderc.a "$prefix_dir/lib/libshaderc_combined.a"

# create a pkgconfig file
cat >"$prefix_dir"/lib/pkgconfig/shaderc_combined.pc <<END
prefix=/usr/local
includedir=\${prefix}/include
libdir=\${prefix}/lib

Name: shaderc_combined
Description: A collection of tools, libraries, and tests for Vulkan shader compilation.
Version: $v_shaderc
Libs: -L\${libdir} -lshaderc_combined
Cflags: -I\${includedir}
END