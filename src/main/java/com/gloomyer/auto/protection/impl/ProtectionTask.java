package com.gloomyer.auto.protection.impl;

import com.gloomyer.auto.domain.ApkInfo;
import com.gloomyer.auto.utils.ApkUtils;
import com.gloomyer.auto.utils.LG;
import com.qiniu.util.StringMap;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ms.v20180408.MsClient;
import com.tencentcloudapi.ms.v20180408.models.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;

class ProtectionTask implements Runnable {
    private static final long DEFAULT_SLEEP_TIME = 1000 * 60 * 3;
    private static final long DECREMENT_TIME = 1000 * 60;
    private static final long MIN_TIME = 1000 * 40;
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
        String itemId = upload2LG();
        JSONObject retInfo = query(itemId);
        downloadApk(retInfo);
    }

    /**
     * 对加固成功的结果，进行下载APK
     *
     * @param info info
     */
    private void downloadApk(JSONObject info) {

    }

    /**
     * 加固任务查询
     *
     * @param itemId id
     */
    private JSONObject query(String itemId) {
        long sleepTime = DEFAULT_SLEEP_TIME;
        for (; ; ) {
            try {
                Thread.sleep(sleepTime);
                sleepTime -= DECREMENT_TIME;
                if (sleepTime <= MIN_TIME) {
                    sleepTime = MIN_TIME;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MsClient client = createMsClient();

            String params = "{\"ItemId\":\"" + itemId + "\"}";
            DescribeShieldResultRequest req =
                    DescribeShieldResultRequest.fromJsonString(params, DescribeShieldResultRequest.class);
            try {
                DescribeShieldResultResponse resp = client.DescribeShieldResult(req);
                String json = CreateResourceInstancesRequest.toJsonString(resp);
                JSONObject obj = new JSONObject(json);
                LG.e("ID:{0}\n查询结果:{1}", itemId, json);
                if (resp.getTaskStatus() == 3) {
                    //异常，删除，重新执行
                    delete(itemId);
                    return query(upload2LG());
                } else if (resp.getTaskStatus() == 1) {
                    return obj;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private void delete(String itemId) {
        try {
            MsClient client = createMsClient();
            String params = "{\"ItemIds\":[\"" + itemId + "\"]}";
            DeleteShieldInstancesRequest req = DeleteShieldInstancesRequest.fromJsonString(params, DeleteShieldInstancesRequest.class);
            DeleteShieldInstancesResponse resp = client.DeleteShieldInstances(req);
            LG.e("删除ID:{0},结果:{1}", itemId, DeleteShieldInstancesRequest.toJsonString(resp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String upload2LG() {
        MsClient client = createMsClient();

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
            JSONObject obj = new JSONObject(CreateShieldInstanceRequest.toJsonString(resp));
            LG.e("加固结果:{0}", obj.toString());
            return obj.getString("ItemId");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private MsClient createMsClient() {
        Credential cred = new Credential(secretId, secretKey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ms.tencentcloudapi.com");
//
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        return new MsClient(cred, "ap-shanghai", clientProfile);
    }
}
