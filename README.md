# KeepAlive
Android-AppDaemon
=================

App process daemon, used to keep your app alive.

Usage
=====
* This daemon can be added in application or service in your app, use `Daemon.run(context, daemonServiceClazz, intervalTime)` to run the daemon, then you can do something in onStartCommand of daemon service.
* You need to add `android:exported="true"` to your daemon service in manifest so that daemon can start up your daemon service.
* If you want to monitor the uninstall of app, see also [Android-AppUninstallWatcher][1].

Note
====
该库提供了三种唤醒方式
1. SyncAdapter  ,利用系统账号同步绑定的SyncService 的  进行唤醒。
2. Daemon 这个第三方库引用，保证了5.0以下大部分机型的保活，除小米机型外。 
3. 最新添加了JobService 这个计划循环任务，检查Daemon的存活 。 因为这个是21 出的api 基本上在5.0及以上有效。 


Final
====



License
=======

    Copyright (C) 2015-2016 samuelnotes

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://github.com/Coolerfall/Android-AppDaemon
