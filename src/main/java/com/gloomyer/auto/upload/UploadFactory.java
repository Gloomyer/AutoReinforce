package com.gloomyer.auto.upload;

import com.gloomyer.auto.upload.impl.PgyUploadMpl;

public class UploadFactory {
    public enum UploadMethod {
        PGY,
    }

    public static Upload create(UploadMethod method) {
        if (method == UploadMethod.PGY) {
            return new PgyUploadMpl();
        }

        return null;
    }
}
