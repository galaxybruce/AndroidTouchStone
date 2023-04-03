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

### 9. uni小程序sdk能否集成HBuilderX 打包生成的App中
1. uni小程序sdk 仅支持在原生App中集成使用，不支持 HBuilderX 打包生成的App中集成。如需在uni-app项目中使用，请加QQ群984388064申请
2. uni小程序sdk 支持同时运行多个小程序实例，但android平台最多打开三个
3. uni 小程序 sdk 无法使用插件市场中付费的原生插件

### 10. 小程序和原生通信的方式
1. 小程序主动发消息给宿主，app全局监听，只能设置一次。
```
//小程序js层发送事件给宿主
uni.sendNativeEvent("event名称",a, function(e){
	console.log("sendNativeEvent-----------回调"+JSON.stringify(e));
});

//JAVA监听小程序发来的事件 通过callback返回参数
DCUniMPSDK.getInstance().setOnUniMPEventCallBack(new IOnUniMPEventCallBack() {
	@Override
	public void onUniMPEventReceive(String appid, String event, Object data, DCUniMPJSCallback callback) {
        callback.invoke( "测试数据");
	}
});
```
2. 宿主主动发消息给小程序
```
//JAVA原生层
JSONObject data = new JSONObject();
data.put("sj", "点击了关于");
IUniMP.sendUniMPEvent("gy", data);

//uni小程序JS代码 监听宿主触发给小程序的事件
<script>
	export default {
		onLoad() {
			uni.onNativeEventReceive((event,data)=>{
				console.log('接收到宿主App消息：' + event + data);
				this.nativeMsg = '接收到宿主App消息 event：' + event + " data: " + data;
			})
		}
	}
</script>
```
3. 扩展原生能力

