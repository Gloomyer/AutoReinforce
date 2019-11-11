package com.gloomyer.auto.protection;

import com.gloomyer.auto.protection.impl.lg.LGProtectionImpl;
import com.gloomyer.auto.protection.impl.qh.QihuProtectionImpl;

public class ProtectionFactory {
    public enum ProtectionMethod {
        LEGU,QIHOO
    }

    private static ProtectionMethod ofByName(String value) {
        return ProtectionMethod.valueOf(value.toUpperCase());
    }

    public static Protection create(ProtectionMethod method) {
        if (method == ProtectionMethod.LEGU) {
            return new LGProtectionImpl();
        } else if (method == ProtectionMethod.QIHOO) {
            return new QihuProtectionImpl();
        }
        return null;
    }

    public static Protection create(String methodName) {
        ProtectionMethod method = ofByName(methodName);
        return create(method);
    }
}
