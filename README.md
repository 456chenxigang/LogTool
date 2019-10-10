## LogTool 是一个用来实时打印应用log到界面显示的工具库

### 效果
![screenshots](screenshots.gif)

### 引入library
- 在build.gradle中添加
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
以及添加dependency
```	
dependencies {
   	        implementation 'com.github.456chenxigang:LogTool:1.0'
   	}
```

### 使用
- 在MyApplication中初始化：
```
LogManager.getInstance().init(getApplicationContext());
```
- 控制log界面的打开与关闭
```
if (LogManager.getInstance().isShowing()){
         LogManager.getInstance().close();
     }else {
         LogManager.getInstance().show();
       }
```
- TAG过滤
```
LogManager.getInstance().setLogTag("MainActivity");
```