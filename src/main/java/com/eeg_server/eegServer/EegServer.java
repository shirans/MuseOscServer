package com.eeg_server.eegServer;

/**
 * @Author shiran on 20/08/2016.
 */
public interface EegServer {

    void startRecord();

    void stopRecord();

    void close();

    int getEventsSize();

    void dumpResults(String fileName);
}
