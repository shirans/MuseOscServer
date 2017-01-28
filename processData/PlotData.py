import matplotlib.pyplot as plt
import numpy as np
import csv

# a = np.loadtxt(open('/Users/shiran/out/16-12-10_19-58/Alpha.csv', "rb"), dtype=str, delimiter=",", skiprows=1)

# my_data = np.genfromtxt('/Users/shiran/out/16-12-10_19-58/Alpha.csv',  dtype=None,delimiter=',',skip_header=1,missing_values=('MISSING','MISSING','MISSING','MISSING'))
from datetime import datetime, tzinfo

# with open('/Users/shiran/out/17-01-20_13-33/OddBall.csv', 'rU') as csvfile:
#     reader = csv.reader(csvfile, delimiter=csv.excel.delimiter)
#     numrows = sum(1 for row in csvfile)
#
#
#
#
#
# def appedEEGOneByOne(eeg_0, eeg_1, eeg_2, eeg_3, xs_time):
#     alpha = []
#     beta = []
#     gamma = []
#     delta = []
#     theta = []
#     for ind, row in enumerate(it):
#         # ntp = datetime.strptime(row[0][:26], '%Y-%m-%d %H:%M:%S.%f')
#         # server = datetime.strptime(row[1][:26], '%Y-%m-%d %H:%M:%S.%f')
#         # timerow = np.array(ntp,server,dtype=time)
#         xs_time.append(row[0])
#         if row[4] == 'EEG':
#             eeg_0.append(row[5])
#             eeg_1.append(row[6])
#             eeg_2.append(row[7])
#             eeg_3.append(row[8])
#         elif row[4] == 'ALPHA':
#             alpha.append(row[5])
#         elif row[4] == 'BETA':
#             beta.append(row[5])
#         elif row[4] == 'DELTA':
#             delta.append(row[5])
#         elif row[4] == 'THETA':
#             theta.append(row[5])
#         elif row[4] == 'GAMMA':
#             gamma.append(row[5])
#     plt.show()
#
#
# data = []
# with open('/Users/shiran/out/16-12-10_19-58/oddBallResults.txt', 'rU') as f:
#     for line in f:
#         data.append(line.split(','))
#
# eventTime = data.get(0)[0]

    # plt.plot(xs,ys)
    # plt.axis(xs)


#
# X = np.linspace(-np.pi, np.pi, 256, endpoint=True)
# C, S = np.cos(X), np.sin(X)
#
# plt.plot(X, C)
# plt.plot(X, S)

# plt.show()
