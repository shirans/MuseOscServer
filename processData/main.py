import os
import matplotlib.dates as md
from graph.plot_graph import PlotGraph
from inputparser.input_parser import InputParser
from dbtest.experiment_data import ExperimentData, get_connection, WAVE_EEG_TABLE, SERVER_TIMESTAMP_COL, VALUE_COL, \
    EEG_TYPE_COL, TYPE_COL, CUES_TABLE
from utils.log import get_logger
import matplotlib.pyplot as plt

# FILE_LOCATION = '/Users/shiran/out/17-01-20_13-33/OddBall.csv'
FILE_LOCATION = '/Users/shiran/out/17-02-04_20-45_Alpha'


def load_data():
    db = ExperimentData()
    input_parser = InputParser(FILE_LOCATION)
    db.insert(input_parser)


if __name__ == '__main__':
    logger = get_logger('main')
    # load_data()

    conn = get_connection()
    c = conn.cursor()
    # get all the eeg wage data
    all_data = c.execute("select {},{} from {} where {}='ALPHA' "
                         .format(SERVER_TIMESTAMP_COL, VALUE_COL, WAVE_EEG_TABLE,TYPE_COL))\
        .fetchall()

    trial_ticks = c.execute("select * from {} ".format(CUES_TABLE))

    split = zip(*all_data)

    y = split[1] # alpha waves values
    x = split[0]  # timestamps
    x_md =md.date2num(x)
    plt.subplots_adjust(bottom=0.2)
    plt.xticks(rotation=25)
    ax = plt.gca()
    xfmt = md.DateFormatter('%H:%M:%S.%s.%f')
    ax.xaxis.set_major_formatter(xfmt)

    plt.plot(x_md, y)
    plt.show()
    print "end"
