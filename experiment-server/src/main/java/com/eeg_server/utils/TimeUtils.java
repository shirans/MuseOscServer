package com.eeg_server.utils;

import org.apache.commons.net.ntp.TimeStamp;

/**
 * @author shiran Schwartz on 25/11/2016.
 */
public class TimeUtils {
    public static final SyncDateTimeFormatter FORMAT = new SyncDateTimeFormatter("yyyy-MM-dd HH:mm:ss.SSSSSS");
    public static final long SEVENTY_YEARS_INSECONDS = (70 * 365 + 17) * 86400L;

    /***
     * http://forum.choosemuse.com/t/stream-raw-data-from-muse2016-to-mac/944/8
     * FYI, after much testing I have found out that the timeTag produced by oscP5 is incorrect.
     * The millisecond bytes are a fractional component of the maximum value (4294967296) as per this bug fix:
     * https://github.com/mrRay/vvopensource/commit/9963270382f82ff40ecb47e45af90f2bf431d459
     * But oscP5 calculates it as: final long secsFractional = ((theTime % 1000) << 32) / 1000; which is incorrect.
     * <p>
     * I have tried coding a work around without altering the oscP5 source, but processing encounters floating point
     * <p>
     * This: long epocTimeMS = (timeTag + 8959209420479266816L)/4294967L;
     * .. is a terrible hack to get a semi accurate time stamp from oscP5!
     * oscP5 is fundamentally flawed in its time stamp implementation.
     * If you want accurate time stamps, you can't this code, or oscP5 at all.
     * To send the data, I am using the VVOSC library for iOS Muse Monitor, and JavaOSC for the Android version.
     * I don't code for Mac's, but if you want some windows app code to look at,
     * I've written a small test app in Visual Studio 2015 in C#.Net using the SharpOSC library.
     * number rounding errors due to the very large numbers involved and so accuracy is lost.
     */
    public static long timeConversionHack(long timetag) {
        return (timetag + 8959209420479266816L) / 4294967L;
    }

    public static String timeConversionHackString(long timetag) {
        return FORMAT.format(timeConversionHack(timetag));
    }

    /**
     * 64-bit big-endian fixed-point time tag, semantics defined below
     * Time tags are represented by a 64 bit fixed point number.
     * The first 32 bits specify the number of seconds since midnight on January 1, 1900, and the last 32 bits specify fractional parts of a second to a precision of about 200 picoseconds.
     * This is the representation used by Internet NTP timestamps.The time tag value consisting of 63 zero bits followed by a one in the least signifigant bit is a special case meaning "immediately.
     *
     * @param timetag
     * @return
     */
    public static long convertOscTimeTagBySpec(long timetag) {
        long secondsSince1900 = timetag >>> 32 - SEVENTY_YEARS_INSECONDS ;
        long fracionalPicoSecs = timetag & 0xffffffffL;
        // this is wrong, look at ntp
        return secondsSince1900 * 1000 + fracionalPicoSecs / 1000000000000L ;
    }

    public static String convertOscTimeTagBySpecString(long timetag) {
        return FORMAT.format(convertOscTimeTagBySpec(timetag));
    }

    public static String currentMilliString() {
        return FORMAT.format(System.currentTimeMillis());
    }

    public static String format(long time) {
        return FORMAT.format(time);
    }

    public static long getNTPtime(long timetag) {
        return new org.apache.commons.net.ntp.TimeStamp(timetag).getTime();
    }

    public static String getNtpTimeString(long timetag) {
        return FORMAT.format(new TimeStamp(timetag).getDate());
    }
}




