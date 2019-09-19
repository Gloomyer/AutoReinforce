package com.gloomyer.auto;

import com.gloomyer.auto.domain.ApkInfo;
import com.gloomyer.auto.utils.ApkUtils;
import org.junit.Test;

public class TestApk {

    @Test
    public void test() {
        ApkInfo info = ApkUtils.read2Legu("/Users/gloomy/Projects/rucheng-android/ructrip/build/outputs/apk/huawei/release/huawei@name_1.1.0@code_1909191755.apk");
        System.out.println(info);
    }
}
