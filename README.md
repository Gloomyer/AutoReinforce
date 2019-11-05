## like:

将需要的demo下的命令的复制 保存到当前目录 任意名称.sh

修改对应路径,即可完成打包

参数说明（带*表示必填）

> -A (*)打包方式0:只打包,1:只加固,2:打包+加固

> -C (*)多渠道配置 可以多个

> -S (*)输出文件的保存目录

> -PP (*)项目路径

> -PB 指定打包项目分支,默认master

> -RTV (*)项目中预指的替换字符串

> -SCMD (*)签名工具全路径

> -SSF (*)签名全路径

> -SSP (*)签名密码

> -SKA (*)签名别名

> -SKP (*)签名别名密码

## demos

### 打多个渠道包到指定目录

```shell
java -jar pack2.0.0.jar \
-A 0 \
-C ructrip \
-C miui \
-C flyme \
-C tencent \
-C huawei \
-C qh360 \
-C oppo \
-C vivo \
-S /Users/gloomy/Downloads/autos \
-PP /Users/gloomy/Projects/rucheng-android \
-PB 1.0.3 \
-RTV NOT_CONFIGURED_CHANNEL_VALUE \
-SCMD /Users/gloomy/Library/Android/sdk/build-tools/28.0.3/apksigner \
-SSF /Users/gloomy/Projects/rucheng-android/keystore/kaistart.keystore \
-SSP kaishizhongchou. \
-SKA kaistart.keystore \
-SKP kaishizhongchou.
```