import datetime
import matplotlib.pyplot as plt
import numpy as np
import csv
from os import listdir
from os.path import isfile
import os

import time

from utils.log import get_logger

logger = get_logger('InputParser')


class InputParser:
    def __init__(self, folder_path):
        self.folderPath = folder_path
        for f in listdir(folder_path):
            if isfile:
                filename, file_extension = os.path.splitext(f)
                if file_extension == '.txt':
                    self.experimentQues = os.path.join(folder_path, f)
                elif file_extension == '.csv':
                    self.experimentData = os.path.join(folder_path, f)
        expType = os.path.basename(folder_path).split('_')
        self.date = expType[0]
        self.time = expType[1]
        self.type = expType[2]
        self.num_rows = self.num_rows(self.experimentData)
        logger.info("num rows: " + str(self.num_rows))
        logger.info("starting to parse experiment data from:" + self.experimentData)
        with open(self.experimentData, 'rU') as csvfile:
            reader = csv.reader(csvfile, delimiter=csv.excel.delimiter)
            columns = ['Timetag Ntp', 'Server Timestamp', 'Raw Timetag', ' Raw Server Timestamp', 'Data Type', 'data']
            self.eeg_data = self.appedEegOneByOne(reader)
        logger.info("created data: " + str(self.eeg_data))

    @staticmethod
    def num_rows(file_path):
        with open(file_path, 'rU') as csvfile:
            # reader = csv.reader(csvfile, delimiter=csv.excel.delimiter)
            return sum(1 for row in csvfile)
            # xs = np.asarray([x[14:23] for x in xs_time[:3]])
            # ys = np.asarray(eeg_0[:100])
            # xs1 = np.arange(100)
            # plt.xticks(xs1, xs)
            # plt.plot(xs1, ys)
            # plt.show()

    def appedEegOneByOne(self, reader):
        it = iter(reader)
        next(it)
        # eeg = np.empty((self.num_rows, 4))
        rows = self.num_rows
        cols = 4
        # eeg = np.empty((rows, cols))
        eeg = [(None, [0 for x in range(cols)]) for y in range(rows)]
        eegInd = 0
        wave_data = {'ALPHA': [], 'BETA': [], 'DELTA': [], 'THETA': [], 'GAMMA': []}

        for ind, row in enumerate(it):
            # ntp = datetime.strptime(row[0][:26], '%Y-%m-%d %H:%M:%S.%f')
            # server = datetime.strptime(row[1][:26], '%Y-%m-%d %H:%M:%S.%f')
            # timerow = np.array(ntp,server,dtype=time)
            start_time = datetime.datetime.now()

            time = timeObject(row)
            data_type = row[4]

            if data_type == 'EEG':
                eeg[eegInd] = [time, row[5:9]]
                eegInd += 1
            else:
                wave = row[4]
                wave_data[data_type].append((time, wave))
        return EegData(eeg, wave_data)

    def __str__(self):
        return 'num_rows:' + str(self.num_rows) + ", file:" + len(self.experimentData) + ", eeg data: " + self.eeg_data


class timeObject:
    def __init__(self, row):
        self.timetag_ntp = row[0]
        self.server_timestamp = row[1]
        self.raw_ntp_timetag = row[2]
        self.raw_server_timetap = row[3]


class EegData:
    def __init__(self, raw_eeg, wave_data):
        self.wave_data = wave_data
        self.raw_eeg = raw_eeg

    def __str__(self):
        to_string = []
        for index, wave_type in enumerate(self.wave_data):
            to_string.append(wave_type + ": " + str(len(self.wave_data[wave_type])) + ",")
        to_string.append("eeg data:" + str(len(self.raw_eeg)))
        return ''.join(to_string)
