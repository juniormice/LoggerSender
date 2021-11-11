package com.dbapp.bean.aes;

import java.util.Objects;

import static com.dbapp.bean.aes.AesMode.*;
import static com.dbapp.bean.aes.AesPadding.*;

public enum ModeAndPadding {
    // ECB
    ECB_PKCS5(ECB, PKCS5PADDING), ECB_ISO10126(ECB, ISO10126PADDING), ECB_No(ECB, NOPADDING),
    // CBC
    CBC_PKCS5(CBC, PKCS5PADDING), CBC_ISO10126(CBC, ISO10126PADDING), CBC_No(CBC, NOPADDING),
    // CFB
    CFB_PKCS5(CFB, PKCS5PADDING), CFB_ISO10126(CFB, ISO10126PADDING), CFB_No(CFB, NOPADDING),
    // CTR
    CTR_PKCS5(CTR, PKCS5PADDING), CTR_ISO10126(CTR, ISO10126PADDING), CTR_No(CFB, NOPADDING),
    // OFB
    OFB_PKCS5(OFB, PKCS5PADDING), OFB_ISO10126(OFB, ISO10126PADDING), OFB_No(CFB, NOPADDING);
    private final AesMode mode;
    private final AesPadding padding;

    ModeAndPadding(AesMode mode, AesPadding padding) {
        this.mode = mode;
        this.padding = padding;
    }

    public AesMode getMode() {
        return mode;
    }

    public AesPadding getPadding() {
        return padding;
    }

    private static ModeAndPadding getModeAndPadding(AesPadding padding, ModeAndPadding pkcs5, ModeAndPadding iso10126, ModeAndPadding no) {
        switch (padding) {
            case PKCS5PADDING:
                return pkcs5;
            case ISO10126PADDING:
                return iso10126;
            case NOPADDING:
                return no;
            default:
                return null;
        }
    }

    public static ModeAndPadding of(String mode, String padding) {
        AesMode aesMode = AesMode.of(mode);
        AesPadding aesPadding = AesPadding.of(padding);
        if (Objects.isNull(aesMode) || Objects.isNull(aesPadding)) {
            return null;
        }
        switch (aesMode) {
            case ECB:
                return getModeAndPadding(aesPadding, ECB_PKCS5, ECB_ISO10126, ECB_No);
            case CBC:
                return getModeAndPadding(aesPadding, CBC_PKCS5, CBC_ISO10126, CBC_No);
            case CFB:
                return getModeAndPadding(aesPadding, CFB_PKCS5, CFB_ISO10126, CFB_No);
            case CTR:
                return getModeAndPadding(aesPadding, CTR_PKCS5, CTR_ISO10126, CTR_No);
            case OFB:
                return getModeAndPadding(aesPadding, OFB_PKCS5, OFB_ISO10126, OFB_No);
            default:
                return null;
        }
    }
}
