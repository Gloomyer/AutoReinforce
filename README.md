## anadroid 自动打包加固工具
### 简介
Android繁琐的渠道包加固流程极其的浪费时间。

所以决定写一个工具来完成这个事情。

目前加固方式只支持乐加固。

存储支持七牛&aliyun aliyun没有测试过。 手上没有alioss

### 时间测试
# 测试结果对比
## 自动加固
启动时间:15:11:39
8个渠道包&自动加固 过程...
结束时间:15.24:37
耗时:12分钟58秒(778s)


## 手动打包
开始clean&打普通包:15.35:18
结束普通包:17:39:46
耗时:4分钟28秒

加固开始时间:17:41:31
加固结束时间:17:52:48
耗时:11分钟27秒

上传开始时间:17:53:48
上传结束时间:17:55:54
耗时:2分钟6秒

二维码生成时间:17:56:42
二维码生成时间:18:00:56
耗时:4分钟14秒

手动打包共耗时:22分钟15秒(1335s)

## 结果
时间效率提升: 自动打包只需要手动打包的：0.582771535580524%的耗时
效率提升约为42%。

主要是解放双手，无需人为操作。避免失误的可能性（上传错包，二维码生成时输错了url)

### 资源准备
1:乐固 申请sercetId&secretKey [申请地址](https://console.cloud.tencent.com/cam/capi)

2:阿里云或者七牛云 申请sercetId&secretKey&bucket(需要运维/服务端提供支持)

### Android项目准备
Android加固方式一般有两种，一种是修改apk实现快速打包，第二种是动态修改AndroidManifest文件。

这个工具是针对第二种做的适配。

打开 app model 的AndroidManifest.xml

在application节点下加入
```xml
   <meta-data
        android:name="UMENG_CHANNEL"
        android:value="${UMENG_CHANNEL_VALUE}"/>
```

打开 app model 的build.gradle文件

首先在Android节点下加入:
```gradle
android {
    ...
    def isRelease = false
    for (String s : gradle.startParameter.taskNames) {
        if (s.contains("Release") | s.contains("release")) {
            isRelease = true
            break
        }
    }
    ...
     productFlavors {
        app {}
        tencent {}
        miui {}
        huawei {}
        flyme {}
        meizu {}
        oppo {}
        vivo {}
        productFlavors.all { flavor ->
            flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name]
        }
    }

    applicationVariants.all { variant ->
        variant.outputs.all { output ->
            outputFileName = "${variant.productFlavors[0].name}"+
            "@name_${defaultConfig.versionName}"+
            "@code_${defaultConfig.versionCode}"+
            "@${isRelease ? 'Release' : 'Debug'}.apk"
        }
    }
    ...
}
```
上面是为了实现多渠道打包。

### 脚本使用介绍
脚本用java+maven写的

打开用idea打开项目项目直接执行 maven package即可

[懒人包直接下载免编译](https://github.com/Gloomyer/AutoReinforce/releases/)

jar文件随意放

在jar文件同目录下编写config.json配置文件.


### config.json详解

```json
{
  "dir": {
    "projectSavePath": "", //项目的根路径
    "buildBranch": "",//需要打包的分支
    "outputDir": "" //最后结果的输出目录
  },
  "upload": {
    "uploadMethod": "qiniu", //上传方式 支持2个参数 [qiniu]和[ali]
    "QNAccessKey": "",// 七牛上传用的 accessKey
    "QNSecretKey": "",// 七牛上传用的 secretKey
    "QNBucket": "", //七牛上传 用的 bucket
    "QNStartUrl": "", //七牛下载地址前缀 如：https://xxxx.xxxx.com/ 必须带反斜杠
    "ALAccessKey": "",// 阿里上传用的 accessKey
    "ALSecretKey": "",// 阿里上传用的 secretKey
    "ALBucket": "",// 阿里上传 用的 bucket
    "ALStartUrl": "",// 阿里下载地址前缀 如：https://xxxx.xxxx.com/ 必须带反斜杠
    "ALEndpoint": "http://oss-cn-hangzhou.aliyuncs.com" //阿里上传 用的 节点 阿里文档有写，一般默认即可
  },
  "reinforce": {
    "reinforceMethod": "legu", //加固方式 目前只支持乐固
    "LGSecretId": "", //乐固 SecretId
    "LGSecretKey": "" // 乐固 SecretKey
  },
  "signer": {
    "apksignerPath": "/Users/gloomy/Library/Android/sdk/build-tools/28.0.3/apksigner", //这个是Android7.0之后的签名工具的全路径，去自己的sdk下找 必须7.0 以上的目录才有 项目的build tool 版本多少用多少就好了
    "storeFile": "", //签名文件的全路径
    "keyAlias": "", //签名文件别名
    "keyPassword": "", //签名文件密码
    "storePassword": "" //别名密码
  }
}
```

### 执行
终端进入jar同级目录。 执行
```shell script
java -jar name.jar
```
