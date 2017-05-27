import matplotlib.pyplot as plt
import math
import numpy as np
import matplotlib.dates as md
from inputparser.input_parser import InputParser, ALPHA_ABSOLUTE, BETA_ABSOLUTE, GAMMA_ABSOLUTE, DELTA_ABSOLUTE, \
    THETA_ABSOLUTE
from dbtest.experiment_data import ExperimentData, get_connection, WAVE_EEG_TABLE, SERVER_TIMESTAMP_COL, \
    TYPE_COL, CUES_TABLE, TP9, AF8, AF7, TP10, DEVICE_TIMESTAMP_COL


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
        plt.axvline(x=i, ymin=0, ymax=max_val, c="blue", linewidth=1, zorder=0)


def plot_relative(alpha_relative, beta_relative, x_cues):
    split = zip(*alpha_relative)
    x = split[0]
    y = split[1]
    x = md.date2num(x)
    fig = plt.figure()
    fig.add_subplot(111)
    format_x_axis(False)
    plt.plot(x, y, '--r', linewidth=1.0)

    plot_lines_in_cues_timestamps(y, x_cues)


def plotGraph(x_alpha, y_alpha, is_log_scale, x_cues, style):
    fig = plt.figure()
    fig.add_subplot(111)
    format_x_axis(is_log_scale)
    plt.plot(x_alpha, y_alpha, style, linewidth=1.0)

    print_mean_by_cue(x_alpha, y_alpha, x_cues)
    plot_lines_in_cues_timestamps(y_alpha, x_cues)
    return fig


def Swticher(argument):
    swtitcher = {
        ALPHA_ABSOLUTE: 0,
        BETA_ABSOLUTE: 1,
        GAMMA_ABSOLUTE: 2,
        DELTA_ABSOLUTE: 3,
        THETA_ABSOLUTE: 4,
    }
    return swtitcher.get(argument, lambda: 5)


def getRelativeData(all_data):
    alpha_relatives = []
    beta_relatives = []
    curr = []
    timestamps = []
    for data in all_data:
        timestamp = data[0]
        if timestamp not in timestamps:
            timestamps.append(timestamp)
        wave_type = data[1]
        index = Swticher(wave_type)
        if index > 4:
            print "err:" + data
        avg_val = (to_pow(data[2]) + to_pow(data[3]) + to_pow(data[4]) + to_pow(data[5])) / 4
        curr.insert(index, avg_val)
        if len(curr) == 5:
            sum_all = math.pow(10, curr[0]) + math.pow(10, curr[1]) + math.pow(10, curr[2]) + math.pow(10, curr[
                3]) + math.pow(10, curr[4])
            alpha_relative = math.pow(10, curr[0]) / sum_all
            beta_relative = math.pow(10, curr[1]) / sum_all

            max_timestamp = np.max(timestamps)

            alpha_relatives.append([max_timestamp, alpha_relative])
            beta_relatives.append([max_timestamp, beta_relative])
            curr = []
            timestamps = []
    return alpha_relatives, beta_relatives


def generate_graph(trial_id):
    conn = get_connection()
    c = conn.cursor()
    alpha_data = extractWavesFromDB(c, trial_id, ALPHA_ABSOLUTE)
    all_data = c.execute("select {},{},{},{},{},{} from {} where trial_id={} order by {}"
                         .format(SERVER_TIMESTAMP_COL, TYPE_COL, TP9, AF7, AF8, TP10, WAVE_EEG_TABLE,
                                 trial_id, DEVICE_TIMESTAMP_COL)).fetchall()
    alpha_relative, beta_relative = getRelativeData(all_data)

    trial_ticks = c.execute(
        "select server_timestamp,cue_name from {} where trial_id={}".format(CUES_TABLE, trial_id)).fetchall()
    split = zip(*trial_ticks)

    x_alpha, y_alpha_linear, y_alpha_log10 = extract_eeg(alpha_data)
    x_beta, y_beta_linear, y_beta_log10 = extract_eeg(alpha_data)
    x_cues = md.date2num(split[0])

    plot_relative(alpha_relative, beta_relative, x_cues)

    # plotGraph(x_alpha, y_alpha_linear, False, x_cues, 'r--')
    # plotGraph(x_beta, y_beta_linear, False, x_cues, 'g--')
    plt.show()

    print "end"


def extractWavesFromDB(c, trial_id, wave_type):
    return c.execute("select {},{},{},{},{} from {} where {}='{}' and trial_id={}"
                     .format(SERVER_TIMESTAMP_COL, TP9, AF7, AF8, TP10, WAVE_EEG_TABLE, TYPE_COL, wave_type,
                             trial_id)).fetchall()


def format_x_axis(is_log_scale):
    # rotate the lables
    plt.xticks(rotation=30)
    # format the time
    xfmt = md.DateFormatter('%M:%S.%f')
    ax = plt.gca()
    if is_log_scale:
        ax.set_yscale('log')
    ax.xaxis.set_major_formatter(xfmt)
