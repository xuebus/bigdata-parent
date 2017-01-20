// ----------------------------------------------------------------------------
// Copyright 2010-2013 Yangling Tech. Co. Ltd. 
// All Rights Reserved
// ----------------------------------------------------------------------------
//
// ----------------------------------------------------------------------------
// Description:
//  日志工具
// ----------------------------------------------------------------------------
// Change History:
// 2010-9-23 Zhbwang
//      -Initial release
//
// ----------------------------------------------------------------------------
package com.zhbwang.bigdata.lib.tools;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.xml.DOMConfigurator;

import java.text.MessageFormat;

/**
 * @project: gps
 * @description: LogUtil
 * @author: Zhibin Wang
 * @date: 2010-9-23
 * @version: 1.00
 */
public class LogUtil {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();

    public static final String LOG4J_FILE = "log4j.file";
    public static final String LOG4J_FILE_REMARK = "log4j 配置文件，需要调用reinit方法使之生效";

    public static void reinit(String file) {
        DOMConfigurator.configure(file);
        LogUtil.levelInfo(1, "Reinit log4j file : {0}", file);
    }

    public static final String LOG4J_LEVEL = "log4j.level";
    public static final String LOG4J_LEVEL_REMARK = "log4j日志输出等级，level越高，输出越少，Default: 2";
    // 分级输出日志， level越高，优先级越高
    private static int level = 2;

    public static void setLogLevel(int levelSet) {
        if(levelSet>0){
            level = levelSet;
        }
    }
    public static int getLogLevel() {
        return level;
    }

    public static void levelInfo(int logLevel,
                                 String pattern,
                                 Object... arguments) {

        if (logLevel <= 0) {
            return;
        }
        if (level >= logLevel) {
            String pre = "";
            for (int i = 0; i < logLevel; i++) {
                pre += "#";
            }
            info(pre + pattern, arguments);
        }
    }

    public static void levelInfo(int logLevel,
                                 String pattern) {
        if (logLevel <= 0) {
            return;
        }
        if (level >= logLevel) {
            String pre = "";
            for (int i = 0; i < logLevel; i++) {
                pre += "#";
            }
            info(pre + pattern);
        }
    }
//
//    public static void levelInfo_1(String pattern,
//                                   Object... arguments) {
//        if (level >= 1) {
//            info("#" + pattern, arguments);
//        }
//    }
//
//    public static void levelInfo_1(String pattern) {
//        if (level >= 1) {
//            info("#" + pattern);
//        }
//    }
//
//    public static void levelInfo_2(String pattern,
//                                   Object... arguments) {
//        if (level >= 2) {
//            info("##" + pattern, arguments);
//        }
//    }
//
//    public static void levelInfo_2(String pattern) {
//        if (level >= 2) {
//            info("##" + pattern);
//        }
//    }
//
//    public static void levelInfo_3(String pattern,
//                                   Object... arguments) {
//        if (level >= 3) {
//            info("###" + pattern, arguments);
//        }
//    }
//
//    public static void levelInfo_3(String pattern) {
//        if (level >= 3) {
//            info("###" + pattern);
//        }
//    }
//
//    public static void levelInfo_4(String pattern,
//                                   Object... arguments) {
//        if (level >= 4) {
//            info("####" + pattern, arguments);
//        }
//    }
//
//    public static void levelInfo_4(String pattern) {
//        if (level >= 4) {
//            info("####" + pattern);
//        }
//    }
//
//    public static void levelInfo_5(String pattern,
//                                   Object... arguments) {
//        if (level >= 5) {
//            info("####" + pattern, arguments);
//        }
//    }
//
//    public static void levelInfo_5(String pattern) {
//        if (level >= 5) {
//            info("####" + pattern);
//        }
//    }

    public static void console(String pattern) {
        System.out.println(pattern);
    }

    public static void console(String pattern,
                               Object... arguments) {
        console(format(pattern, arguments));
    }

    public static void trace(Object message) {
        if (logger.isTraceEnabled()) {
            forcedLog(logger, Level.TRACE, message);
        }
    }

