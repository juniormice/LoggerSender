package com.dbapp.bean;

import org.apache.commons.lang3.StringUtils;

public enum ProtocolType {
    UDP,
    TCP,
    SNMPTRAP,
    HTTP,
    KAFKA;

    public static ProtocolType of(String type) {
        if (StringUtils.isEmpty(type)) {
            return UDP;
        }
        switch (type.toLowerCase()) {
            case "udp":
                return UDP;
            case "tcp":
                return TCP;
            case "snmptrap":
                return SNMPTRAP;
            case "http":
                return HTTP;
            case "kafka":
                return KAFKA;
            default:
                return UDP;
        }
    }
}
