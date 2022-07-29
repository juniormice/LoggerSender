package com.dbapp.service;

import com.dbapp.Main;
import com.dbapp.bean.FileModel;
import com.dbapp.bean.SenderConfig;
import com.dbapp.bean.SwitchType;
import com.dbapp.exception.SocException;
import com.dbapp.service.handler.CatalogHandler;
import com.dbapp.service.handler.HelpHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.dbapp.constant.BizConst.*;
import static java.lang.String.format;

/**
 * 命令处理服务
 * 1.识别，解析命令
 * 2.调用需要的API服务完成调用
 * 3.调用展示服务进行命令的反馈
 *
 * @author xutao
 */
public class HandlerServer {
    private static final int CMD_ARG_INDEX_ONE = 1;
    private static final int CMD_ARG_INDEX_TWO = 2;

    private static final String CMD_SPLIT_REGEX = " +";

    private static Pattern ipPattern = Pattern.compile("^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$");
    private static Pattern portPattern = Pattern.compile("^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$");

    private HandlerServer() {
    }

    public static void handleCmd(String cmd) throws SocException {
        if (StringUtils.isEmpty(cmd)) {
            return;
        }
        String[] cmdArray = cmd.split(CMD_SPLIT_REGEX);
        for (int i = 0; i < cmdArray.length; i++) {
            if (CMD_CONST_EMPTY.equals(cmdArray[i])) {
                cmdArray[i] = StringUtils.EMPTY;
            }
        }
        List<String> cmdList = Arrays.asList(cmdArray);
        if (StringUtils.isEmpty(cmdList.get(0))) {
            return;
        }
        switch (cmdList.get(0).toLowerCase()) {
            case CMD_CONST_HELP:
                handleHelp();
                break;
            case CMD_CONST_RM:
                handleRm(cmdList, cmd);
                break;
            case CMD_CONST_MKDIR:
                handleMkdir(cmdList, cmd);
                break;
            case CMD_CONST_CD:
                handleCd(cmdList, cmd);
                break;
            case CMD_CONST_LL:
                handleLl();
                break;
            case CMD_CONST_LS:
                handleLs();
                break;
            case CMD_CONST_PWD:
                handlePwd();
                break;
            case CMD_CONST_QUIT:
                handleQuit();
                break;
            case CMD_CONST_EXIT:
                handleExit();
                break;
            case CMD_CONST_SHOW:
                handleShow();
                break;
            case CMD_CONST_SAVE:
                handleSave();
                break;
            case CMD_CONST_UPDATE:
                handleUpdate(cmdList, cmd);
                break;
            case CMD_CONST_SWITCH:
                handleSwitch(cmdList, cmd);
                break;
            case CMD_CONST_START:
                handleStart();
                break;
            case CMD_CONST_STOP:
                handleStop();
                break;
            default:
                DisplayService.cmdNotExists(cmd);
        }
    }

    private static void handleRm(List<String> cmdList, String cmd) throws SocException {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        if (cmdList.size() <= CMD_ARG_INDEX_ONE) {
            DisplayService.cmdNotExists(cmd);
            return;
        }
        CatalogHandler.rm(cmdList.get(CMD_ARG_INDEX_ONE));
    }

    private static void handleCd(List<String> cmdList, String cmd) throws SocException {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        if (cmdList.size() <= CMD_ARG_INDEX_ONE) {
            DisplayService.cmdNotExists(cmd);
            return;
        }
        CatalogHandler.enterCatalog(cmdList.get(CMD_ARG_INDEX_ONE));
    }

    private static void handleHelp() {
        String help = HelpHandler.help();
        DisplayService.directPrint(help);
    }

    private static void handleLl() {
        try {
            List<FileModel> children = CatalogHandler.ls();
            DisplayService.llResult(children);
        } catch (Exception e) {
            DisplayService.socException(e.getMessage());
        }
    }

    private static void handleLs() {

        try {
            List<FileModel> children = CatalogHandler.ls();
            DisplayService.lsResult(children);
        } catch (Exception e) {
            DisplayService.socException(e.getMessage());
        }
    }

    private static void handleMkdir(List<String> cmdList, String cmd) throws SocException {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        if (cmdList.size() <= CMD_ARG_INDEX_ONE) {
            DisplayService.cmdNotExists(cmd);
            return;
        }

        CatalogHandler.mkdir(cmdList.get(CMD_ARG_INDEX_ONE));
    }

    private static void handlePwd() {
        String path = CatalogHandler.pwd();
        DisplayService.pwdResult(path);
    }

    private static void handleShow() {
        DisplayService.directPrint(SenderConfig.description());
    }

    private static void handleSave() {
        SocCommonService.saveConfig();
    }

    private static void handleStart() throws SocException {
        SenderService.startSend();
    }

    private static void handleStop() throws SocException {
        SenderService.stopSend();
    }

