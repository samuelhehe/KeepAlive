# KeepAlive
====

从以前开发的项目中整理出来的一种三角调用式保活机制，能够根据不同的系统版本进行选择性适配

Usage
=====

* 可以按照该库的引用方式添加到自己的项目中， 启动SyncService 保证自己的Service保活。 
* 可以使用检查调用进程 ，提高推送到达率。

Note
====
该库提供了三种唤醒方式
1. SyncAdapter  ,利用系统账号同步绑定的SyncService 的  进行唤醒。
2. Daemon 这个第三方库引用，保证了5.0以下大部分机型的保活，除小米机型外。 注意添加export=true ，在DaemonService Manifest.xml 文件中
3. 最新添加了JobService 这个计划循环任务，检查Daemon的存活 。 因为这个是21出的api 基本上在5.0及以上有效。 

Final
====

这个库最初是从以前开发的项目中为了提高第三方推送sdk的推送到达率而设计的一种保活方式，有一定的作用，但也有一定的不足之处。
这里简单提取出来， 使用的时候可以根据系统版本来做适当的配置，选择性启用保活Service。 

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
