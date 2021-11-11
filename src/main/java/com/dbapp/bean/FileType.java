package com.dbapp.bean;

/**
 * 文件类型枚举类
 *
 * @author xutao
 */
public enum FileType {

    /**
     * 文件类型
     */
    FILE("文件", "file"),

    /**
     * 文件夹类型
     */
    CATALOG("文件夹", "dir");

    private String displayNameEn;
    private String displayNameCn;

    FileType(String displayNameCn, String displayNameEn) {
        this.displayNameCn = displayNameCn;
        this.displayNameEn = displayNameEn;
    }

    public String getDisplayNameCn() {
        return displayNameCn;
    }

    public String getDisplayNameEn() {
        return displayNameEn;
    }
}
