import logging

FORMAT = '%(asctime)s - %(levelname)s - %(message)s'


def get_logger(loggerName):
    logging.basicConfig(format=FORMAT)
    logger = logging.getLogger(loggerName)
    logger.setLevel(logging.INFO)
    return logger
