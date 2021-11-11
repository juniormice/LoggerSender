package com.dbapp.constant;

import static com.dbapp.bean.SenderConfig.isEn;
import static com.dbapp.service.DisplayService.FORMAT_HEADER;
import static com.dbapp.service.DisplayService.LINE_BREAK;

/**
 * @author xutao
 * @date 2020/02/19
 */
public class HelpConst {

    static final String ANNOTATION_LINE = "  --";
    static final String MODULE_FORMAT_PREFIX = "   ";
    static final String CMD_FORMAT_PREFIX = "     ";
    static final String CMD_ANNOTATION_HELP = isEn() ? " use <help> to show all command." : " 使用help展示全部命令.";
    private static final String CMD_ANNOTATION_CD = isEn() ? " enter catalog, one by one" : " 用于进入文件目录，一次只能进入一层";
    private static final String CMD_ANNOTATION_LL = isEn() ? " show detail message for files" : " 显示当前目录中的文件的详细信息";
    private static final String CMD_ANNOTATION_LS = isEn() ? " show simple message for files" : " 显示当前目录中的文件的简要信息";
    private static final String CMD_ANNOTATION_MKDIR = isEn() ? " create new dir" : " 在当前目录下创建新目录";
    private static final String CMD_ANNOTATION_CATALOG_PATH = isEn() ? " display current path" : " 显示当前目录路径";

    private static final String CMD_ANNOTATION_START = isEn() ? " Start to Send Msg, Use <Ctrl + C> to stop." : " 开始发送日志, 使用「Ctrl + C」停止发送。";
    private static final String CMD_ANNOTATION_RM = isEn() ? " delete file" : " 删除当前目录中的某个文件";
    private static final String CMD_ANNOTATION_SHOW = isEn() ? " show current configuration" : " 展示当前配置信息";
    private static final String CMD_ANNOTATION_SWITCH = isEn() ? " switch, to open/close feature" : " 开关，用于打开/关闭某些功能";
    private static final String CMD_ANNOTATION_SAVE = isEn() ? " save current config to file" : " 保存当前配置信息";

    private static final String CMD_ANNOTATION_UPDATE = isEn() ? " use <update> to update config" : " 使用update来更新配置信息";
    private static final String UPDATE_PROTOCOL_HELP = isEn() ? " update send msg type(UDP, TCP, SNMPTRAP, HTTP, KAFKA)" : " 更新协议（UDP，TCP, SNMPTRAP, HTTP, KAFKA）";
    private static final String UPDATE_IP_HELP = isEn() ? " update target ip" : " 更新目标IP";
    private static final String UPDATE_PORT_HELP = isEn() ? " update target port" : " 更新目标PORT";
    private static final String UPDATE_TOTAL_HELP = isEn() ? " update msg total num, -1 is unlimited" : " 更新数据发送总量, -1 表示无限制";
    private static final String UPDATE_SPEED_HELP = isEn() ? " update msg send speed(EPS)" : " 更新数据发送速度（EPS）";
    private static final String UPDATE_FILE_HELP = isEn() ? " update log data file(file or dir)" : " 更新日志数据来源（文件或目录）";

