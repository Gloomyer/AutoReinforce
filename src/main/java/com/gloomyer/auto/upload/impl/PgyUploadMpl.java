package com.gloomyer.auto.upload.impl;

import com.gloomyer.auto.generate.HttpClientGenerate;
import com.gloomyer.auto.upload.Upload;
import com.gloomyer.auto.upload.UploadCache;
import com.gloomyer.auto.utils.LG;
import com.gloomyer.auto.utils.ShellExecute;
import com.gloomyer.auto.utils.Utils;
import okhttp3.*;

import java.io.File;

public class PgyUploadMpl implements Upload {
    private static final String PGY_RUL = "https://www.pgyer.com/apiv2/app/upload";

    @SuppressWarnings("ConstantConditions")
    @Override
    public String upload(File file) {
        //String cmd = String.format(
        //        "curl -F 'file=@%s' -F '_api_key=%s' https://www.pgyer.com/apiv2/app/upload",
        //        file.getAbsolutePath(),
        //        UploadCache.pgyApiKey
        //);
        //LG.e("upload cmd:{0}", cmd);
        LG.e("蒲公英平台开始上传数据!");
        long start = System.currentTimeMillis();
        OkHttpClient client = Utils.getDefaultImpl(HttpClientGenerate.class).generate();
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body = RequestBody.create(MediaType.parse("file/*"), file);
        bodyBuilder.addFormDataPart("file", file.getName(), body);
        bodyBuilder.addFormDataPart("_api_key", UploadCache.pgyApiKey);

        Request request = new Request.Builder().url(PGY_RUL)
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

        long end = System.currentTimeMillis();
        LG.e("蒲公英上传完成，上传耗时:{0}秒", (end - start) / 1000);
        return ret;
    }
}
