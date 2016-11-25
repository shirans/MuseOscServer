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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.eeg_server.utils.TimeUtils.*;

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


    public String convertTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        //format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(date);
    }

    void oscEvent(OscMessage msg) {
        long timetag = msg.timetag();
        String epocTimeMSHack = timeConversionHackString(timetag);
        String epocMsSpeck = convertOscTimeTagBySpecString(timetag);
        String serverCurrentTimestamp = currentMilliString();
        String ntp = getNTPtimeString(timetag);

        logger.info("hack timestamp: " + epocTimeMSHack);
        logger.info("current timestamp: " + serverCurrentTimestamp);
        logger.info("epocMsSpeck: " + epocMsSpeck);
        logger.info("ntp: " + ntp);



        Type type = Type.getValue(msg.addrPattern());
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        logger.info(f.format(msg.timetag()) + msg.addrPattern());

        String msgToAdd = null;
        switch (type) {
            case OTHER:
                return;
            case EEG:
                msgToAdd = MuseSignals.asString(msg, Type.EEG);
                break;
            case HORSE_SHOE:
                logger.info("signal quality:" + MuseSignals.asString(msg, Type.HORSE_SHOE));
                break;
            case STRICT:
                logger.info("is good: " + MuseSignals.asString(msg, Type.STRICT));
            case QUANTIZATION_EEG:
                logger.info("device Quantization:" + MuseSignals.parseQuantization(msg));
                break;
        }

        if (Strings.isNotEmpty(msgToAdd)) {
            messages.add(msgToAdd);
        }
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
    /**
     * // NTP representation
     String asBitsString = String.format("%032d", new BigInteger(Long.toBinaryString((long) timetag)));
     String first32 = asBitsString.substring(0,32);
     long secondsSince1900 = new BigInteger(first32, 2).longValue();
     String last32 = asBitsString.substring(33);
     long microsecondsSince1900 = new BigInteger(first32, 2).longValue();
     long ntp = secondsSince1900 + microsecondsSince1900;
     //(70*365 + 17)*86400 = 2208988800
     long unitNtpDelta = (long) (70 * 365 + 17) * 86400;
     long secondsAsMs = secondsSince1900*1000;
     long microSecondsAsMs = microsecondsSince1900/1000;
     long NtpMs =secondsAsMs + microSecondsAsMs;
     long s = secondsAsMs
     */

}
