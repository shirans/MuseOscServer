package com.eeg_server.eegServer;

/**
 * @auter shiran Schwartz on 26/11/2016.
 */
public class EegData {
    public long getTimeTagNtp() {
        return timeTagNtp;
    }

    public long getServerTimeTag() {
        return serverTimeTag;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public String getType() {
        return type;
    }

    final long timeTagNtp;
    final long serverTimeTag;
    final Object[] arguments;
    final String type;

    public EegData(String type, long timeTagNtp, long serverTimeTag, Object[] arguments) {
       this.type = type;
        this.timeTagNtp = timeTagNtp;
        this.serverTimeTag = serverTimeTag;
        this.arguments = arguments;
    }
}
