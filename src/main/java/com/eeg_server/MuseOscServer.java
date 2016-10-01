package com.eeg_server;

import com.eeg_server.oscP5.OscMessage;
import com.eeg_server.oscP5.OscP5;
import com.eeg_server.oscP5.OscProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shiran on 17/07/2016.
 */
public class MuseOscServer {
    private static MuseOscServer museOscServer;
    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
    private static int recvPort = 5001;
    private OscP5 museServer;

    public static void main(String[] args) {
        museOscServer = new MuseOscServer();
        museOscServer.museServer = new OscP5(museOscServer, "10.0.0.6", 5001, OscProperties.MULTICAST);
    }

    void oscEvent(OscMessage msg) {
        StringBuilder out = new StringBuilder();
        out.append(msg);
        out.append(" , ");

        if (msg.checkAddrPattern("/muse/eeg")) {
            eegData(msg, out);

//            System.out.println(out.toString());
        }
    }

    void registerDispose() {
        System.out.println("registerDispose");
    }


    private void eegData(OscMessage msg, StringBuilder out) {
        out.append(format.format(new Date(new Long(msg.get(4).intValue()) * 1000 +
                new Double(msg.get(5).intValue() * 0.001).intValue()))).append("\t");
        out.append("Left Ear: ").append(msg.get(0).floatValue()).append("\t");
        out.append("Left Forehead: ").append(msg.get(1).floatValue()).append("\t");
        out.append("Forehead :").append(msg.get(2).floatValue()).append("\t");
        out.append("Right Ear: ").append(msg.get(3).floatValue());

    }


    // how to process brain waves: https://github.com/shevek/dblx/blob/4734d4617bc042895a28b14d26fc53fd780a9018/dblx-iface-muse/src/main/java/org/anarres/dblx/iface/muse/net/MuseConnect.java

}
