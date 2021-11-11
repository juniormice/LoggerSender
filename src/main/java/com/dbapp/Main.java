package com.dbapp;

import com.dbapp.bean.SenderConfig;
import com.dbapp.exception.SocException;
import com.dbapp.service.CommandService;
import com.dbapp.service.DisplayService;
import com.dbapp.service.HandlerServer;
import com.dbapp.service.SocCommonService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {

    private static boolean RUNNING = true;

    static {
        Logger.getRootLogger().setLevel(Level.OFF);
    }


    public static void main(String[] args) throws Exception {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .completer(CommandService.completer())
                .build();

        DisplayService.welcome();
        String prompt = "soc> ";
        SocCommonService.init();
        if (SenderConfig.isEn()) {
            System.out.println("Auto Load Config Success!" + SenderConfig.description());
            System.out.println("You Can Use <Tab> To Complete command, Or use <help> to show all!");
        } else {
            System.out.println("自动加载配置成功！" + SenderConfig.description());
            System.out.println("您可以使用「Tab」进行命令补全，或者您可以输入「help」以查看全部的命令!");
        }
        while (RUNNING) {
            String line;
            try {
                line = StringUtils.trim(lineReader.readLine(prompt));
                if (StringUtils.isNotEmpty(line)) {
                    try {
                        HandlerServer.handleCmd(line);
                    } catch (SocException socE) {
                        DisplayService.socException(socE.getMessage());
                    }
                }
            } catch (EndOfFileException | UserInterruptException e) {
                HandlerServer.handleCmd("stop");
            } catch (ArithmeticException ignored) {
            } catch (Exception e) {
                DisplayService.error(e);
            }
        }
        DisplayService.exit();
    }

    public static void exit() {
        RUNNING = false;
        System.exit(0);
    }
}