    public static void trace(Object message,
                             Throwable t) {
        if (logger.isTraceEnabled()) {
            forcedLog(logger, Level.TRACE, message, t);
        }
    }

    public static void trace(String pattern,
                             Object... arguments) {
        if (logger.isTraceEnabled()) {
            forcedLog(logger, Level.TRACE, format(pattern, arguments));
        }
    }

    public static void trace(String pattern,
                             Throwable t,
                             Object... arguments) {
        if (logger.isTraceEnabled()) {
            forcedLog(logger, Level.TRACE, format(pattern, arguments), t);
        }
    }

    public static void debug(Object message) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, message);
        }
    }

    public static void debug(Object message,
                             Throwable t) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, message, t);
        }
    }

    public static void debug(String pattern,
                             Object... arguments) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, arguments));
        }
    }

    public static void debug(String pattern,
                             Throwable t,
                             Object... arguments) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, arguments), t);
        }
    }

    public static void info(Object message) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, message);
        }
    }

    public static void info(Object message,
                            Throwable t) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, message, t);
        }
    }

    public static void info(String pattern,
                            Object... arguments) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, arguments));
        }
    }

    public static void info(String pattern,
                            Throwable t,
                            Object... arguments) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, arguments), t);
        }
    }

    public static void warn(Object message) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, message);
        }
    }

    public static void warn(Object message,
                            Throwable t) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, message, t);
        }
    }

    public static void warn(String pattern,
                            Object... arguments) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, arguments));
        }
    }

    public static void warn(String pattern,
                            Throwable t,
                            Object... arguments) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, arguments), t);
        }
    }

    public static void error(Object message) {
        if (logger.isEnabledFor(Level.ERROR)) {
            forcedLog(logger, Level.ERROR, message);
        }
    }

    public static void error(Object message,
                             Throwable t) {
        if (logger.isEnabledFor(Level.ERROR)) {
            forcedLog(logger, Level.ERROR, message, t);
        }
    }

    public static void error(String pattern,
                             Object... arguments) {
        if (logger.isEnabledFor(Level.ERROR)) {
            forcedLog(logger, Level.ERROR, format(pattern, arguments));
        }
    }

    public static void error(String pattern,
                             Throwable t,
                             Object... arguments) {
        if (logger.isEnabledFor(Level.ERROR)) {
            forcedLog(logger, Level.ERROR, format(pattern, arguments), t);
        }
    }

    public static void fatal(Object message) {
        if (logger.isEnabledFor(Level.FATAL)) {
            forcedLog(logger, Level.FATAL, "FATAL EXIT: " + message);
        }
        System.exit(1);
    }

    public static void fatal(Object message,
                             Throwable t) {
        if (logger.isEnabledFor(Level.FATAL)) {
            forcedLog(logger, Level.FATAL, "FATAL EXIT: " + message, t);
        }
        System.exit(1);
    }

    public static void fatal(String pattern,
                             Object... arguments) {
        if (logger.isEnabledFor(Level.FATAL)) {
            forcedLog(logger,
                    Level.FATAL,
                    "FATAL EXIT: " + format(pattern, arguments));
        }
        System.exit(1);
    }

    public static void fatal(String pattern,
                             Throwable t,
                             Object... arguments) {
        if (logger.isEnabledFor(Level.FATAL)) {
            forcedLog(logger,
                    Level.FATAL,
                    "FATAL EXIT: " + format(pattern, arguments),
                    t);
        }
        System.exit(1);
    }

    public static void assertLog(boolean assertion,
                                 String message) {
        if (!assertion) {
            forcedLog(logger, Level.ERROR, message);
        }
    }

    private static void forcedLog(org.apache.log4j.Logger logger,
                                  Level level,
                                  Object message) {
        logger.callAppenders(new LoggingEvent(FQCN,
                logger,
                level,
                message,
                null));
    }

    private static void forcedLog(org.apache.log4j.Logger logger,
                                  Level level,
                                  Object message,
                                  Throwable t) {
        logger.callAppenders(new LoggingEvent(FQCN, logger, level, message, t));
    }

    private static String format(String pattern,
                                 Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    private static final String FQCN;

    static {
        FQCN = LogUtil.class.getName();
    }
}
