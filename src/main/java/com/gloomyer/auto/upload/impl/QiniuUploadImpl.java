package com.gloomyer.auto.upload.impl;

import com.gloomyer.auto.generate.HttpClientGenerate;
import com.gloomyer.auto.upload.Upload;
import com.gloomyer.auto.upload.UploadCache;
import com.gloomyer.auto.utils.FileUtils;
import com.gloomyer.auto.utils.LG;
import com.gloomyer.auto.utils.Utils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.text.MessageFormat;

public class QiniuUploadImpl implements Upload {
    private UploadManager uploadManager;
    private Configuration cfg;

    @Override
    public String upload(File file) {
        createUploadParams();
        try {
            String fileUrl = UploadCache.uploadUrlPrefix + file.getName();
            //先检测是否已经存在了
            if (isExist(file, fileUrl)) {
                return fileUrl;
            }

            String token = createUploadToken();
            Response response = uploadManager.put(file.getAbsolutePath(), file.getName(), token);
            JSONObject obj = new JSONObject(response.bodyString());
            if (obj.has("hash") && obj.has("key")) {
                return fileUrl;
            }
            throw new RuntimeException(MessageFormat.format("文件上传失败!{0}", obj.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 先检测是否已经存在了
     *
     * @param file    文件
     * @param fileUrl 文件url
     * @return 是否已经存在服务端了，如果是不走传输，提升效率
     */
    private boolean isExist(File file, String fileUrl) {
        OkHttpClient client = Utils.getDefaultImpl(HttpClientGenerate.class).generate();
        String queryUrl = getFileInfoUrl(fileUrl);
        Request request = new Request.Builder()
                .url(queryUrl)
                .get()
                .build();
        Call call = client.newCall(request);
        try {
            okhttp3.Response response = call.execute();
            assert response.body() != null;
            JSONObject json = new JSONObject(response.body().string());
            if (json.has("hash") && json.has("fsize")) {
                String remoteMd5 = json.getString("hash");
                long fileSize = json.getLong("fsize");
                String localMd5 = Utils.getFileMD5(file);
                boolean ret = remoteMd5.equals(localMd5) && fileSize == file.length();
                if (ret) {
                    LG.e("fileName:[{0}]\n" +
                                    "remoteMD5:[{1}]\n" +
                                    "localMD5:[{2}]\n" +
                                    "服务端文件MD5 文件Size 和本地匹配，认定已经存在，不走上传逻辑直接认为成功!\n" +
                                    "完整URL:[{3}]\n" +
                                    "服务器MD5文件查询URL:[{4}]",
                            file.getName(),
                            remoteMd5,
                            localMd5,
                            fileUrl,
                            queryUrl
                    );
                }
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @NotNull
    private String getFileInfoUrl(String fileUrl) {
        return MessageFormat.format("{0}?qhash/md5", fileUrl);
    }

    private String createUploadToken() {
        Auth auth = Auth.create(UploadCache.uploadAccessKey, UploadCache.uploadSecretKey);
        String token = auth.uploadToken(UploadCache.uploadBucketName);
        LG.i("七牛上传token:{0}", token);
        return token;
    }

    /**
     * 反射创建参数
     */
    private void createUploadParams() {
        if (cfg == null) {
            cfg = new Configuration(Region.autoRegion());
            uploadManager = new UploadManager(cfg);

            try {
                Field clientField = uploadManager.getClass().getDeclaredField("client");
                clientField.setAccessible(true);
                Client client = (Client) clientField.get(uploadManager);
                Field httpClientField = client.getClass().getDeclaredField("httpClient");
                httpClientField.setAccessible(true);
                OkHttpClient.Builder builder = Utils.getDefaultImpl(HttpClientGenerate.class).generateBuilder();
                builder.addNetworkInterceptor(chain -> {
                    Request request = chain.request();
                    okhttp3.Response response = chain.proceed(request);
                    Object tag = request.tag();

                    assert tag != null;
                    try {
                        Field ipField = tag.getClass().getDeclaredField("ip");
                        ipField.setAccessible(true);
                        ipField.set(tag,
                                chain.connection().socket().getRemoteSocketAddress().toString());
                    } catch (Exception var6) {
                        try {
                            Field ipField = tag.getClass().getDeclaredField("ip");
                            ipField.set(tag, "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    return response;
                });
                httpClientField.set(client, builder.build());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
