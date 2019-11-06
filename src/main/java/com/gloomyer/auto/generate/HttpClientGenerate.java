package com.gloomyer.auto.generate;

import com.gloomyer.auto.annotation.DefaultImpl;
import com.gloomyer.auto.generate.impl.HttpClientGenerateImpl;
import okhttp3.OkHttpClient;

@DefaultImpl(clazz = HttpClientGenerateImpl.class)
public interface HttpClientGenerate {
    OkHttpClient generate();
}
