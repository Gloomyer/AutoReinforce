## 参数说明:

> -A (*)打包方式0:只打包,1:只加固,2:打包+上传至蒲公英, 3:打包加加固

> -PAK 蒲公英ApiKey， 如果action != 2 这个不用填， 但是当action==2这个为必填

> -M 打包模式0:debug包，1:release包，默认0

> -C (*)多渠道配置 正常可以多个，但是当acton==2，渠道只允许配置一个,如果配置了多个将只会构建第一个

> -S (*)输出文件的保存目录

> -PP (*)项目路径

> -PB 指定打包项目分支,默认master

> -RTV (*)项目中预指的替换字符串

> -SCMD (*)签名工具全路径

> -SSF (*)签名全路径

> -SSP (*)签名密码

> -SKA (*)签名别名

> -SKP (*)签名别名密码

## Use Demos

选择下面你需要的方式 保存到当前目录 任意名称.sh

### 打多个Release渠道包到指定目录

```shell
java -jar pack2.0.0.jar \
-A 0 \
-M 1 \
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
-SSF 待替换 \
-SSP 待替换 \
-SKA 待替换 \
-SKP 待替换
```

### 打一个Debug包并且提交到蒲公英平台
```shell
java -jar pack2.0.0.jar \
-A 2 \
-M 0 \
-PAG 替换为你的蒲公英ApiKey \
-C ructrip \
-S /Users/gloomy/Downloads/autos \
-PP /Users/gloomy/Projects/rucheng-android \
-PB 1.0.3 \
-RTV NOT_CONFIGURED_CHANNEL_VALUE \
-SCMD /Users/gloomy/Library/Android/sdk/build-tools/28.0.3/apksigner \
-SSF 待替换 \
-SSP 待替换 \
-SKA 待替换 \
-SKP 待替换
```

### 打一个Release包并且提交到蒲公英平台
```shell
java -jar pack2.0.0.jar \
-A 2 \
-M 0 \
-PAG 替换为你的蒲公英ApiKey \
-C ructrip \
-S /Users/gloomy/Downloads/autos \
-PP /Users/gloomy/Projects/rucheng-android \
-PB 1.0.3 \
-RTV NOT_CONFIGURED_CHANNEL_VALUE \
-SCMD /Users/gloomy/Library/Android/sdk/build-tools/28.0.3/apksigner \
-SSF 待替换 \
-SSP 待替换 \
-SKA 待替换 \
-SKP 待替换
```