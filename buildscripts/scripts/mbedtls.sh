#!/bin/bash -e

. ../../include/depinfo.sh
. ../../include/path.sh

if [ "$1" == "build" ]; then
	true
elif [ "$1" == "clean" ]; then
	make clean
	exit 0
else
	exit 255
fi

$0 clean # separate building not supported, always clean

# since 3.6.6 Mbed TLS reads entropy from /dev/random on Android, which blocks on
# kernels older than 5.6 and can stall TLS handshakes for minutes
./scripts/config.py set MBEDTLS_PLATFORM_DEV_RANDOM '"/dev/urandom"'

make -j$cores no_test
make DESTDIR="$prefix_dir" install
