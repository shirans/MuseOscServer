import sqlite3

from utils.log import get_logger

logger = get_logger('ExperimentData')


class ExperimentData:
    def __init__(self):
        conn = self.getConnection()
        c = conn.cursor()

        # Create table
        c.execute('''CREATE TABLE eegTimeline
                     (timetagNtp text, timetagServer text, rawNtpTimestamp timestamp, rawServerTimestamp  timestamp)''')

        # c.execute('''CREATE TABLE timeline
        #     (timetag_ntp text, timetag_server text, raw_ntpm_timestamp timestamp, raw_erver timestamp  timestamp)''')

        # Insert a row of data
        #
        #
        # purchases = [('2006-03-28', 'BUY', 'IBM', 1000, 45.00),
        #              ('2006-04-05', 'BUY', 'MSFT', 1000, 72.00),
        #              ('2006-04-06', 'SELL', 'IBM', 500, 53.00),
        #              ]

        # c.executemany('INSERT INTO stocks VALUES (?,?,?,?,?)', purchases)
        # Save (commit) the changes
        conn.commit()

        # for row in c.execute('SELECT * FROM oddballEvents'):
        #     logger.info(row)

        # We can also close the connection if we are done with it.
        # Just be sure any changes have been committed or they will be lost.
        conn.close()

    def getConnection(self):
        return sqlite3.connect(':memory:')

    def insert(self, input_parser):
        conn = self.getConnection()
        c = conn.cursor()

        for line in input_parser.eeg_data.xs_time:
            c.execute('''INSERT INTO eegTimeline VALUES ('2017-01-20 13:33:18.000481',"
                  "'2017-01-20 13:33:18.000597',1484911998597,1484911998597)''')
        # Create table


        # We can also close the connection if we are done with it.
        # Just be sure any changes have been committed or they will be lost.
        conn.close()
