package com.eeg_server.oddball;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @auter shiran on 20/08/2016.
 */
public class OddBallExperiment {

    private static final Logger logger = LogManager.getLogger(OddBallExperiment.class);
    static Boolean isFinished = false;
    private Queue<Pair<Long, Type>> events;

    public PlayingThread getPlayingThread() {
        return playingThread;
    }

    private PlayingThread playingThread;

    public void start() {
        playingThread = new PlayingThread();
        this.events = playingThread.getEvents();
        playingThread.start();
    }

    public Boolean isFinished() {
        return isFinished;
    }

    public void dumpResults() throws IOException {
        String path = FileUtil.getPath();

        List<String> lines =
                playingThread.getEvents().stream().map(x -> x.getLeft() + "," + x.getRight()).collect(Collectors.toList());
        Files.write(Paths.get(path).resolve("oddBallResults"), lines);

    }

    public int getEventsSize() {
        return events.size();
    }
}
