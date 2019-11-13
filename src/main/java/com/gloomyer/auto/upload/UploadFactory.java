package com.gloomyer.auto.upload;

import com.gloomyer.auto.upload.impl.ApiHostUploadImpl;
import com.gloomyer.auto.upload.impl.PgyUploadImpl;
import com.gloomyer.auto.upload.impl.QiniuUploadImpl;

public class UploadFactory {
    public enum UploadMethod {
        PGY, QINIU, API_HOST
    }

    private static UploadMethod ofByName(String value) {
        return UploadMethod.valueOf(value.toUpperCase());
    }

    public static Upload create(UploadMethod method) {
        if (method == UploadMethod.PGY) {
            return new PgyUploadImpl();
        } else if (method == UploadMethod.QINIU) {
            return new QiniuUploadImpl();
        } else if (method == UploadMethod.API_HOST) {
            return new ApiHostUploadImpl();
        }
        return null;
    }

    public static Upload create(String methodText) {
        UploadMethod method = ofByName(methodText);
        return create(method);
    }
}
