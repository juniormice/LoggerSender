package com.dbapp.bean;

import org.apache.commons.lang3.StringUtils;

public enum SwitchType {
    ON,
    OFF;

    public static SwitchType of(String type) {
        if (StringUtils.isEmpty(type)) {
            return OFF;
        }
        if ("on".equals(type.toLowerCase())) {
            return ON;
        }
        return OFF;
    }
}
