import matplotlib.pyplot as plt
import numpy as np
import csv


class InputParser:
    def __init__(self, file_path):
        self.file_path = file_path
        self.num_rows = self.num_rows(self.file_path)
        self.parse_file()
        self.eeg_data

    @staticmethod
    def num_rows(file_path):
        with open(file_path, 'rU') as csvfile:
            # reader = csv.reader(csvfile, delimiter=csv.excel.delimiter)
            return sum(1 for row in csvfile)

    def parse_file(self):
        with open(self.file_path, 'rU') as csvfile:
            reader = csv.reader(csvfile, delimiter=csv.excel.delimiter)
            columns = ['Timetag Ntp', 'Server Timestamp', 'Raw Timetag', ' Raw Server Timestamp', 'Data Type', 'data']

            eeg_0 = []
            eeg_1 = []
            eeg_2 = []
            eeg_3 = []
            xs_time = []
            self.eeg_data = self.appedEEGOneByOne(eeg_0, eeg_1, eeg_2, eeg_3, xs_time, reader)
            # xs = np.asarray([x[14:23] for x in xs_time[:3]])
            # ys = np.asarray(eeg_0[:100])
            # xs1 = np.arange(100)
            # plt.xticks(xs1, xs)
            # plt.plot(xs1, ys)
            # plt.show()

    def appedEEGOneByOne(self, eeg_0, eeg_1, eeg_2, eeg_3, xs_time, reader):
        it = iter(reader)
        next(it)
        xs_time = []
        eeg = np.empty((self.num_rows, 4))
        alpha = []
        beta = []
        gamma = []
        delta = []
        theta = []
        for ind, row in enumerate(it):
            # ntp = datetime.strptime(row[0][:26], '%Y-%m-%d %H:%M:%S.%f')
            # server = datetime.strptime(row[1][:26], '%Y-%m-%d %H:%M:%S.%f')
            # timerow = np.array(ntp,server,dtype=time)
            xs_time.append(row[0])
            if row[4] == 'EEG':
                eeg[:ind, :] = row[5:9]
            elif row[4] == 'ALPHA':
                alpha.append(row[5])
            elif row[4] == 'BETA':
                beta.append(row[5])
            elif row[4] == 'DELTA':
                delta.append(row[5])
            elif row[4] == 'THETA':
                theta.append(row[5])
            elif row[4] == 'GAMMA':
                gamma.append(row[5])
        return EegData(xs_time, alpha, beta, gamma, delta, theta)

    def __str__(self):
        return 'num_rows:' + str(self.num_rows) + ", file:" + len(self.file_path) + ", eeg data: " + self.eeg_data


class EegData:
    def __init__(self, xs_time, alpha, beta, gamma, delta, theta):
        self.xs_time = xs_time
        self.alpha = alpha
        self.beta = beta
        self.gamma = gamma
        self.delta = delta
        self.theta = theta

    def __str__(self):
        return 'x:' + str(len(self.xs_time)) + ", alpha:" + str(len(self.alpha))
