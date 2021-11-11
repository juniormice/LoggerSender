package com.dbapp.bean.aes;

import com.dbapp.exception.SocException;
import com.dbapp.service.DisplayService;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static com.dbapp.bean.aes.AesMode.*;

public class AesEncryption {

    private final Set<AesMode> NEED_IV_MODE = new HashSet<>();
    private final Set<ModeAndPadding> NEED_PADDING = new HashSet<>();
    private final AesModel aesModel;

    public AesEncryption(String key, String iv, String mode, String padding) throws Exception {
        NEED_IV_MODE.add(CBC);
        NEED_IV_MODE.add(CFB);
        NEED_IV_MODE.add(CTR);
        NEED_IV_MODE.add(OFB);
        NEED_PADDING.add(ModeAndPadding.ECB_No);
        NEED_PADDING.add(ModeAndPadding.CBC_No);
        aesModel = new AesModel();
        if (StringUtils.isNotEmpty(key)) {
            aesModel.setSecretKeySpec(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AesConstants.PROTOCOL_AES));
        }
        if (StringUtils.isNotEmpty(iv)) {
            aesModel.setIvParameterSpec(new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
        }
        AesMode aesMode = AesMode.of(mode);
        if (aesMode == null) {
            throw new SocException("Aes Mode UnSupport：" + mode);
        }
        aesModel.setMode(aesMode);
        AesPadding aesPadding = AesPadding.of(padding);
        if (aesPadding == null) {
            throw new SocException("Aes InitVector UnSupport：" + padding);
        }
        aesModel.setPadding(aesPadding);
        aesModel.setModeAndPadding(ModeAndPadding.of(mode, padding));
    }

    public String encryptToString(byte[] value) {
        try {
            Cipher cipher = getCipherForEncode();
            if (NEED_PADDING.contains(aesModel.getModeAndPadding())) {
                value = paddingEcbNoValue(cipher, value);
            }
            byte[] encrypted = cipher.doFinal(value);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            DisplayService.directPrint("encrypt data failed.", e);
        }
        return null;
    }

    private Cipher getCipherForEncode() throws Exception {
        String instanceName = AesConstants.PROTOCOL_AES + "/" + aesModel.getMode().name() + "/" + aesModel.getPadding().name();
        Cipher cipher = Cipher.getInstance(instanceName);
        if (NEED_IV_MODE.contains(aesModel.getMode())) {
            cipher.init(Cipher.ENCRYPT_MODE, aesModel.getSecretKeySpec(), aesModel.getIvParameterSpec());
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, aesModel.getSecretKeySpec());
        }
        return cipher;
    }

    private byte[] paddingEcbNoValue(Cipher cipher, byte[] value) {
        int blockSize = cipher.getBlockSize();
        int plainTextLength = value.length;
        if (plainTextLength % blockSize != 0) {
            plainTextLength = plainTextLength + (blockSize - (plainTextLength % blockSize));
        }
        byte[] plainText = new byte[plainTextLength];
        System.arraycopy(value, 0, plainText, 0, value.length);
        return plainText;
    }
}
