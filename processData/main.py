import os

from graph.plot_graph import PlotGraph
from inputparser.input_parser import InputParser
from dbtest.experiment_data import ExperimentData
from utils.log import get_logger

# FILE_LOCATION = '/Users/shiran/out/17-01-20_13-33/OddBall.csv'
FILE_LOCATION = '/Users/shiran/out/17-02-04_20-45_Alpha'

if __name__ == '__main__':
    logger = get_logger('main')

    # g = PlotGraph(input_parser)
    # g.plot();
    # logger.info('num rows %s', input_parser)
    db = ExperimentData()

    input_parser = InputParser(FILE_LOCATION)

    db.insert(input_parser)

