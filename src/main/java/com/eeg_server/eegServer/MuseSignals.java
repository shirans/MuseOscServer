package com.eeg_server.eegServer;

import com.eeg_server.oscP5.OscMessage;
import com.eeg_server.utils.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;

/**
 * @auter shiran Schwartz on 01/10/2016.
 */
public class MuseSignals {
    private static final Logger logger = LogManager.getLogger(MuseSignals.class);

    /**
     * This is the EEG data converted to microvolts.  Presets 10, 12, 14, 15 emit data at 220Hz.
     * Four channel (10bits): ffff
     * Position 1: Left Ear(TP9), Range: 0.0 - 1682.815 in microvolts
     * Position 2: Left Forehead(FP1), Range: 0.0 - 1682.815 in microvolts
     * Position 3: Right Forehead(FP2), Range: 0.0 - 1682.815 in microvolts
     * Position 4: Right Ear(TP10), Range: 0.0 - 1682.815 in microvolts
     * If the --osc_timestamp option is used, there are 2 extra fields appended to these messages:
     * integer: Number of seconds since 1970 when this event occurred
     * integer: Number of microseconds within that second
     * If you use the --no-scale command-line option, you get the proprietary raw data. We do not recommend you use this as it may change at any time. Keep in mind that the gain will be slightly different for every Muse, as the value of the resistors that determine it can change by up to 1%. So the gain could be anywhere from about 1923 to 2001. So the uV/bit for the ADC will vary from Muse to Muse.
     * <p>
     * Four channel (10 bit resolution): ffff
     * Position 1: Left Ear, Range: 0.0-1023.0, measure of voltage of EEG reading. To get microvolts: uV=(x/1023)*3.3V*(1/A)*1000000; x= this value; A=gain of AFE(Analog Front End)=1961. Max microvolts: 1682, Min microvolts: 0
     * Position 2: Left Forehead, Range: 0-1023
     * Position 3: Right Forehead, Range: 0-1023
     * Position 4: Right Ear, Range: 0-1023
     */
    static float[] parseEeg(OscMessage msg) {
        float[] floats = new float[5];
        for (int i = 0; i < 5; i++) {
            floats[i] = msg.get(i).floatValue();
        }
        // 0 = TP9 - Left Ear
        // 1 = AF7 - Left Forehead
        // 2 = AF8 - Forehead
        // 3 = TP10 - Right Ear
        // 4 = Right AUX
        return floats;
    }

    static int parseQuantization(OscMessage msg) {
        return msg.get(0).intValue();
    }


    static String asString(Object[] arguments) {
        try {
            String array = Arrays.toString(arguments);
            if (arguments.length > 0) {
                return array.substring(1,array.length()-1);
            }
            return array;
        } catch (Exception e) {
            logger.error("could not parse message" + Arrays.toString(arguments) + " num args:" , e);
            return Strings.EMPTY;
        }
    }

    public static void calcQuality(OscMessage msg) {

    }

    public static String asString(OscMessage msg, Type t) {
        return String.format("%s,%s,%s", TimeUtils.getNTPtimeString(msg.timetag()),t.name(),asString(msg.arguments()));
    }
}
