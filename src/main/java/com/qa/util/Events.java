package com.qa.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Events {
    private Events() {}
    private static org.slf4j.Logger logWriter = LoggerFactory.getLogger(Events.class.getName());

    /**
     * This method is used to mark the start of a test case in the log file.
     *
     * @param sTestCaseName - Name of the test case to be started
     */
    public static void startTestCase(final String sTestCaseName) {
        info("****************************************************************************************");
        info("$$$$$$$$$$$$$$$$$$$$$  " + sTestCaseName + "  $$$$$$$$$$$$$$$$$");
        info("****************************************************************************************");
    }

    /**
     * This method is used to mark the start of a use case in the log file.
     *
     * @param sUseCaseName - Name of the used case to be started
     */
    public static void startUseCase(final String sUseCaseName) {
        info("****************************************************************************************");
        info("#####################  " + sUseCaseName + "  #################");
        info("****************************************************************************************");
    }

    /**
     * This method is used to mark the end of a previously started test case in the log file.
     *
     * @param sTestCaseName - Name of the test case to be ended
     */
    public static void endTestCase(final String sTestCaseName) {
        info("****************************************************************************************");
        info("XXXXXXXXXXXXXXXXXXXXXXX  " + "--TESTCASE-"+sTestCaseName+"-END--" + "  XXXXXXXXXXXXXX");
        info("****************************************************************************************");
    }

    /**
     * This method is used to mark the end of a previously started use case in the log file.
     *
     * @param sUseCaseName - Name of the use case to be ended
     */
    public static void endUseCase(final String sUseCaseName) {
        info("****************************************************************************************");
        info("#####################  " + "--USE--CASE--END--" + "  ################");
        info("****************************************************************************************");
    }

    /**
     * This method is used to print INFO messages in the log file.
     *
     * @param message - message to be printed in the log file
     */
    public static void info(final String message) {
        logWriter.info("Thread-id-"+ Thread.currentThread().getId() +": " + message);
        //    	System.out.println(message);
    }

    public static void info(final String message,boolean printOnConsole) {
        logWriter.info(message);
        if(true) {
            System.out.println(message);
        }
    }

    /**
     * This method is used to print WARNING messages in the log file.
     *
     * @param message - message to be printed in the log file
     */
    public static void warn(final String message) {
        logWriter.warn(message);
        //        System.out.println(message);
    }

    /**
     * This method is used to print ERROR messages in the log file.
     *
     * @param message - message to be printed in the log file
     */
    public static void error(final String message) {
        logWriter.error("Thread-id-"+ Thread.currentThread().getId() +": " + message);
        //        System.out.println(message);
    }

    /**
     * This method is used to print ERROR messages and exception in the log file.
     *
     * @param message - message to be printed in the log file
     */
    public static void error(final String message, final Throwable t) {
        logWriter.error(message, t);
    }

    /**
     * This method is used to print DEBUG messages in the log file.
     *
     * @param message - message to be printed in the log file.
     */
    public static void debug(final String message) {
        logWriter.debug(message);
    }
}
