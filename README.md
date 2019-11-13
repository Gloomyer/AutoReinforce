## 构建说明
项目通过apktools 来逆向生成的包，然后动态修改渠道实现快速打包。

实测8个渠道7分钟提升至只需要192秒(3分钟12秒)

需要对项目作出如下配置.

我们的项目是和友盟配合，所以现在manifest文件中加入如下字段:

```xml
<meta-data
    android:name="UMENG_CHANNEL"
    android:value="NOT_CONFIGURED_CHANNEL_VALUE" />
```

value里面的字段需要记住.

然后在app model下的gradle文件中修改apk名称生成规则.
```groovy
applicationVariants.all { variant ->
    variant.outputs.all { output ->
        //outputFileName = "${variant.productFlavors[0].name}"+
        outputFileName = "NOT_CONFIGURED_CHANNEL_VALUE"+
        "@name_${defaultConfig.versionName}"+
        "@code_${defaultConfig.versionCode}"+
        "@${isRelease ? 'Release' : 'Debug'}.apk"
    }
}
```

上面的规则可以自定义，但是NOT_CONFIGURED_CHANNEL_VALUE请保留。

build文件中的NOT_CONFIGURED_CHANNEL_VALUE和manifest中的NOT_CONFIGURED_CHANNEL_VALUE

可以换成自己想要的，但是请保证两者的一致性。

然后在运行程序的时候将这个值传递给我。 利用-RTV参数 具体请看参数说明和use demos

如果涉及到上传至七牛/阿里云..最后会在保存目录下生成result.txt保存文件的下载地址

## app-host

这两天蒲公英炸了，公司没有申请fir fir认证极其麻烦。

和运维沟通之后，帮忙搭建了一个内网app分发平台 [App-Host](https://github.com/pluosi/app-host) 

自动构建工具开始支持app-host提交

## 参数说明:

> -A (*)(action)打包方式0:只打包,1:只加固,2:打包+上传至蒲公英, 3:打包加加固 4:打包+上传app-host

> -PAK (pgy api key)蒲公英ApiKey， 如果action != 2 这个不用填， 但是当action==2这个为必填

> -M (model)打包模式0:debug包，1:release包，默认0

> -C (*)(channel)多渠道配置 正常可以多个，但是当acton==2，渠道只允许配置一个,如果配置了多个将只会构建第一个

> -S (*)(save)输出文件的保存目录

> -PP (*)(project path)项目路径

> -PB (project branch)指定打包项目分支,默认当前分支,不进行切换分支行为

> -RTV (*)(replace text value)项目中预指的替换字符串

> -SCMD (*)(sign cmd)签名工具全路径

> -SSF (*)(sign store file)签名全路径

> -SSP (*)(sign store password)签名密码

> -SKA (*)(sign key alias)签名别名

> -SKP (*)(sign keyAlias password)签名别名密码

> -PM (protectionMethod) 加固方式，非必填，默认legu 支持2个参数[legu,qihoo]

> -PSI (protection secretid) 如果action==3 || action ==1 并且pm==空或者pm==legu 这个为必填，乐固secretId

> -PSK (protection secretKey) 如果action==3 || action ==1 并且pm==空或者pm==legu 这个为必填，乐固secretKey

> -QHUN (qihoo username) 如果action==3 || action ==1 并且pm==qihoo 这个为必填 qihoo 360 账户

> -QHPW (qihoo password) 如果action==3 || action ==1 并且pm==qihoo 这个为必填 qihoo 360 密码

> -AHU (app-host url) app-host post 提交地址 action==4 必填

> -AHT (app-host token) app-host post 提交token action==4 必填

> -AHPI (app-host plat id) app-host post 提交plat id action==4 必填
## Use Demos

选择下面你需要的方式 保存到当前目录 任意名称.sh

### 打多个Release渠道包到指定目录

```shell script
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
```shell script
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

### 打一个Debug包并且提交到app-host
```shell script
java -jar pack2.0.0.jar \
-A 4 \
-M 0 \
-PAG 替换为你的蒲公英ApiKey \
-C ructrip \
-S /Users/gloomy/Downloads/autos \
-PP /Users/gloomy/Projects/rucheng-android \
-RTV NOT_CONFIGURED_CHANNEL_VALUE \
-SCMD /Users/gloomy/Library/Android/sdk/build-tools/28.0.3/apksigner \
-SSF 待替换 \
-SSP 待替换 \
-SKA 待替换 \
-SKP 待替换 \
-AHU 待替换 \
-AHT 待替换 \
-AHPI 待替换
```

### 打一个Release包并且提交到蒲公英平台
```shell script
java -jar pack2.0.0.jar \
-A 2 \
-M 1 \
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


### 打正式库多个包并且加固，使用360加固
```shell script
java -jar pack2.0.0.jar \
-A 3 \
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
-PB master \
-RTV NOT_CONFIGURED_CHANNEL_VALUE \
-SCMD /Users/gloomy/Library/Android/sdk/build-tools/28.0.3/apksigner \
-SSF 待替换 \
-SSP 待替换 \
-SKA 待替换 \
-SKP 待替换 \
-PM qihoo \
-QHUN 360账户 \
-QHPW 360密码
```

### 打正式库多个包并且加固，使用乐固
```shell script
java -jar pack2.0.0.jar \
-A 3 \
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
-PB master \
-RTV NOT_CONFIGURED_CHANNEL_VALUE \
-SCMD /Users/gloomy/Library/Android/sdk/build-tools/28.0.3/apksigner \
-SSF 待替换 \
-SSP 待替换 \
-SKA 待替换 \
-SKP 待替换 \
-PM legu \
-PSI 乐固secretId \
-PSK 乐固secretKey
```