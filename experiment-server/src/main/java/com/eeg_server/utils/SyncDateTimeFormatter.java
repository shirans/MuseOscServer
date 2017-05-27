package com.eeg_server.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shiran Schwartz on 25/11/2016.
 */
public class SyncDateTimeFormatter {
    private SimpleDateFormat dateTimeFormatter;
    public SyncDateTimeFormatter(String format) {
        this.dateTimeFormatter = new SimpleDateFormat(format);
    }

    public synchronized String format(long time){
        return dateTimeFormatter.format(time);
    }

    public synchronized String format(Date date){
        return dateTimeFormatter.format(date);
    }
}
