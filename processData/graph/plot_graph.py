import matplotlib.pyplot as plt
import math
import numpy as np
import matplotlib.dates as md
from inputparser.input_parser import InputParser, ALPHA_ABSOLUTE
from dbtest.experiment_data import ExperimentData, get_connection, WAVE_EEG_TABLE, SERVER_TIMESTAMP_COL, \
    TYPE_COL, CUES_TABLE, TP9, AF8, AF7, TP10


def to_pow(x):
    return math.pow(10, x)


def extract_eeg(data):
    to_vectors = zip(*data)
    x = to_vectors[0]  # timestamps
    y = to_vectors[1:]  # eeg
    x = np.array(x)
    y = np.transpose(np.array(y))
    ny_pow = np.vectorize(to_pow)(y)

    y_mean_non_log = np.mean(ny_pow, axis=1)
    x_md = md.date2num(x)
    return x_md, y_mean_non_log,


def print_mean_by_cue(x_alpha, y_alpha, x_cues):
    mean_by_cue = []
    sub_tot = 0
    num = 0
    cue_index = 0
    for i, entry in np.ndenumerate(x_alpha):
        if cue_index > len(x_cues) - 1:
            sub_tot += y_alpha[i]
            num += 1
        elif entry > x_cues[cue_index]:
            mean_by_cue.append(sub_tot / num)
            sub_tot = 0
            num = 0
            cue_index += 1
        else:
            sub_tot += y_alpha[i]
            num += 1
    mean_by_cue.append(sub_tot / num)
    print mean_by_cue


def plot_lines_in_cues_timestamps(y_alpha_non_log, x_cues):
    min_val = min(y_alpha_non_log)
    max_val = max(y_alpha_non_log)
    for i in x_cues:
        plt.axvline(x=i, ymin=0, ymax=max_val, c="blue", linewidth=1, zorder=0)


def generate_graph(trial_id):
    conn = get_connection()
    c = conn.cursor()
    wave_type = ALPHA_ABSOLUTE
    alpha_data = c.execute("select {},{},{},{},{} from {} where {}='{}' and trial_id={}"
                           .format(SERVER_TIMESTAMP_COL, TP9, AF7, AF8, TP10, WAVE_EEG_TABLE, TYPE_COL, wave_type,
                                   trial_id)).fetchall()

    trial_ticks = c.execute(
        "select server_timestamp,cue_name from {} where trial_id={}".format(CUES_TABLE, trial_id)).fetchall()
    split2 = zip(*trial_ticks)

    x_alpha, y_alpha_non_log = extract_eeg(alpha_data)
    x_cues = md.date2num(split2[0])

    y2_cues = []
    for i in range(5):
        y2_cues.append(0)

    plt.figure(1)
    plt.subplot(211)

    format_x_axis(False)

    # plt.plot(x_alpha, y_alpha, 'r--', x_beta, y_beta, 'b--', x_cues, y2_cues, 'g^', linewidth=1.0)
    plt.plot(x_alpha, y_alpha_non_log, 'r--', linewidth=1.0)

    # plt.plot((x_alpha[30], min_val), (x_alpha[30], max_val), 'k-')
    # ax1 = plt.subplot()

    #
    print_mean_by_cue(x_alpha, y_alpha_non_log, x_cues)
    plot_lines_in_cues_timestamps(y_alpha_non_log, x_cues)

    plt.show()

    print "end"


def format_x_axis(is_log_scale):
    # rotate the lables
    plt.xticks(rotation=30)
    # format the time
    xfmt = md.DateFormatter('%M:%S.%f')
    ax = plt.gca()
    if is_log_scale:
        ax.set_yscale('log')
    ax.xaxis.set_major_formatter(xfmt)