    private static void updateIp(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            if (ipPattern.matcher(value).matches()) {
                SenderConfig.updateIp(value);
                DisplayService.updateSuccess();
            } else {
                DisplayService.directPrint(SenderConfig.isEn() ? "please use correct ip!" : "请输出正确的IP!");
            }
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateProtocol(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateProtocol(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updatePort(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            if (portPattern.matcher(value).matches()) {
                try {
                    int port = Integer.parseInt(value);
                    SenderConfig.updatePort(port);
                    DisplayService.updateSuccess();
                } catch (Exception e) {
                    DisplayService.directPrint(e.getMessage());
                }
            } else {
                DisplayService.directPrint(SenderConfig.isEn() ? "Please use correct port!" : "请输入正确的端口!");
            }
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateTotal(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            long total = 0;
            try {
                total = Long.parseLong(value);
            } catch (Exception e) {
                DisplayService.directPrint(e.getMessage());
            }
            if (total == -1 || total > 0) {
                SenderConfig.updateTotal(total);
                DisplayService.updateSuccess();
            } else {
                DisplayService.directPrint(SenderConfig.isEn() ? "Total should more than 0" : "总数需大于0");
            }
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateSpeed(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            int speed = 0;
            try {
                speed = Integer.parseInt(value);
            } catch (Exception e) {
                DisplayService.directPrint(e.getMessage());
            }
            if (speed > 0) {
                SenderConfig.updateSpeed(speed);
                DisplayService.updateSuccess();
            } else {
                DisplayService.directPrint(SenderConfig.isEn() ? "EPS should more than 0" : "EPS需大于0");
            }
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateFile(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String file = cmdList.get(CMD_ARG_INDEX_TWO);
            String resolvedFilePath = SocCommonService.getFileAbsolutePath(file);
            File finalFile = new File(resolvedFilePath);
            if (!finalFile.exists()) {
                String msg = SenderConfig.isEn() ? "file (%s) not exists!" : "文件(%s) 不存在!";
                DisplayService.directPrint(format(msg, resolvedFilePath));
                return;
            }
            if (!finalFile.canRead()) {
                String msg = SenderConfig.isEn() ? "file(%s) has no authority" : "文件(%s) 没有权限!";
                DisplayService.directPrint(format(msg, resolvedFilePath));
                return;
            }
            SenderConfig.updateFiles(resolvedFilePath);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateTopic(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateTopic(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void switchSsl(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SwitchType type = SwitchType.of(value);
            SenderConfig.updateSsl(SwitchType.ON.equals(type));
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateServer(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateServer(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateApiKey(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateApiKey(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateFileEncoding(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateFileEncoding(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateLogEncoding(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateLogEncoding(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateAesKey(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateAesKey(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateAesIV(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateAesInitVector(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateAesMode(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateAesMode(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateAesPadding(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateAesPadding(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateLocale(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateLocale(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void updateLogHeader(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SenderConfig.updateLogHeader(value);
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void handleUpdate(List<String> cmdList, String cmd) throws SocException {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize <= CMD_ARG_INDEX_ONE) {
            DisplayService.cmdNotExists(cmd);
            return;
        }
        String second = cmdList.get(CMD_ARG_INDEX_ONE);
        switch (second.toLowerCase()) {
            case CMD_CONST_PROTOCOL:
                updateProtocol(cmdList, cmd);
                break;
            case CMD_CONST_IP:
                updateIp(cmdList, cmd);
                break;
            case CMD_CONST_PORT:
                updatePort(cmdList, cmd);
                break;
            case CMD_CONST_TOTAL:
                updateTotal(cmdList, cmd);
                break;
            case CMD_CONST_SPEED:
                updateSpeed(cmdList, cmd);
                break;
            case CMD_CONST_FILE:
                updateFile(cmdList, cmd);
                break;
            case CMD_CONST_API_KEY:
                updateApiKey(cmdList, cmd);
                break;
            case CMD_CONST_SERVER:
                updateServer(cmdList, cmd);
                break;
            case CMD_CONST_TOPIC:
                updateTopic(cmdList, cmd);
                break;
            case CMD_CONST_FILE_ENCODING:
                updateFileEncoding(cmdList, cmd);
                break;
            case CMD_CONST_LOG_ENCODING:
                updateLogEncoding(cmdList, cmd);
                break;
            case CMD_CONST_AES_KEY:
                updateAesKey(cmdList, cmd);
                break;
            case CMD_CONST_AES_INIT_VECTOR:
                updateAesIV(cmdList, cmd);
                break;
            case CMD_CONST_AES_MODE:
                updateAesMode(cmdList, cmd);
                break;
            case CMD_CONST_AES_PADDING:
                updateAesPadding(cmdList, cmd);
                break;
            case CMD_CONST_LOG_HEADER:
                updateLogHeader(cmdList, cmd);
                break;
            case CMD_CONST_LOCALE:
                updateLocale(cmdList, cmd);
                break;
            default:
                DisplayService.cmdNotExists(cmd);
        }
    }

    private static void handleSwitch(List<String> cmdList, String cmd) throws SocException {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize <= CMD_ARG_INDEX_ONE) {
            DisplayService.cmdNotExists(cmd);
            return;
        }
        String second = cmdList.get(CMD_ARG_INDEX_ONE);
        switch (second.toLowerCase()) {
            case CMD_CONST_AES:
                switchAes(cmdList, cmd);
                break;
            case CMD_CONST_SSL:
                switchSsl(cmdList, cmd);
                break;
            case CMD_CONST_LOG_HEADER:
                switchLogHeader(cmdList, cmd);
                break;
            default:
                DisplayService.cmdNotExists(cmd);
        }
    }

    private static void switchAes(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SwitchType type = SwitchType.of(value);
            SenderConfig.updateUseAes(SwitchType.ON.equals(type));
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void switchLogHeader(List<String> cmdList, String cmd) {
        if (CollectionUtils.isEmpty(cmdList)) {
            return;
        }
        int cmdSize = cmdList.size();
        if (cmdSize > CMD_ARG_INDEX_TWO) {
            String value = cmdList.get(CMD_ARG_INDEX_TWO);
            SwitchType type = SwitchType.of(value);
            SenderConfig.updateUseLogHeader(SwitchType.ON.equals(type));
            DisplayService.updateSuccess();
        } else {
            DisplayService.cmdNotExists(cmd);
        }
    }

    private static void handleQuit() {
        Main.exit();
    }

    private static void handleExit() {
        Main.exit();
    }

}
