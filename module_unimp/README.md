## uni小程序sdk集成注意点：
### 1. 把需要依赖的aar放到module_unimp/libs中

### 2. module_unimp/src/main/assets/data目录中三个文件原样复制过来
```
dcloud_control.xml
dcloud_error.html
dcloud_properties.xml
```

### 3. 相关build.gradle中的配置，在该项目中搜索"【uni小程序配置】"关键字

### 4. 内置小程序wgt资源，放在module_unimp/src/main/assets/apps目录中

### 5. 如何查看小程序中console.log日志
修改项目中assets/data/dcloud_control.xml 内部信息。将syncDebug改为true，开启调试模式。 注意正式版需要改为false!!!
修改后查看process:com.galaxybruce.touchstone进程查看log。TAG为console

### 6. uni小程序sdk 支持同时运行多个小程序实例，但android平台最多打开三个
从apk的manifest.xml中可以看到，这几个进程是内置的，不应该是4个吗？
```
process: com.galaxybruce.touchstone:unimp1
process: com.galaxybruce.touchstone:unimp2
process: com.galaxybruce.touchstone:unimp3
process: com.galaxybruce.touchstone:unimp4
```

### 7. 如何兼容现有的路由？
小程序中可以搞一个路由中转页面，所有的外部跳转先定向到该路由页面，里面再通过参数跳转到其他页面，
比如参数是base64(实际的页面路径)

### 8. 原生扩展逻辑需要与宿主交互
1. 可通过原生实现AIDL或者广播等等进行与宿主交互得到。玩法由开发者自行实现。
2. 可通过小程序与宿主通道进行交互数据。但缺点是仅支持小程序js层与宿主原生层交互。限制比较多