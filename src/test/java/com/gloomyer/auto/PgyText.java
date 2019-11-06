package com.gloomyer.auto;

import com.gloomyer.auto.upload.UploadCache;
import com.gloomyer.auto.upload.impl.PgyUploadMpl;
import org.junit.Test;

import java.io.File;

public class PgyText {
    @Test
    public void upload() {
        UploadCache.pgyApiKey = "82dca5392cdfe2cec4e1a65e48532b29";
        new PgyUploadMpl()
                .upload(new File("/Users/gloomy/Downloads/autos/ructrip@name_1.0.3@code_771@Debug.apk"));
    }
}
