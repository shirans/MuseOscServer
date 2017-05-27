package com.eeg_server.eegServer;

import com.eeg_server.experiment.oddball.FileUtils;
import com.eeg_server.experiment.oddball.OddBallExperiment;
import com.eeg_server.oscP5.OscMessage;
import com.eeg_server.oscP5.OscP5;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.eeg_server.experiment.oddball.FileUtils.NEW_LINE;

//TODO: parse 	/muse/eeg/quantization,
/**
 * @author shiran on 20/08/2016.
 */
public class MuseEegServer implements EegServer {

    private static final Logger logger = LogManager.getLogger(OddBallExperiment.class);
    private static final String HEADER = "Timetag Ntp,Server Timestamp,Raw Timetag, Raw Server Timestamp,Data Type,data";
    private OscP5 oscP5;
    private List<EegData> messages = Lists.newLinkedList();


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

    Set<String> others = Sets.newHashSet();
    void oscEvent(OscMessage msg) {
        String name = msg.addrPattern();
        Type type = Type.getValue(name);

        String msgToAdd = null;
        switch (type) {
            case OTHER:
                String str = msg.addrPattern();
                if (others.contains(str)) {
                    return;
                }
                logger.debug("got event of type:" + str + "with argumetns:" + Arrays.toString(msg.arguments()));
                others.add(str);

                return;
            case HORSE_SHOE:
//                logger.info("signal quality:" + MuseSignals.asString(msg, Type.HORSE_SHOE));
                return;
            case STRICT:
                logger.info("is good: " + MuseSignals.asString(msg, Type.STRICT));
                return;
            case QUANTIZATION_EEG:
                logger.info("device Quantization:" + MuseSignals.parseQuantization(msg));
            case EEG:
            default:
                long timetag = msg.timetag();
                long serverCurrentTimestamp = System.currentTimeMillis();
                EegData data = new EegData(type.name(), timetag, serverCurrentTimestamp, msg.arguments());
                messages.add(data);
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
    public void dumpResults(String filename) {
        String fileName = FileUtils.resolve(filename + ".csv").toString();
        logger.info(String.format("writing data to file at path: %s", fileName));
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(HEADER);
            writer.write(NEW_LINE);
            for (EegData eegData : messages) {
                writer.append(FileUtils.formatCsvLine(eegData));
                writer.append(NEW_LINE);
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            logger.error(String.format("could not write to path: %s", fileName), e);
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
