#!/bin/bash -e

. ../../include/path.sh

if [ "$1" == "build" ]; then
	true
elif [ "$1" == "clean" ]; then
	rm -rf _build$ndk_suffix
	exit 0
else
	exit 255
fi

mkdir -p _build$ndk_suffix
cd _build$ndk_suffix

cpu=armv7-a
[[ "$ndk_triple" == "aarch64"* ]] && cpu=armv8-a
[[ "$ndk_triple" == "x86_64"* ]] && cpu=generic
[[ "$ndk_triple" == "i686"* ]] && cpu="i686 --disable-asm"

cpuflags=
[[ "$ndk_triple" == "arm"* ]] && cpuflags="$cpuflags -mfpu=neon -mcpu=cortex-a8"

../configure \
	--target-os=android --enable-cross-compile --cross-prefix=$ndk_triple- --cc=$CC \
	--arch=${ndk_triple%%-*} --cpu=$cpu --pkg-config=pkg-config \
	--extra-cflags="-I$prefix_dir/include $cpuflags" --extra-ldflags="-L$prefix_dir/lib" \
	--enable-{jni,mediacodec,mbedtls,libdav1d} --disable-vulkan \
	--disable-static --enable-shared --enable-{gpl,version3} \
	--disable-{stripping,doc,programs} \
	--disable-{muxers,encoders,devices,filters} \
	#--disable-decoders \
	#--enable-decoder={h264,h264_mediacodec,hevc,hevc_mediacodec,vp8,vp8_mediacodec,vp9,vp9_mediacodec,mpeg2video,mpeg2_mediacodec,mpeg4,mpeg4_mediacodec,av1_mediacodec,ssa,srt,dvdsub,webvtt,aac,flac,ac3,eac3,truehd,dca,opus,libdav1d,vorbis,subrip} \
	#--disable-demuxers \
	#--enable-demuxer={matroska,mov,avi,srt,ass,webvtt,hls,mpegts,mpegvideo} \
	#--disable-parsers \
	#--enable-parser={aac,ac3,flac,h264,hevc,dvdsub,opus,vp8,vp9,vorbis,mpegvideo}

make -j$cores
make DESTDIR="$prefix_dir" install
