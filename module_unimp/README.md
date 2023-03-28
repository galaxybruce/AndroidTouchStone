## uni小程序sdk集成注意点：
1. 把需要依赖的aar放到module_unimp/libs中
2. module_unimp/src/main/assets/data目录中三个文件原样复制过来
```
dcloud_control.xml
dcloud_error.html
dcloud_properties.xml
```
3. 相关build.gradle中的配置，在该项目中搜索"【uni小程序配置】"关键字
4. 内置小程序wgt资源，放在module_unimp/src/main/assets/apps目录中
5. 如何查看小程序中console.log日志
```
修改项目中assets/data/dcloud_control.xml 内部信息。将syncDebug改为true，开启调试模式。 注意正式版需要改为false!!!
修改后查看process:com.galaxybruce.touchstone进程查看log。TAG为console
```
6. uni小程序sdk 支持同时运行多个小程序实例，但android平台最多打开三个
```
从apk的manifest.xml中可以看到，这几个进程是内置的，不应该是4个吗？
process: com.galaxybruce.touchstone:unimp1
process: com.galaxybruce.touchstone:unimp2
process: com.galaxybruce.touchstone:unimp3
process: com.galaxybruce.touchstone:unimp4
```
