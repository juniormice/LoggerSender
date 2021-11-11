package com.dbapp.bean.aes;

import org.apache.commons.lang3.StringUtils;

public enum AesMode {
    ECB, CBC, CFB, CTR, OFB;

    public static AesMode of(String mode) {
        if (StringUtils.isEmpty(mode)) {
            return null;
        }
        switch (mode.toUpperCase()) {
            case "ECB":
                return ECB;
            case "CBC":
                return CBC;
            case "CFB":
                return CFB;
            case "CTR":
                return CTR;
            case "OFB":
                return OFB;
            default:
                return null;
        }
    }
}
