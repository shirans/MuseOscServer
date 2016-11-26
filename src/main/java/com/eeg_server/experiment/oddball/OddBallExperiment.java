package com.eeg_server.experiment.oddball;

import com.eeg_server.experiment.Experiment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @auter Shiran Schwartz on 20/08/2016.
 */
public class OddBallExperiment extends Experiment {

    private static final Logger logger = LogManager.getLogger(OddBallExperiment.class);

    public OddBallExperiment(int sleepFactor, int numIterations, int randomize) {
        super(sleepFactor, numIterations, randomize);
    }
}
