package com.eeg_server.eegServer;

/**
 * @auter shiran on 20/08/2016.
 */
public interface EegServer {

    void startRecord();

    void stopRecord();

    void close();
}
