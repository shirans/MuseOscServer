package com.eeg_server.oddball;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @auter Shiran Schwartz on 20/08/2016.
 */
public class FileUtil {

    private static final Logger logger = LogManager.getLogger(FileUtil.class);
    private static SimpleDateFormat FILE_FORMAT_NAME = new SimpleDateFormat("yy-MM-dd_HH-mm");

    private static String currFileName = FILE_FORMAT_NAME.format(new Date());

    private static String eegResultsPath = "/Users/shiran/out/";
    private static String path;
    private static Boolean init = false;
    private static final Object sync = new Object();
    private static void init() {
        if (!init) {
            synchronized (sync) {
                if (!init) {
                    path = eegResultsPath + currFileName;
                    boolean created = new File(path).mkdir();
                    if (!created) {
                        logger.error("could not create a new dir:" + path);
                    }
                    init = true;
                }
            }
        }
    }

    public static String getPath() {
        init();
        return path;
    }

}
