package com.eeg_server.experiment.oddball;

import com.eeg_server.experiment.Experiment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Shiran Schwartz on 20/08/2016.
 */
public class OddBallExperiment extends Experiment {

    private static final Logger logger = LogManager.getLogger(OddBallExperiment.class);

    public OddBallExperiment(int sleepFactor, int interval, int numIterations, int randomize) {
        super(sleepFactor, interval, numIterations, randomize);
    }
}
