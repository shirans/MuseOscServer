from inputparser.input_parser import InputParser
from utils.log import get_logger
import graph.plot_graph as pl
from dbtest.experiment_data import ExperimentData
import graph.avg_around_signal as p300

# FILE_LOCATION = '/Users/shiran/out/17-01-20_13-33/OddBall.csv'
# FILE_LOCATION = '/Users/shiran/out/17-04-08_16-15_Alpha'
# FILE_LOCATION = '/Users/shiran/out/17-04-16_00-41_Alpha'
FILE_LOCATION = '/Users/shiran/out/17-05-05_11-54_OddBall_P300'  # a good alpha data


# raw eeg is measured in micro volts.
#  2 uv is considered to be the noise level of eeg measurements (1 uv == ~ 10^-6 v ==> 0.000002 volts )
# 10 - 100 uv  is the peak to peak amplitude of an average EEG (0.00001 to 0.0001 volts )
# 1 mv == ~10^-3 v ==> 0.001 volts
# Muse raw signals are 0.0 to 1682.815 micro-volts, which is 0 to 1.682815 volt
# http://alexandre.barachant.org/blog/2017/02/05/P300-with-muse.html :
# things that anything beyond 100 uv is noise like blinks.
# the data from my app is ~800 micro volt, which is 800*10^-3 volt which is X/10^-6 ==  800*10^-3/10^-6 milli volt == 800* 10^3


def load_data():
    db = ExperimentData()
    input_parser = InputParser(FILE_LOCATION)
    ex_id = db.insert(input_parser)
    logger.info("new experiment id:" + str(ex_id))
    return ex_id


if __name__ == '__main__':
    logger = get_logger('main')
    # trial = load_data()
    trial_id = 4
    # pl.generate_graph(trial_id)
    p300.plot(trial_id)
