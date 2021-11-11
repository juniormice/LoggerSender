package com.dbapp.bean.aes;

import org.apache.commons.lang3.StringUtils;

public enum AesPadding {
    PKCS5PADDING, ISO10126PADDING, NOPADDING;

    public static AesPadding of(String padding) {
        if (StringUtils.isEmpty(padding)) {
            return null;
        }
        switch (padding.toUpperCase()) {
            case "PKCS5PADDING":
                return PKCS5PADDING;
            case "ISO10126PADDING":
                return ISO10126PADDING;
            case "NOPADDING":
                return NOPADDING;
            default:
                return null;
        }
    }
}
