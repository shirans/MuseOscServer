from dbtest.experiment_data import get_connection, DEVICE_TIMESTAMP_COL, SERVER_TIMESTAMP_COL, TYPE_COL, TP9, AF7, AF8, \
    TP10, RAW_EEG_TABLE
from graph.plot_graph import extractWavesFromDB
from inputparser.input_parser import ALPHA_ABSOLUTE


def plot(trial_id):
    conn = get_connection()
    c = conn.cursor()
    all_data = c.execute("select * from {} where trial_id={} order by {}"
                         .format(RAW_EEG_TABLE, trial_id, DEVICE_TIMESTAMP_COL)).fetchall()

    print all_data