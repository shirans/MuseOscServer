from aetypes import Enum

import ntplib
import csv
from os import listdir
from os.path import isfile
import os

from utils.log import get_logger

EEG = 'EEG'
ALPHA_ABSOLUTE = 'ALPHA_ABSOLUTE'
BETA_ABSOLUTE = 'BETA_ABSOLUTE'
GAMMA_ABSOLUTE = 'GAMMA_ABSOLUTE'
DELTA_ABSOLUTE = 'DELTA_ABSOLUTE'
THETA_ABSOLUTE = 'THETA_ABSOLUTE'


class DataType(Enum):
    EEG = 0
    ALPHA_ABSOLUTE = 1
    BETA_ABSOLUT = 2
    GAMMA_ABSOLUTE = 3
    DELTA_ABSOLUTE = 4
    THETA_ABSOLUTE = 5


logger = get_logger('InputParser')


def extract_cues(reader):
    it = iter(reader)
    cues = []
    for row in it:
        cues.append((row[0], row[1]))
    return cues


class InputParser:
    def __init__(self, folder_path):
        self.folderPath = folder_path
        for f in listdir(folder_path):
            if isfile:
                filename, file_extension = os.path.splitext(f)
                if file_extension == '.txt':
                    self.experiment_cues = os.path.join(folder_path, f)
                elif file_extension == '.csv':
                    self.experiment_data = os.path.join(folder_path, f)
        expType = os.path.basename(folder_path).split('_')
        self.date = expType[0]
        self.time = expType[1]
        self.type = expType[2]
        self.num_rows = self.num_rows(self.experiment_data)
        logger.info("num rows: " + str(self.num_rows))
        logger.info("starting to parse experiment data from:" + self.experiment_data)
        with open(self.experiment_data, 'rU') as csvfile:
            reader = csv.reader(csvfile, delimiter=csv.excel.delimiter)
            columns = ['Timetag Ntp', 'Server Timestamp', 'Raw Timetag', ' Raw Server Timestamp', 'Data Type', 'data']
            self.eeg_data = self.appedEegOneByOne(reader)
        with open(self.experiment_cues) as csv_file:
            reader = csv.reader(csv_file, delimiter=csv.excel.delimiter)
            self.cues = extract_cues(reader)

        logger.info("created data: " + str(self.eeg_data))
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
        rows = self.num_rows
        cols = 4
        eeg = [(None, [0 for x in range(cols)]) for y in range(rows)]
        dataIndex = {ALPHA_ABSOLUTE: 0, BETA_ABSOLUTE: 0, DELTA_ABSOLUTE: 0, THETA_ABSOLUTE: 0, GAMMA_ABSOLUTE: 0,
                     EEG: 0}
        wave_data = {ALPHA_ABSOLUTE: [(None, [0 for x in range(cols)]) for y in range(rows)],
                     BETA_ABSOLUTE: [(None, [0 for x in range(cols)]) for y in range(rows)],
                     DELTA_ABSOLUTE: [(None, [0 for x in range(cols)]) for y in range(rows)],
                     THETA_ABSOLUTE: [(None, [0 for x in range(cols)]) for y in range(rows)],
                     GAMMA_ABSOLUTE: [(None, [0 for x in range(cols)]) for y in range(rows)]}

        for ind, row in enumerate(it):
            # ntp = datetime.strptime(row[0][:26], '%Y-%m-%d %H:%M:%S.%f')
            # server = datetime.strptime(row[1][:26], '%Y-%m-%d %H:%M:%S.%f')
            # timerow = np.array(ntp,server,dtype=time)

            time = timeObject(row)
            data_type = row[4]

            index = dataIndex[data_type]
            if data_type == EEG:
                eeg[index] = [time, row[5:10]]
            else:
                wave_data[data_type][index] = [time, row[5:9]]
            dataIndex[data_type] += 1
        shorten_wave_data = {}
        for key, data in wave_data.iteritems():
            shorten_wave_data[key] = data[:dataIndex[key]]

        return EegData(eeg[:dataIndex[EEG]], shorten_wave_data)

    def __str__(self):
        return 'num_rows:' + str(self.num_rows) + ", file:" + len(self.experiment_data) + ", eeg data: " + self.eeg_data


class timeObject:
    def __init__(self, row):
        self.timetag_ntp = row[0]
        self.server_timestamp = row[1]
        self.raw_ntp_timestamp = row[2]
        self.raw_server_timestamp = row[3]


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
