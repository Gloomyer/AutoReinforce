package com.gloomyer.auto.query;

import com.gloomyer.auto.config.Config;
import com.gloomyer.auto.utils.Log;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ms.v20180408.MsClient;
import com.tencentcloudapi.ms.v20180408.models.DescribeShieldResultRequest;
import com.tencentcloudapi.ms.v20180408.models.DescribeShieldResultResponse;

public class LGQuery implements IQuery {

    private int count;

    @Override
    public String query(StringMap json) {
        try {
            String requestId = json.get("ItemId").toString();
            String secretId = Config.getDefault().getReinforce().getLGSecretId();
            String secretKey = Config.getDefault().getReinforce().getLGSecretKey();
            @SuppressWarnings("DuplicatedCode")
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            MsClient client = new MsClient(cred, "ap-shanghai", clientProfile);

            String params = "{\"ItemId\":\"" + requestId + "\"}";
            DescribeShieldResultRequest req = DescribeShieldResultRequest.fromJsonString(params, DescribeShieldResultRequest.class);

            DescribeShieldResultResponse resp = client.DescribeShieldResult(req);

            if (StringUtils.isNullOrEmpty(resp.getShieldInfo().getAppUrl())) {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("{0},第{1}次查询", requestId, ++count);
                return query(json);
            }
            return resp.getShieldInfo().getAppUrl();
        } catch (TencentCloudSDKException e) {
            Log.e(e.toString());
            return query(json);
        }
    }
}
