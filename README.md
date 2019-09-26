## anadroid 自动打包加固工具
### 简介
Android繁琐的渠道包加固流程极其的浪费时间。

所以决定写一个工具来完成这个事情。

目前加固方式只支持乐加固。

存储支持七牛&aliyun aliyun没有测试过。 手上没有alioss

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
