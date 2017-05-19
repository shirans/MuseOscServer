package com.eeg_server.eegServer;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author shiran Schwartz on 01/10/2016.
 */
public enum Type {
    EEG, QUANTIZATION_EEG, HORSE_SHOE, STRICT, RAW,
    ALPHA_ABSOLUTE, BETA_ABSOLUTE, GAMMA_ABSOLUTE, DELTA_ABSOLUTE, THETA_ABSOLUTE,
    ALPHA_RELATIVE, BETA_RELATIVE, GAMMA_RELATIVE, THETA_RELATIVE, DELTA_RELATIVE, OTHER;
    private static Map<String, Type> types;

    static {
        types = Maps.newLinkedHashMapWithExpectedSize(11);
        types.put("/muse/eeg", EEG);
        types.put("/muse/eeg/quantization", QUANTIZATION_EEG);
        types.put("/muse/elements/horseshoe", HORSE_SHOE);
        types.put("/muse/elements/is_good", STRICT);

        types.put("muse/elements/raw", RAW);

        types.put("/muse/elements/alpha_absolute", ALPHA_ABSOLUTE);
        types.put("/muse/elements/beta_absolute", BETA_ABSOLUTE);
        types.put("/muse/elements/gamma_absolute", GAMMA_ABSOLUTE);
        types.put("/muse/elements/delta_absolute", DELTA_ABSOLUTE);
        types.put("/muse/elements/theta_absolute", THETA_ABSOLUTE);

        types.put("/muse/elements/alpha_relative", ALPHA_RELATIVE);
        types.put("/muse/elements/beta_relative", BETA_RELATIVE);
        types.put("/muse/elements/gamma_relative", GAMMA_RELATIVE);
        types.put("/muse/elements/delta_relative", DELTA_RELATIVE);
        types.put("/muse/elements/theta_relative", THETA_RELATIVE);
    }

    public static Type getValue(String name) {
        Type type = types.get(name);
        if (type == null) {
            return OTHER;
        }
        return type;
    }
}
