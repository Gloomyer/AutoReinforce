package com.gloomyer.auto.protection.impl;

import com.gloomyer.auto.domain.ApkInfo;
import com.gloomyer.auto.utils.ApkUtils;
import com.gloomyer.auto.utils.LG;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ms.v20180408.MsClient;
import com.tencentcloudapi.ms.v20180408.models.CreateResourceInstancesRequest;
import com.tencentcloudapi.ms.v20180408.models.CreateResourceInstancesResponse;
import com.tencentcloudapi.ms.v20180408.models.CreateShieldInstanceRequest;
import com.tencentcloudapi.ms.v20180408.models.CreateShieldInstanceResponse;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;

class ProtectionTask implements Runnable {
    private final File file;
    final String url;
    private final String secretId;
    private final String secretKey;

    ProtectionTask(File file, String url, String secretId, String secretKey) {
        this.file = file;
        this.url = url;
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    @Override
    public void run() {
        createProtectionRule();
        //upload2LG();
    }

    /**
     * 创建加固策略
     */
    private void createProtectionRule() {
        Credential cred = new Credential(secretId, secretKey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ms.tencentcloudapi.com");

        JSONObject obj = new JSONObject();
        try {
            obj.put("Pid", "13624");
            obj.put("TimeUnit", "d");
            obj.put("TimeSpan", 1);
            obj.put("ResourceNum", 1);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        MsClient client = new MsClient(cred, "ap-shanghai", clientProfile);
        CreateResourceInstancesRequest req = CreateResourceInstancesRequest
                .fromJsonString(obj.toString(), CreateResourceInstancesRequest.class);
        try {
            CreateResourceInstancesResponse resp = client.CreateResourceInstances(req);
            System.out.println(CreateResourceInstancesRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void upload2LG() {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ms.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        MsClient client = new MsClient(cred, "", clientProfile);

        JSONObject params = new JSONObject();
        JSONObject appInfo = new JSONObject();
        JSONObject serviceInfo = new JSONObject();
        try {
            ApkInfo info = ApkUtils.read2Legu(file);
            assert info != null;
            appInfo.put("AppUrl", url);
            appInfo.put("AppMd5", info.getAppMd5());
            appInfo.put("AppSize", String.valueOf(info.getAppSize()));
            appInfo.put("FileName", file.getName());
            appInfo.put("AppPkgName", info.getAppPkgName());
            appInfo.put("AppVersion", info.getAppVersion());
            appInfo.put("AppName", info.getAppName());

            serviceInfo.put("ServiceEdition", "basic");
            serviceInfo.put("CallbackUrl", "");
            serviceInfo.put("SubmitSource", "MAC_TOOL");

            params.put("AppInfo", appInfo);
            params.put("ServiceInfo", serviceInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        LG.e("乐固提交数据:file:{0}\n加固参数:{1}", file.getName(), params.toString());
        CreateShieldInstanceRequest req = CreateShieldInstanceRequest
                .fromJsonString(params.toString(),
                        CreateShieldInstanceRequest.class);
        try {
            CreateShieldInstanceResponse resp = client.CreateShieldInstance(req);
            System.out.println(CreateShieldInstanceRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