    private static final String UPDATE_API_KEY_HELP = isEn() ? " update apiKey while using http type" : " 更新Http协议连接使用的ApiKey";
    private static final String UPDATE_SERVER_HELP = isEn() ? " update send address while using kafka type" : " 更新Kafka协议发送的服务地址";
    private static final String UPDATE_TOPIC_HELP = isEn() ? " update kafka topic while using kafka type" : " 更新Kafka协议发送的Topic";
    private static final String UPDATE_LOG_HEADER_HELP = isEn() ? " update log header" : " 更新发送的日志头信息";
    private static final String UPDATE_FILE_ENCODING_HELP = isEn() ? " update log file encoding" : " 更新日志数据来源文件编码";
    private static final String UPDATE_LOG_ENCODING_HELP = isEn() ? " update send log encoding" : " 更新发送日志的编码";
    public static final String UPDATE_AES_KEY_HELP = isEn() ? " update AES key while using AES" : " 更新AES密钥";
    public static final String UPDATE_AES_IV_HELP = isEn() ? " update initVector while using AES" : " 更新AES偏移量";
    public static final String UPDATE_AES_MODE_HELP = isEn() ? " update encrypt mode while using AES" : " 更新AES加密模式";
    public static final String UPDATE_AES_PADDING_HELP = isEn() ? " update padding type while using AES" : " 更新AES补码方式";
    private static final String SWITCH_AES_HELP = isEn() ? " open/close AES Encrypt" : " 打开/关闭AES加密";
    private static final String SWITCH_HEADER_HELP = isEn() ? " open/close Append Log Header" : " 打开/关闭添加日志头";
    private static final String MODULE_ANNOTATION_HEADER = isEn() ? " support commands(use <Tab> to complete)" : " 支持的命令如下（支持命令tab补全）：";
    public static final String HELP_CMD = FORMAT_HEADER +
            (isEn() ? BizConst.HELP_HELP_EN : BizConst.HELP_HELP) + LINE_BREAK +
            LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.HELP + ANNOTATION_LINE + CMD_ANNOTATION_HELP + LINE_BREAK +
            LINE_BREAK +
            MODULE_ANNOTATION_HEADER + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_CD + ANNOTATION_LINE + CMD_ANNOTATION_CD + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_PWD + ANNOTATION_LINE + CMD_ANNOTATION_CATALOG_PATH + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_LS + ANNOTATION_LINE + CMD_ANNOTATION_LS + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_LL + ANNOTATION_LINE + CMD_ANNOTATION_LL + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_RM + ANNOTATION_LINE + CMD_ANNOTATION_RM + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_MKDIR + ANNOTATION_LINE + CMD_ANNOTATION_MKDIR + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_START + ANNOTATION_LINE + CMD_ANNOTATION_START + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_UPDATE + ANNOTATION_LINE + CMD_ANNOTATION_UPDATE + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_AES_KEY + ANNOTATION_LINE + UPDATE_AES_KEY_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_AES_IV + ANNOTATION_LINE + UPDATE_AES_IV_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_AES_MODE + ANNOTATION_LINE + UPDATE_AES_MODE_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_AES_PADDING + ANNOTATION_LINE + UPDATE_AES_PADDING_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_PROTOCOL + ANNOTATION_LINE + UPDATE_PROTOCOL_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_IP + ANNOTATION_LINE + UPDATE_IP_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_PORT + ANNOTATION_LINE + UPDATE_PORT_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_TOTAL + ANNOTATION_LINE + UPDATE_TOTAL_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_SPEED + ANNOTATION_LINE + UPDATE_SPEED_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_FILE + ANNOTATION_LINE + UPDATE_FILE_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_API_KEY + ANNOTATION_LINE + UPDATE_API_KEY_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_SERVER + ANNOTATION_LINE + UPDATE_SERVER_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_TOPIC + ANNOTATION_LINE + UPDATE_TOPIC_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_LOG_HEADER + ANNOTATION_LINE + UPDATE_LOG_HEADER_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_FILE_ENCODING + ANNOTATION_LINE + UPDATE_FILE_ENCODING_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.UPDATE_LOG_ENCODING + ANNOTATION_LINE + UPDATE_LOG_ENCODING_HELP + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_SWITCH + ANNOTATION_LINE + CMD_ANNOTATION_SWITCH + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.SWITCH_AES + ANNOTATION_LINE + SWITCH_AES_HELP + LINE_BREAK +
            CMD_FORMAT_PREFIX + BizConst.SWITCH_HEADER + ANNOTATION_LINE + SWITCH_HEADER_HELP + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_SHOW + ANNOTATION_LINE + CMD_ANNOTATION_SHOW + LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.CMD_CONST_SAVE + ANNOTATION_LINE + CMD_ANNOTATION_SAVE + LINE_BREAK +
            LINE_BREAK +
            LINE_BREAK +
            MODULE_FORMAT_PREFIX + BizConst.QUIT + ANNOTATION_LINE + (isEn() ? BizConst.QUIT_HELP_EN : BizConst.QUIT_HELP) + LINE_BREAK;

}
