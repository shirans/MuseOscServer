package com.eeg_server.eegServer;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author shiran Schwartz on 01/10/2016.
 */
public enum Type {
    EEG, QUANTIZATION_EEG, HORSE_SHOE,STRICT,RAW,ALPHA,BETA,GAMMA,DELTA,THETA, OTHER;
    private static Map<String, Type> types;

    static {
        types = Maps.newLinkedHashMapWithExpectedSize(4);
        types.put("/muse/eeg", EEG);
        types.put("/muse/eeg/quantization", QUANTIZATION_EEG);
        types.put("/muse/elements/horseshoe", HORSE_SHOE);
        types.put("/muse/elements/is_good", STRICT);
        types.put("muse/elements/raw",RAW);
        types.put("muse/elements/raw",RAW);
        types.put("/muse/elements/alpha",ALPHA);
        types.put("/muse/elements/beta",BETA);
        types.put("/muse/elements/gamma",GAMMA);
        types.put("/muse/elements/delta",DELTA);
        types.put("/muse/elements/theta",THETA);
    }

    public static Type getValue(String name) {

        Type type = types.entrySet().stream().filter(x -> name.startsWith(x.getKey())).findFirst().map(Map.Entry::getValue).orElse(OTHER);
        if (type == null) {
            return OTHER;
        }
        return type;
    }
}
