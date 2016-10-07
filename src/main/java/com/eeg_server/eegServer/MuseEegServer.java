package com.eeg_server.eegServer;

import com.eeg_server.oddball.FileUtil;
import com.eeg_server.oddball.OddBallExperiment;
import com.eeg_server.oscP5.OscMessage;
import com.eeg_server.oscP5.OscP5;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @auter shiran on 20/08/2016.
 */
public class MuseEegServer implements EegServer {

    private static final Logger logger = LogManager.getLogger(OddBallExperiment.class);
    private OscP5 oscP5;
    private List<String> messages = Lists.newLinkedList();


    public static void main(String args[]) throws InterruptedException {
        new MuseEegServer().startRecord();
    }

    @Override
    public void startRecord() {
        this.oscP5 = new OscP5(this, 5001);
        logger.info("MuseEegServer starts recording");
    }

    void oscEvent(OscMessage msg) {
        long currTime = System.currentTimeMillis();
        Type type = Type.getValue(msg.addrPattern());
//        logger.info(msg.addrPattern());
        String msgToAdd = null;
        switch (type) {
            case OTHER:
                return;
            case EEG:
                msgToAdd = parse(currTime, msg, Type.EEG);
                break;
            case HORSE_SHOE:
                logger.info("signal quality:" + parse(currTime,msg, Type.HORSE_SHOE));
                break;
            case STRICT:
                logger.info("is good: " + parse(currTime,msg,Type.STRICT));
            case QUANTIZATION_EEG:
                logger.info("device Quantization:" + MuseSignals.parseQuantization(msg));
                break;
        }

        if (Strings.isNotEmpty(msgToAdd)) {
            messages.add(msgToAdd);
        }
    }

    private String parse(long currTime, OscMessage msg, Type type) {
        return MuseSignals.asString(currTime, msg.arguments(),type);
    }

    @Override
    public void stopRecord() {
        oscP5.dispose();
        logger.info("MuseEegServer stops recording");
    }

    @Override
    public void close() {

    }

    @Override
    public int getEventsSize() {
        return messages.size();
    }

    @Override
    public void dumpResults() {
        String path = FileUtil.getPath();
        logger.info("saving results to path:" + path);
        try {
            Files.write(Paths.get(path).resolve("EegData"), messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // how to process brain waves: https://github.com/shevek/dblx/blob/4734d4617bc042895a28b14d26fc53fd780a9018/dblx-iface-muse/src/main/java/org/anarres/dblx/iface/muse/net/MuseConnect.java

}
