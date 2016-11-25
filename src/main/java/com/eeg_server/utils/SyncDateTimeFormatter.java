package com.eeg_server.utils;

import java.text.SimpleDateFormat;

/**
 * @auter shiran Schwartz on 25/11/2016.
 */
public class SyncDateTimeFormatter {
    private SimpleDateFormat dateTimeFormatter;
    public SyncDateTimeFormatter(String format) {
        this.dateTimeFormatter = new SimpleDateFormat(format);
    }

    public synchronized String format(long time){
        return dateTimeFormatter.format(time);
    }
}
