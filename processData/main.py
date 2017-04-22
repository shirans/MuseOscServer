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
# FILE_LOCATION = '/Users/shiran/out/17-04-08_16-15_Alpha'
FILE_LOCATION = '/Users/shiran/out/17-04-16_00-41_Alpha'


def load_data(save_to_db):
    db = ExperimentData()
    input_parser = InputParser(FILE_LOCATION)
    if save_to_db:
        ex_id = db.insert(input_parser)
        logger.info("new experiment id:" + str(ex_id))


def extract_eeg(data):
    x = data[0]  # timestamps
    y = data[1]  # eeg
    x_md = md.date2num(x)
    return x_md, y


def generate_graph(trial_id):
    conn = get_connection()
    c = conn.cursor()
    alpha_data = c.execute("select {},{} from {} where {}='ALPHA' and trial_id={}"
                           .format(SERVER_TIMESTAMP_COL, VALUE_COL, WAVE_EEG_TABLE, TYPE_COL, trial_id)).fetchall()

    beta_data = c.execute("select {},{} from {} where {}='BETA' and trial_id={}"
                          .format(SERVER_TIMESTAMP_COL, VALUE_COL, WAVE_EEG_TABLE, TYPE_COL, trial_id)).fetchall()

    trial_ticks = c.execute(
        "select server_timestamp,cue_name from {} where trial_id={}".format(CUES_TABLE, trial_id)).fetchall()

    split2 = zip(*trial_ticks)

    x_alpha, y_alpha = extract_eeg(zip(*alpha_data))
    x_beta, y_beta = extract_eeg(zip(*beta_data))

    x_cues = md.date2num(split2[0])
    y2_cues = []
    for i in range(5):
        y2_cues.append(0)

    plt.figure(1)
    plt.subplot(211)

    format_x_axis()

    plt.plot(x_alpha, y_alpha, 'r--', x_beta, y_beta, 'b--', x_cues, y2_cues, 'g^', linewidth=1.0)
    plt.show()

    print "end"


def format_x_axis():
    # rotate the lables
    plt.xticks(rotation=30)
    # format the time
    xfmt = md.DateFormatter('%M:%S.%f')
    ax = plt.gca()
    ax.xaxis.set_major_formatter(xfmt)


if __name__ == '__main__':
    logger = get_logger('main')
    load_data(True)
    generate_graph(7)
