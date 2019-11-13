package com.gloomyer.auto.upload.impl;

import com.gloomyer.auto.generate.HttpClientGenerate;
import com.gloomyer.auto.upload.Upload;
import com.gloomyer.auto.upload.UploadCache;
import com.gloomyer.auto.utils.LG;
import com.gloomyer.auto.utils.Utils;
import okhttp3.*;

import java.io.File;

public class ApiHostUploadImpl implements Upload {

    @Override
    public String upload(File file) {
        LG.e("api-host平台开始上传数据!");
        long start = System.currentTimeMillis();

        String ret = apiHostUpload(file);

        long end = System.currentTimeMillis();
        LG.e("api-host上传完成，上传耗时:{0}秒", (end - start) / 1000);
        return ret;
    }

    private String apiHostUpload(File file) {
        OkHttpClient client = Utils.getDefaultImpl(HttpClientGenerate.class).generate();
        MultipartBody.Builder bodyBuilder = new MultipartBody
                .Builder()
                .setType(MultipartBody.FORM);
        RequestBody body = RequestBody.create(MediaType.parse("file/*"), file);
        bodyBuilder.addFormDataPart("file", file.getName(), body);
        bodyBuilder.addFormDataPart("token", UploadCache.appHostToken);
        bodyBuilder.addFormDataPart("plat_id", UploadCache.appHostPlatId);
        Request request = new Request.Builder().url(UploadCache.appHostUrl)
                .post(bodyBuilder.build())
                .build();
        Call call = client.newCall(request);
        String ret;
        try {
            ret = call.execute().body().string();
        } catch (Exception e) {
            e.printStackTrace();
            ret = e.toString();
        }

        return ret;
    }
}
