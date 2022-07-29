package com.dbapp.service;

import com.dbapp.bean.ProtocolType;
import com.dbapp.bean.SwitchType;
import com.dbapp.bean.aes.AesMode;
import com.dbapp.bean.aes.AesPadding;
import com.dbapp.constant.BizConst;
import com.dbapp.service.handler.CatalogHandler;
import org.apache.commons.lang3.StringUtils;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.*;

/**
 * 命令行交互层
 * 1: 命令的帮助
 * 2: 命令的解析
 *
 * @author xutao
 */
public class CommandService {

    private static final String CMD_SEPARATOR = " ";

    private CommandService() {
    }

    private static ArgumentCompleter argumentCompleterHelp() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.HELP));
    }

    private static ArgumentCompleter argumentCompleterCd() {
        return new ArgumentCompleter(
                new StringsCompleter("cd"),
                CatalogHandler.catalogCompleter(),
                new NullCompleter()
        );
    }

    private static ArgumentCompleter argumentCompleterSwitchAes() {
        return new ArgumentCompleter(
                new StringsCompleter("switch"),
                new StringsCompleter("aes"),
                new EnumCompleter(SwitchType.class),
                new NullCompleter()
        );
    }

    private static ArgumentCompleter argumentCompleterSwitchSsl() {
        return new ArgumentCompleter(
                new StringsCompleter("switch"),
                new StringsCompleter("ssl"),
                new EnumCompleter(SwitchType.class),
                new NullCompleter()
        );
    }

    private static ArgumentCompleter argumentCompleterSwitchLogHeader() {
        return new ArgumentCompleter(
                new StringsCompleter("switch"),
                new StringsCompleter("logHeader"),
                new EnumCompleter(SwitchType.class),
                new NullCompleter()
        );
    }

    private static ArgumentCompleter argumentCompleterUpdateProtocol() {
        return new ArgumentCompleter(
                new StringsCompleter("update"),
                new StringsCompleter("protocol"),
                new EnumCompleter(ProtocolType.class),
                new NullCompleter()
        );
    }

    private static ArgumentCompleter argumentCompleterUpdateFile() {
        return new ArgumentCompleter(
                new StringsCompleter("update"),
                new StringsCompleter("file"),
                CatalogHandler.catalogCompleter(),
                new NullCompleter()
        );
    }

    private static ArgumentCompleter argumentCompleterUpdateAesMode() {
        return new ArgumentCompleter(
                new StringsCompleter("update"),
                new StringsCompleter("aesMode"),
                new EnumCompleter(AesMode.class),
                new NullCompleter()
        );
    }

    private static ArgumentCompleter argumentCompleterUpdateAesPadding() {
        return new ArgumentCompleter(
                new StringsCompleter("update"),
                new StringsCompleter("aesPadding"),
                new EnumCompleter(AesPadding.class),
                new NullCompleter()
        );
    }

    private static ArgumentCompleter argumentCompleterLl() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.LL));
    }

    private static ArgumentCompleter argumentCompleterLs() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.LS));
    }

    private static ArgumentCompleter argumentCompleterPwd() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.CMD_CONST_PWD));
    }

    private static ArgumentCompleter argumentCompleterMkdir() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.MKDIR));
    }

    private static ArgumentCompleter argumentCompleterRm() {
        return new ArgumentCompleter(
                new StringsCompleter("rm"),
                CatalogHandler.catalogCompleter(),
                new NullCompleter()
        );
    }

    private static ArgumentCompleter argumentCompleterQuit() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.QUIT));
    }

    private static ArgumentCompleter argumentCompleterExit() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.EXIT));
    }

    private static Completer[] buildStringCompleter(String cmd) {
        if (StringUtils.isEmpty(cmd)) {
            return new Completer[]{new NullCompleter()};
        }
        String[] strings = cmd.split(CMD_SEPARATOR);
        Completer[] completerList = new Completer[strings.length + 1];
        for (int i = 0; i < strings.length; i++) {
            completerList[i] = new StringsCompleter(strings[i]);
        }
        completerList[strings.length] = new NullCompleter();
        return completerList;
    }


    private static ArgumentCompleter argumentCompleterStart() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.CMD_CONST_START));
    }

    private static ArgumentCompleter argumentCompleterShow() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.CMD_CONST_SHOW));
    }

    private static ArgumentCompleter argumentCompleterSave() {
        return new ArgumentCompleter(buildStringCompleter(BizConst.CMD_CONST_SAVE));
    }

    private static AggregateCompleter argumentCompleterSwitch() {
        return new AggregateCompleter(
                argumentCompleterSwitchAes(),
                argumentCompleterSwitchLogHeader(),
                argumentCompleterSwitchSsl()
        );
    }

    private static AggregateCompleter argumentCompleterUpdate() {
        return new AggregateCompleter(
                argumentCompleterUpdateProtocol(),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_IP)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_PORT)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_TOTAL)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_SPEED)),
                argumentCompleterUpdateFile(),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_API_KEY)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_SERVER)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_TOPIC)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_FILE_ENCODING)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_LOG_ENCODING)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_AES_KEY)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_AES_IV)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_LOG_HEADER)),
                new ArgumentCompleter(buildStringCompleter(BizConst.UPDATE_LOCALE)),
                argumentCompleterUpdateAesMode(),
                argumentCompleterUpdateAesPadding()
        );
    }

    public static Completer completer() {
        return new AggregateCompleter(
                argumentCompleterHelp(),
                argumentCompleterCd(),
                argumentCompleterLl(),
                argumentCompleterLs(),
                argumentCompleterPwd(),
                argumentCompleterMkdir(),
                argumentCompleterRm(),
                argumentCompleterQuit(),
                argumentCompleterExit(),
                argumentCompleterStart(),
                argumentCompleterShow(),
                argumentCompleterSave(),
                argumentCompleterUpdate(),
                argumentCompleterSwitch()
        );
    }

}
