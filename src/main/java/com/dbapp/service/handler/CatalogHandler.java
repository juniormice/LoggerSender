package com.dbapp.service.handler;

import com.dbapp.bean.FileModel;
import com.dbapp.bean.FileType;
import com.dbapp.bean.SenderConfig;
import com.dbapp.exception.SocException;
import com.dbapp.service.SocCommonService;
import com.dbapp.service.handler.completer.CatalogCompleter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

/**
 * 用于目录类操作命令的处理
 *
 * @author xutao
 * @version 1.0.1 目录易用性改造
 */
public class CatalogHandler {
    private static final String BACKEND_PATH = "..";
    private static final String WINDOWS_FILE_PREFIX = ":";
    private static final String LINUX_FILE_PREFIX = "/";

    private static CatalogCompleter catalogCompleter = new CatalogCompleter();

    public static void mkdir(String catalogName) throws SocException {
        if (StringUtils.isEmpty(catalogName)) {
            String msg = SenderConfig.isEn() ? "fileName is empty" : "文件名为空";
            throw new SocException(msg);
        }
        String filePath = SocCommonService.getFileAbsolutePath(catalogName);
        File file = new File(filePath);
        if (file.exists()) {
            String msg = SenderConfig.isEn() ? "dir (%s) already exist." : "目录(%s)已存在";
            throw new SocException(format(msg, catalogName));
        } else {
            try {
                boolean success = file.mkdir();
                if (!success) {
                    String msg = SenderConfig.isEn() ? "dir create failed!" : "文件夹创建失败！";
                    throw new SocException(msg);
                }
            } catch (Exception e) {
                throw new SocException(e.getMessage());
            }
        }
        refreshCatalog();
    }

    public static void resetCatalog() throws SocException {
        SocCommonService.resetCatalog();
        refreshCatalog();
    }

    public static synchronized void enterCatalog(String fileName) throws SocException {
        if (StringUtils.isEmpty(fileName)) {
            String msg = SenderConfig.isEn() ? "fileName is empty." : "文件名为空";
            throw new SocException(msg);
        }
        // 进入 根目录 或者 上层目录
        if (fileName.startsWith(LINUX_FILE_PREFIX) || (fileName.indexOf(WINDOWS_FILE_PREFIX) > 0) ||
                BACKEND_PATH.equals(fileName)) {
            SocCommonService.updateWorkDir(fileName);
            refreshCatalog();
            return;
        }

        List<FileModel> childList = listFiles(SocCommonService.currentWorkDir());
        if (CollectionUtils.isEmpty(childList)) {
            String msg = SenderConfig.isEn() ? "file (%s) not exists" : "文件(%s)不存在";
            throw new SocException(format(msg, fileName));
        }
        for (FileModel fileModel : childList) {
            if (fileModel.getFileName().equals(fileName)) {
                switch (fileModel.getType()) {
                    case FILE:
                        String msg = SenderConfig.isEn() ? "(%s) is not a directory" : "(%s)不是一个文件夹";
                        throw new SocException(format(msg, fileName));
                    case CATALOG:
                        SocCommonService.updateWorkDir(fileModel.getAbsolutePath());
                        refreshCatalog();
                        return;
                    default:
                }
            }
        }
        String msg = SenderConfig.isEn() ? "file (%s) not exists" : "文件(%s)不存在";
        throw new SocException(format(msg, fileName));
    }

    public static List<FileModel> ls() {
        return listFiles(SocCommonService.currentWorkDir());
    }

    public static void rm(String fileName) throws SocException {
        if (StringUtils.isEmpty(fileName)) {
            String msg = SenderConfig.isEn() ? "fileName is empty." : "文件名为空";
            throw new SocException(msg);
        }
        String fullPath = SocCommonService.currentWorkDir() + File.separator + fileName;
        File file = new File(fullPath);
        try {
            boolean flag = file.delete();
            if (!flag) {
                String msg = SenderConfig.isEn() ? "file (%s) delete failed." : "文件(%s)删除失败";
                throw new SocException(format(msg, fullPath));
            }
        } catch (Exception e) {
            throw new SocException(e.getMessage());
        }

        refreshCatalog();
    }

    public static String pwd() {
        return SocCommonService.currentWorkDir();
    }

    public static CatalogCompleter catalogCompleter() {
        return catalogCompleter;
    }

    private static void refreshCatalog() throws SocException {
        String workDir = SocCommonService.currentWorkDir();
        if (StringUtils.isEmpty(workDir)) {
            String msg = SenderConfig.isEn() ? "file directory get failed. inter error!" : "文件目录获取失败,内部错误!";
            throw new SocException(msg);
        }
        catalogCompleter.refresh(listFiles(workDir));
    }

    private static List<FileModel> listFiles(String fileParent) {
        if (StringUtils.isEmpty(fileParent)) {
            return null;
        }
        File file = new File(fileParent);
        File[] childFiles = file.listFiles();
        List<FileModel> nodes = new ArrayList<>();
        if (Objects.isNull(childFiles)) {
            return nodes;
        }
        for (File child : childFiles) {
            FileModel fileModel = new FileModel();
            fileModel.setFileName(child.getName());
            fileModel.setAbsolutePath(child.getAbsolutePath());
            fileModel.setType(child.isFile() ? FileType.FILE : FileType.CATALOG);
            fileModel.setLength(child.length());
            nodes.add(fileModel);
        }
        return nodes;
    }

}
