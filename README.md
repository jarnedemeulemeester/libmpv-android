# libmpv for Android

Based on [mpv-android](https://github.com/mpv-android/mpv-android)

## Building from source

Take a look at [README.md](buildscripts/README.md) inside the `buildscripts` directory.

The AAR bundles the following native libraries, which are built by the [buildscripts](buildscripts/README.md):

| Library | Version | License |
| ------- | ------- | ------- |
| mpv | 0.42.0 | GPL-2.0-or-later |
| FFmpeg | 9.0.2 | GPL-3.0-or-later |
| libplacebo | 7.387.0 | LGPL-2.1-or-later |
| libass | 0.17.5 | ISC |
| lua | 5.2.4 | MIT |
| curl | 8.23.0 | curl license (MIT-like) |
| mbedtls | 3.6.7 | Apache-2.0 |
| freetype | 2.15.0 | FreeType License (FTL) or GPL-2.0-or-later |
| fontconfig | 2.18.3 | fontconfig license (permissive) |
| fribidi | 1.0.18 | LGPL-2.1-or-later |
| harfbuzz | 15.1.0 | Old MIT |
| libunibreak | 8.0 | Zlib |
| dav1d | 1.5.4 | BSD-2-Clause |
| libxml2 | 2.15.4 | MIT |

`libmpv.so` and the FFmpeg libraries are bundled as shared libraries, the remaining libraries are statically linked into them. Versions are maintained in [`buildscripts/include/depinfo.sh`](buildscripts/include/depinfo.sh).

## Licensing

The source code in this repository is licensed under the [MIT license](LICENSE).

Since `libmpv.so` links against the FFmpeg libraries and other GPL-licensed components, the effective license of the binaries in the AAR is **GPL-3.0-or-later**. Apps distributing the AAR must comply with the terms of that license. See the table above for the licenses of the individual components.
