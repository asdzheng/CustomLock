##信任wifi##

![信任wifi][1]

这个app是一个系统工具，可以设置一个或多个我们信任的wifi热点(如家里的，公司里的)，设置成功后，只要手机连着的wifi是已信任的，以后点亮屏幕都不用先解锁。（暂时只支持隐藏图形锁）

### 屏幕截图 ###

![截图][2]

这个app只有一个界面，列出了你手机已经连接过的wifi热点，只要点击右边的按钮选中就可以。左边的图标是显示当前此wifi热点的信号强度。

###已知的bug###

由于隐藏系统锁屏界面已被官方标记为过时，有些定制的Android系统有修改过KeyGuardManager这个api,所以有些手机型号会不支持这app所提供的功能。(已知支持的手机有:小米手机，三星手机，华为系统3.0以下手机)

###使用控件###

本app的开关按钮使用开源控件：[ToggleButton][3]

###项目构建环境###

eclips

最新sdk

引用到的项目:appcompat，recyclerview，cardview

###License###
    Copyright 2015 asdzheng

    Licensed under the Apache License, Version 2.0 (the "License");you may not use this file except in compliance with the License.You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and limitations under the License.


---



  [1]: http://img.blog.csdn.net/20150503172344944?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYXNkemhlbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center
  [2]: http://img.blog.csdn.net/20150503172851890?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYXNkemhlbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center
  [3]: https://github.com/zcweng/ToggleButton
