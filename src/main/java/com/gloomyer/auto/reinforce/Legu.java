package com.gloomyer.auto.reinforce;

import com.gloomyer.auto.domain.ApkInfo;
import com.gloomyer.auto.utils.LG;
import com.google.gson.JsonObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ms.v20180408.MsClient;
import com.tencentcloudapi.ms.v20180408.models.CreateShieldInstanceRequest;
import com.tencentcloudapi.ms.v20180408.models.CreateShieldInstanceResponse;

/**
 * 乐谷加固
 */
public class Legu implements IReinforce {
    private static Legu ins = new Legu();

    public static Legu get() {
        return ins;
    }

    @Override
    public String reinforce(ApkInfo apkInfo) {
//        try {
//            String secretId = Config.getDefault().getReinforce().getLGSecretId();
//            String secretKey = Config.getDefault().getReinforce().getLGSecretKey();
//            Credential cred = new Credential(secretId, secretKey);
//
//            HttpProfile httpProfile = new HttpProfile();
//            httpProfile.setEndpoint("ms.tencentcloudapi.com");
//
//            ClientProfile clientProfile = new ClientProfile();
//            clientProfile.setHttpProfile(httpProfile);
//
//            MsClient client = new MsClient(cred, "ap-shanghai", clientProfile);
//            //对象转json
//            CreateShieldInstanceRequest req = CreateShieldInstanceRequest.fromJsonString(getParams(apkInfo), CreateShieldInstanceRequest.class);
//            CreateShieldInstanceResponse resp = client.CreateShieldInstance(req);
//            String json = CreateShieldInstanceRequest.toJsonString(resp);
//            if (json.contains("ItemId")) {
//                return json;
//            } else {
//                try {
//                    Thread.sleep(30 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return reinforce(apkInfo);
//            }
//        } catch (Exception e) {
//            LG.e(e.toString());
//            if(e.toString().contains("LimitExceeded-Max task count is 5"))
//                try {
//                    Thread.sleep(30 * 1000);
//                } catch (InterruptedException ee) {
//                    ee.printStackTrace();
//                }
//            return reinforce(apkInfo);
//        }
        return null;
    }

    private String getParams(ApkInfo apkInfo) {
        JsonObject obj = new JsonObject();
        JsonObject appInfo = new JsonObject();
        JsonObject serviceInfo = new JsonObject();
        appInfo.addProperty("AppUrl", apkInfo.getAppUrl());
        appInfo.addProperty("AppMd5", apkInfo.getAppMd5());
        appInfo.addProperty("AppSize", String.valueOf(apkInfo.getAppSize()));
        appInfo.addProperty("FileName", apkInfo.getFileName());
        appInfo.addProperty("AppPkgName", apkInfo.getAppPkgName());
        appInfo.addProperty("AppVersion", apkInfo.getAppVersion());
        appInfo.addProperty("AppName", apkInfo.getAppName());
        obj.add("AppInfo", appInfo);
        serviceInfo.addProperty("ServiceEdition", "basic");
        serviceInfo.addProperty("CallbackUrl", "");
        serviceInfo.addProperty("SubmitSource", "default");
        obj.add("ServiceInfo", serviceInfo);
        return obj.toString();
    }
}
