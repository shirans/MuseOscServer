package com.eeg_server.eegServer;

import com.eeg_server.oddball.FileUtil;
import com.eeg_server.oddball.OddBallExperiment;
import com.eeg_server.oscP5.OscMessage;
import com.eeg_server.oscP5.OscP5;
import com.eeg_server.oscP5.OscProperties;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @auter shiran on 20/08/2016.
 */
public class MuseEegServer implements EegServer {

    private static final Logger logger = LogManager.getLogger(OddBallExperiment.class);
    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
    private static int recvPort = 6001;
    private OscP5 oscP5;
    private List<String> messages = Lists.newLinkedList();

    public void startRecord() {
        this.oscP5 = new OscP5(this, "10.0.0.6",5001, OscProperties.MULTICAST);
        logger.info("MuseEegServer starts recording");
    }

    void oscEvent(OscMessage msg) {
        StringBuilder out = new StringBuilder();
        out.append(msg);
        out.append(" , ");

        if (msg.checkAddrPattern("/muse/eeg")) {
            out.append(System.currentTimeMillis()).append(": ");
            out.append("Left Ear:").append(msg.get(0).floatValue()).append("\t");
            out.append("Left Forehead:").append(msg.get(1).floatValue()).append("\t");
            out.append("Forehead:").append(msg.get(2).floatValue()).append("\t");
            out.append("Right Ear:").append(msg.get(3).floatValue());
            messages.add(out.toString());
        }
    }

    public void stopRecord() {
        oscP5.dispose();
        logger.info("MuseEegServer stops recording");
    }

    public void close() {

    }

    @Override
    public int getEventsSize() {
        return messages.size();
    }

    @Override
    public void dumpResults() {
        String path = FileUtil.getPath();

        try {
            Files.write(Paths.get(path).resolve("EegData"), messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
