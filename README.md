## Shadowsocks for Android

[![CircleCI](https://circleci.com/gh/huanght1997/shadowsocks-android.svg?style=shield)](https://circleci.com/gh/huanght1997/shadowsocks-android)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![GitHub repo size](https://img.shields.io/github/repo-size/huanght1997/shadowsocks-android.svg)](https://github.com/huanght1997/shadowsocks-android/archive/master.zip)
[![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/huanght1997/shadowsocks-android.svg)](https://github.com/huanght1997/shadowsocks-android/releases)
[![Language: Kotlin](https://img.shields.io/github/languages/top/huanght1997/shadowsocks-android.svg)](https://github.com/huanght1997/shadowsocks-android/search?l=kotlin)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ff80169fd6d548d8a7a293535e66e837)](https://www.codacy.com/app/huanght1997/shadowsocks-android?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=huanght1997/shadowsocks-android&amp;utm_campaign=Badge_Grade)
[![License: GPL-3.0](https://img.shields.io/badge/license-GPL--3.0-orange.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fhuanght1997%2Fshadowsocks-android.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fhuanght1997%2Fshadowsocks-android?ref=badge_shield)


### PREREQUISITES

* JDK 1.8
* Android SDK
  - Android NDK

### BUILD

You can check whether the latest commit builds under UNIX environment by checking Travis status.

* Clone the repo using `git clone --recurse-submodules <repo>` or update submodules using `git submodule update --init --recursive`
* Build it using Android Studio or gradle script

### BUILD WITH DOCKER

```bash
mkdir build
sudo chown 3434:3434 build
docker run --rm -v ${PWD}/build:/build circleci/android:api-28-ndk bash -c "cd /build; git clone https://github.com/shadowsocks/shadowsocks-android; cd shadowsocks-android; git submodule update --init --recursive; ./gradlew assembleDebug"
```

### CONTRIBUTING

If you are interested in contributing or getting involved with this project, please read the CONTRIBUTING page for more information.  The page can be found [here](https://github.com/shadowsocks/shadowsocks-android/blob/master/CONTRIBUTING.md).


### [TRANSLATE](https://discourse.shadowsocks.org/t/poeditor-translation-main-thread/30)

## OPEN SOURCE LICENSES

<ul>
    <li>redsocks: <a href="https://github.com/shadowsocks/redsocks/blob/shadowsocks-android/README">APL 2.0</a></li>
    <li>mbed TLS: <a href="https://github.com/ARMmbed/mbedtls/blob/development/LICENSE">APL 2.0</a></li>
    <li>libevent: <a href="https://github.com/shadowsocks/libevent/blob/master/LICENSE">BSD</a></li>
    <li>tun2socks: <a href="https://github.com/shadowsocks/badvpn/blob/shadowsocks-android/COPYING">BSD</a></li>
    <li>pcre: <a href="https://android.googlesource.com/platform/external/pcre/+/master/dist2/LICENCE">BSD</a></li>
    <li>libancillary: <a href="https://github.com/shadowsocks/libancillary/blob/shadowsocks-android/COPYING">BSD</a></li>
    <li>shadowsocks-libev: <a href="https://github.com/shadowsocks/shadowsocks-libev/blob/master/LICENSE">GPLv3</a></li>
    <li>libev: <a href="https://github.com/shadowsocks/libev/blob/master/LICENSE">GPLv2</a></li>
    <li>libsodium: <a href="https://github.com/jedisct1/libsodium/blob/master/LICENSE">ISC</a></li>
</ul>


[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fhuanght1997%2Fshadowsocks-android.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fhuanght1997%2Fshadowsocks-android?ref=badge_large)

### LICENSE

Copyright (C) 2017 by Max Lv <<max.c.lv@gmail.com>>  
Copyright (C) 2017 by Mygod Studio <<contact-shadowsocks-android@mygod.be>>
Copyright (C) 2019 by Haitao Huang
<<hht970222@gmail.com>>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
