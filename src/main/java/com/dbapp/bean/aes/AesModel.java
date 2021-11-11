package com.dbapp.bean.aes;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesModel {

    private SecretKeySpec secretKeySpec;
    private IvParameterSpec ivParameterSpec;
    private AesPadding padding;
    private AesMode mode;
    private ModeAndPadding modeAndPadding;

    public SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }

    public void setSecretKeySpec(SecretKeySpec secretKeySpec) {
        this.secretKeySpec = secretKeySpec;
    }

    public IvParameterSpec getIvParameterSpec() {
        return ivParameterSpec;
    }

    public void setIvParameterSpec(IvParameterSpec ivParameterSpec) {
        this.ivParameterSpec = ivParameterSpec;
    }

    public AesPadding getPadding() {
        return padding;
    }

    public void setPadding(AesPadding padding) {
        this.padding = padding;
    }

    public AesMode getMode() {
        return mode;
    }

    public void setMode(AesMode mode) {
        this.mode = mode;
    }

    public ModeAndPadding getModeAndPadding() {
        return modeAndPadding;
    }

    public void setModeAndPadding(ModeAndPadding modeAndPadding) {
        this.modeAndPadding = modeAndPadding;
    }
}
