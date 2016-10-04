package com.eeg_server.eegServer;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @auter shiran Schwartz on 01/10/2016.
 */
public enum Type {
    EEG, QUANTIZATION_EEG, HORSE_SHOE,STRICT, OTHER;
    private static Map<String, Type> types;

    static {
        types = Maps.newLinkedHashMapWithExpectedSize(4);
        types.put("/muse/eeg", EEG);
        types.put("/muse/eeg/quantization", QUANTIZATION_EEG);
        types.put("/muse/elements/horseshoe", HORSE_SHOE);
        types.put("/muse/elements/is_good", STRICT);
    }

    public static Type getValue(String name) {
        Type type = types.get(name);
        if (type == null) {
            return OTHER;
        }
        return type;
    }
}
