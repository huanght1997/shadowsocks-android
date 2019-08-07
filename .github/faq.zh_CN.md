### 故障排除

无法连接到服务器：

1. 如果省电模式启用，将其停用；
2. 检查您的配置；
3. 清除应用数据。

应用崩溃：[提交issue](https://github.com/shadowsocks/shadowsocks-android/issues/new)，附带上logcat日志报告，或者向Google Play提交崩溃报告。然后，尝试清除应用数据。

### 如何创建小工具或者基于网络连接切换配置？

使用[Tasker](http://tasker.dinglisch.net/)集成。

### NAT模式为何被废弃？

1. 需要ROOT权限；
2. 不支持IPv6；
3. 不支持UDP转发。

### 在使用VPN模式时如何移除感叹号标记？

在无线网络/数据流量图标处出现的感叹号标记，是因为系统在无VPN连接下无法连接到入口服务器（默认为`clients3.google.com`）。要移除它，按照[这篇文章](http://www.noisyfox.cn/45.html)的指导进行。

### 为什么不支持我的ROM？

1. 一些ROM的VPNService的实现是不完整的，尤其是对IPv6的支持；
2. 一些ROM有着激进的或者不完整的后台服务清理策略；
3. 一些ROM如[Flyme](https://github.com/shadowsocks/shadowsocks-android/issues/1821)在**各个方面**的实现都不好；
4. 如果您安装了Xposed框架或者电量节约应用，此应用可能无法和它们在一起很好地工作。
   
* 针对MIUI的修复： [#772](https://github.com/shadowsocks/shadowsocks-android/issues/772)
* 针对EMUI的修复： [#888](https://github.com/shadowsocks/shadowsocks-android/issues/888)
* 针对华为手机的修复： [#1091 (comment)](https://github.com/shadowsocks/shadowsocks-android/issues/1091#issuecomment-276949836)
* Xposed框架相关： [#1414](https://github.com/shadowsocks/shadowsocks-android/issues/1414)
* Samsung and/or Brevent: [#1410](https://github.com/shadowsocks/shadowsocks-android/issues/1410)
* 另一系列三星手机： [#1712](https://github.com/shadowsocks/shadowsocks-android/issues/1712)
* 安装有GMS的三星手机： [#2138](https://github.com/shadowsocks/shadowsocks-android/issues/2138)
* 由于权限问题请勿将此应用安装在SD卡上： [#1124 (comment)](https://github.com/shadowsocks/shadowsocks-android/issues/1124#issuecomment-307556453)
* `INTERACT_ACROSS_USERS`权限缺失： [#1184](https://github.com/shadowsocks/shadowsocks-android/issues/1184)

### 如何暂停Shadowsocks服务？

* 对于Android 7.0+：使用快速设置中的快速开关块；
* 使用Tasker集成；
* 添加一个配置文件，每个应用的代理只打开Shadowsocks，绕过模式关闭。

### 在Android 5.0+上，为什么Shadowsocks耗电如此之高？

由于Shadowsocks接管了整个设备的网络连接，所有其他应用因为网络活动产生的电量消耗都会同样计算到Shadowsocks上。因此，Shadowsocks的电量使用是您设备中所有网络活动的耗电量之和。Shadowsocks应用本身仅仅是一个应用于现代Android设备的I/O控制应用，本身不应会耗费过多电量。

如果您在使用Shadowsocks后发现电池使用的显著上升，这很有可能是其他应用所导致的。比如，Google Play服务在可以连接到Google后会消耗更多电量。

更多细节：https://kb.adguard.com/en/android/solving-problems/battery

### 应用在WiFi环境下工作正常但在数据流量环境下无法连接？

请在应用设置中允许此应用使用数据流量。

### 如何使用透明代理模式？

1. 安装[AFWall+](https://github.com/ukanth/afwall)；
2. 设置自定义脚本：
```sh
IP6TABLES=/system/bin/ip6tables
IPTABLES=/system/bin/iptables
ULIMIT=/system/bin/ulimit
SHADOWSOCKS_UID=`dumpsys package com.github.shadowsocks | grep userId | cut -d= -f2 - | cut -d' ' -f1 -`
PORT_DNS=5450
PORT_TRANSPROXY=8200
$ULIMIT -n 4096
$IP6TABLES -F
$IP6TABLES -A INPUT -j DROP
$IP6TABLES -A OUTPUT -j DROP
$IPTABLES -t nat -F OUTPUT
$IPTABLES -t nat -A OUTPUT -o lo -j RETURN
$IPTABLES -t nat -A OUTPUT -d 127.0.0.1 -j RETURN
$IPTABLES -t nat -A OUTPUT -m owner --uid-owner $SHADOWSOCKS_UID -j RETURN
$IPTABLES -t nat -A OUTPUT -p tcp --dport 53 -j DNAT --to-destination 127.0.0.1:$PORT_DNS
$IPTABLES -t nat -A OUTPUT -p udp --dport 53 -j DNAT --to-destination 127.0.0.1:$PORT_DNS
$IPTABLES -t nat -A OUTPUT -p tcp -j DNAT --to-destination 127.0.0.1:$PORT_TRANSPROXY
$IPTABLES -t nat -A OUTPUT -p udp -j DNAT --to-destination 127.0.0.1:$PORT_TRANSPROXY
```
3. 设置自定义停用脚本：
```sh
IP6TABLES=/system/bin/ip6tables
IPTABLES=/system/bin/iptables
$IPTABLES -t nat -F OUTPUT
$IP6TABLES -F
```
4. 确认允许Shadowsocks的流量通过；
5. 启动Shadowsocks透明代理服务并启用防火墙。
