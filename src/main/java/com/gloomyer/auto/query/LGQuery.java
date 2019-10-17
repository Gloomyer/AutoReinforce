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
import com.tencentcloudapi.ms.v20180408.models.DeleteShieldInstancesRequest;
import com.tencentcloudapi.ms.v20180408.models.DeleteShieldInstancesResponse;
import com.tencentcloudapi.ms.v20180408.models.DescribeShieldResultRequest;
import com.tencentcloudapi.ms.v20180408.models.DescribeShieldResultResponse;

public class LGQuery implements IQuery {

    private int count;

    private MsClient createMsClient(StringMap json) {
        String itemId = json.get("ItemId").toString();
        String secretId = Config.getDefault().getReinforce().getLGSecretId();
        String secretKey = Config.getDefault().getReinforce().getLGSecretKey();

        Credential cred = new Credential(secretId, secretKey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ms.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        return new MsClient(cred, "ap-shanghai", clientProfile);
    }

    @Override
    public String query(StringMap json) {
        try {
            String itemId = json.get("ItemId").toString();
            MsClient client = createMsClient(json);

            String params = "{\"ItemId\":\"" + itemId + "\"}";
            DescribeShieldResultRequest req = DescribeShieldResultRequest.fromJsonString(params, DescribeShieldResultRequest.class);

            DescribeShieldResultResponse resp = client.DescribeShieldResult(req);
            //2：运行中，3：异常
            if (resp.getTaskStatus() == 3) {
                //加固失败
                throw new JGError();
            }

            if (resp.getTaskStatus() == 2
                    || StringUtils.isNullOrEmpty(resp.getShieldInfo().getAppUrl())) {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("{0},第{1}次查询", itemId, ++count);
                return query(json);
            }
            return resp.getShieldInfo().getAppUrl();
        } catch (TencentCloudSDKException e) {
            Log.e(e.toString());
            return query(json);
        }
    }


    /**
     * 加固完成，删除任务
     *
     * @param json 任务id
     */
    @Override
    public void delete(StringMap json) {
        try {
            String itemId = json.get("ItemId").toString();
            MsClient client = createMsClient(json);
            String params = "{\"ItemIds\":[\"" + itemId + "\"]}";
            DeleteShieldInstancesRequest req = DeleteShieldInstancesRequest.fromJsonString(params, DeleteShieldInstancesRequest.class);
            DeleteShieldInstancesResponse resp = client.DeleteShieldInstances(req);
            Log.e("删除ID:{0} 加固任务,结果:{1}", itemId, DeleteShieldInstancesRequest.toJsonString(resp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
