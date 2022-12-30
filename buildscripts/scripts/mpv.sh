#!/bin/bash -e

. ../../include/path.sh

build=_build$ndk_suffix

if [ "$1" == "build" ]; then
	true
elif [ "$1" == "clean" ]; then
	rm -rf $build
	exit 0
else
	exit 255
fi

unset CC CXX # meson wants these unset

meson setup $build --cross-file "$prefix_dir"/crossfile.txt \
  -Ddefault_library=shared \
  -Dcplayer=false \
  -Dlibmpv=true \
  -Dvulkan=disabled \
  -Diconv=disabled \
  -Dlua=lua \
  -Djpeg=disabled

meson compile -C $build -j$cores

DESTDIR="$prefix_dir" meson install -C $build

ln -sf "$prefix_dir"/lib/libmpv.so "$native_dir"
