package com.dbapp.service;

import com.alibaba.fastjson.JSON;
import com.dbapp.bean.AutoConfig;
import com.dbapp.bean.SenderConfig;
import com.dbapp.exception.SocException;
import com.dbapp.service.handler.CatalogHandler;
import com.dbapp.util.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

import static com.dbapp.util.SystemUtils.isAbsolutePath;
import static java.lang.String.format;

/**
 * @author xutao
 */
public class SocCommonService {
    private static final String CURRENT_DIR = ".";
    private static final String CURRENT_DIR_PARENT = "..";
    private static final String RESOURCE_FILE = System.getProperty("config.file.path");
    /**
     * 工作目录，默认为打开目录的文件
     */
    private static String WORK_DIR = StringUtils.EMPTY;

    /**
     * 当前选中的space信息
     */

    private SocCommonService() {
    }


    private static AutoConfig autoConfig() {
        if (StringUtils.isEmpty(RESOURCE_FILE)) {
            return null;
        }
        try {
            String prop = FileUtils.readFileAsString(RESOURCE_FILE);
            return JSON.parseObject(prop, AutoConfig.class);
        } catch (Exception socE) {
            System.out.println(SenderConfig.isEn() ? "Auto Load Config Failed! " : "自动装载配置失败！" + socE.getMessage());
            return null;
        }
    }

    public static void saveConfig() {
        if (StringUtils.isEmpty(RESOURCE_FILE)) {
            return;
        }
        try {
            String content = SenderConfig.toConfig().format();
            FileUtils.writeContentToFile(content, RESOURCE_FILE);
        } catch (Exception socE) {
            DisplayService.directPrint(SenderConfig.isEn() ? "Config Save To File Failed" : "配置信息保存失败", socE);
        }
    }

    public static void init() throws SocException {
        SenderConfig.update(autoConfig());
        CatalogHandler.resetCatalog();
    }

    public static String getFileAbsolutePath(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return StringUtils.EMPTY;
        }
        if (isAbsolutePath(filePath)) {
            return filePath;
        }
        return WORK_DIR + File.separator + filePath;
    }

    public static void updateWorkDir(String dir) throws SocException {
        if (StringUtils.isEmpty(dir)) {
            return;
        }
        if (CURRENT_DIR.equals(dir)) {
            if (StringUtils.isEmpty(WORK_DIR)) {
                String dotPath = (new File(dir)).getAbsolutePath();
                String path = dotPath.substring(0, dotPath.length() - 1);
                dir = (new File(path)).getAbsolutePath();
            } else {
                dir = WORK_DIR;
            }
        }
        if (CURRENT_DIR_PARENT.equals(dir)) {
            if (StringUtils.isEmpty(WORK_DIR)) {
                dir = (new File((new File(dir)).getAbsolutePath())).getParent();
            } else {
                dir = new File(WORK_DIR).getParent();
            }

        }
        File file = new File(dir);
        if (file.exists()) {
            if (file.isFile()) {
                String msg = SenderConfig.isEn() ? "Dir:%s is not a directory" : "目录:%s 不是一个文件夹";
                throw new SocException(format(msg, file.getAbsolutePath()));
            }
        } else {
            boolean success = file.mkdirs();
            if (!success) {
                String msg = SenderConfig.isEn() ? "dir create failed: %s" : "目录创建失败: %s";
                throw new SocException(format(msg, file.getAbsolutePath()));
            }
        }
        WORK_DIR = file.getAbsolutePath();
    }

    public static void resetCatalog() throws SocException {
        updateWorkDir(CURRENT_DIR);
    }

    public static String currentWorkDir() {
        return WORK_DIR;
    }

}
