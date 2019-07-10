## LogTool 是一个用来实时打印应用log到界面显示的工具库

### 效果
![screenshots](screenshots.gif)

### 使用方法
- 在MyApplication中初始化：
```LogManager.getInstance().init(getApplicationContext());```
- 控制log界面的打开与关闭
```
if (LogManager.getInstance().isShowing()){
         LogManager.getInstance().close();
     }else {
         LogManager.getInstance().show();
       }
```