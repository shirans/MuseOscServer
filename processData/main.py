from inputparser.input_parser import InputParser
from utils.log import get_logger
import graph.plot_graph as pl
from dbtest.experiment_data import ExperimentData

# FILE_LOCATION = '/Users/shiran/out/17-01-20_13-33/OddBall.csv'
# FILE_LOCATION = '/Users/shiran/out/17-04-08_16-15_Alpha'
# FILE_LOCATION = '/Users/shiran/out/17-04-16_00-41_Alpha'
FILE_LOCATION = '/Users/shiran/out/17-04-29_13-25_Alpha'


def load_data():
    db = ExperimentData()
    input_parser = InputParser(FILE_LOCATION)
    ex_id = db.insert(input_parser)
    logger.info("new experiment id:" + str(ex_id))
    return ex_id


if __name__ == '__main__':
    logger = get_logger('main')
    # trial = load_data()
    pl.generate_graph(2)
