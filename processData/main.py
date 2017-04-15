import os
import matplotlib.dates as md
from matplotlib.patches import Rectangle
from graph.plot_graph import PlotGraph
from inputparser.input_parser import InputParser
from dbtest.experiment_data import ExperimentData, get_connection, WAVE_EEG_TABLE, SERVER_TIMESTAMP_COL, VALUE_COL, \
    EEG_TYPE_COL, TYPE_COL, CUES_TABLE
from utils.log import get_logger
import matplotlib.pyplot as plt

# FILE_LOCATION = '/Users/shiran/out/17-01-20_13-33/OddBall.csv'
FILE_LOCATION = '/Users/shiran/out/17-04-08_16-15_Alpha'


def load_data(save_to_db):
    db = ExperimentData()
    input_parser = InputParser(FILE_LOCATION)
    if save_to_db:
        db.insert(input_parser)


def generate_graph(trial_id):
    conn = get_connection()
    c = conn.cursor()
    all_data = c.execute("select {},{} from {} where {}='ALPHA' and trial_id={}"
                         .format(SERVER_TIMESTAMP_COL, VALUE_COL, WAVE_EEG_TABLE, TYPE_COL, trial_id)) \
        .fetchall()

    trial_ticks = c.execute(
        "select server_timestamp,cue_name from {} where trial_id={}".format(CUES_TABLE, trial_id)).fetchall()

    split = zip(*all_data)
    split2 = zip(*trial_ticks)

    y = split[1]  # alpha waves values
    x = split[0]  # timestamps
    x_md = md.date2num(x)

    x_md2 = md.date2num(split2[0])
    # y_2 = split2[1]

    plt.subplots_adjust(bottom=0.2)
    plt.xticks(rotation=25)
    ax = plt.gca()
    r = Rectangle((.5, .5), .25, .1, fill=False)
    y_2 = []
    for i in range(5):
        y_2.append(1)
    xfmt = md.DateFormatter('%M:%S.%s.%f')
    ax.xaxis.set_major_formatter(xfmt)

    plt.plot(x, y, 'r--',x_md2, y_2,'bs')
    plt.show()
    print "end"


if __name__ == '__main__':
    logger = get_logger('main')
    # load_data(True)
    generate_graph(3)
