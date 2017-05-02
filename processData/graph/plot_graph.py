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
    y_linear = np.vectorize(to_pow)(y)

    y_mean_linear = np.mean(y_linear, axis=1)
    y_mean_log10 = np.log10(y_mean_linear)
    x_md = md.date2num(x)
    return x_md, y_mean_linear, y_mean_log10


def print_mean_by_cue(x_alpha, y_alpha, x_cues):
    mean_by_cue = []
    sub_tot = 0
    num = 0
    cue_index = 0
    last_entry = x_alpha[0]
    for i, entry in np.ndenumerate(x_alpha):
        if cue_index > len(x_cues) - 1:
            sub_tot += y_alpha[i]
            num += 1
        elif entry > x_cues[cue_index]:
            mean_by_cue.append({num: sub_tot / num})
            # plt.axhline(y=sub_tot / num, xmin=last_entry, xmax=entry, c="green", linewidth=1,  zorder=0)
            last_entry = entry
            sub_tot = 0
            num = 0
            cue_index += 1
        else:
            sub_tot += y_alpha[i]
            num += 1
    mean_by_cue.append(sub_tot / num)
    print mean_by_cue


def plot_lines_in_cues_timestamps(y_alpha_linear, x_cues):
    max_val = max(y_alpha_linear)
    for i in x_cues:
        print i
        plt.axvline(x=i, ymin=0, ymax=max_val, c="blue", linewidth=1, zorder=0)


def plotGraph(x_alpha, y_alpha_linear, False, x_cues):
    pass


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

    x_alpha, y_alpha_linear, y_alpha_log10 = extract_eeg(alpha_data)
    x_cues = md.date2num(split2[0])

    plt.figure(1)
    plt.subplot(211)

    format_x_axis(False)

    plotGraph(x_alpha, y_alpha_linear, False, x_cues)
    plt.plot(x_alpha, y_alpha_linear, 'r--', linewidth=1.0)
    plt.plot(x_alpha, y_alpha_log10, 'g--', linewidth=1.0)

    print_mean_by_cue(x_alpha, y_alpha_linear, x_cues)
    plot_lines_in_cues_timestamps(y_alpha_linear, x_cues)

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
