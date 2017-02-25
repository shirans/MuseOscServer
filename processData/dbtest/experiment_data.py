import sqlite3

from utils.log import get_logger
import datetime, time

logger = get_logger('ExperimentData')
META_DATA_TABLE = 'experiment_meta_data'
TYPE_COL = 'type'
ID_COL = 'id'
DATE_COL = 'date'
INSERT_METADATA = 'INSERT INTO {} ({},{}) values (?,?) '.format(META_DATA_TABLE, TYPE_COL, DATE_COL)
RAW_EEG_TABLE = 'raw_eeg'
EEG_TYPE_COL = 'eeg_type'
SERVER_TIMESTAMP_COL = 'server_timestamp'
DEVICE_TIMESTAMP_COL = 'device_timestamp'
TRIAL_ID_COL = 'trial_id'
ELECTRODE_1_COL = 'e_1'
ELECTRODE_2_COL = 'e_2'
ELECTRODE_3_COL = 'e_3'
ELECTRODE_4_COL = 'e_4'


def adapt_datetime(ts):
    return time.mktime(ts.timetuple())


class ExperimentData:
    def __init__(self):
        conn = self.getConnection()
        c = conn.cursor()
        # id is auto incremented: http://sqlite.org/autoinc.html
        c.execute('''CREATE TABLE IF NOT EXISTS {}
            ({} integer primary key, {} string, {} timestamp)'''.format(META_DATA_TABLE, ID_COL, TYPE_COL, DATE_COL))

        c.execute('''CREATE TABLE IF NOT EXISTS {}
            ({} integer primary key, {} integer, {} timestamp, {} timestamp, {} double, {} double, {} double, {} double)'''.format(
            RAW_EEG_TABLE, ID_COL, TRIAL_ID_COL, SERVER_TIMESTAMP_COL, DEVICE_TIMESTAMP_COL,
            ELECTRODE_1_COL, ELECTRODE_2_COL, ELECTRODE_3_COL, ELECTRODE_4_COL))

        # c.execute('''insert into %s
        #     (%s,%s) values ('T','2012-12-25 23:59:59')''' % (META_DATA_TABLE, TYPE_COL, DATE_COL))

        conn.commit()

        # for row in c.execute('SELECT * FROM oddballEvents'):
        #     logger.info(row)

        # We can also close the connection if we are done with it.
        # Just be sure any changes have been committed or they will be lost.
        conn.close()

    def getConnection(self):
        # in memory
        # return sqlite3.connect(':memory:', detect_types=sqlite3.PARSE_DECLTYPES | sqlite3.PARSE_COLNAMES)
        return sqlite3.connect('exdb.db', detect_types=sqlite3.PARSE_DECLTYPES | sqlite3.PARSE_COLNAMES)

    def insert(self, input_parser):
        conn = self.getConnection()
        c = conn.cursor()
        ex_type = input_parser.type
        date = "20" + input_parser.date + " " + input_parser.time.replace('-', ':') + ":00"

        c.execute(INSERT_METADATA, (ex_type, date))
        conn.commit()

        ex_id = c.execute("select * from experiment_meta_data where date =? and type = ? order by id desc",
                          (date, ex_type,)).fetchone()[0]

        INSERT_STM = '''INSERT INTO {} ({},{},{},{},{},{},{}) values (?,?,?,?,?,?,?)'''.format(
            RAW_EEG_TABLE, TRIAL_ID_COL, SERVER_TIMESTAMP_COL, DEVICE_TIMESTAMP_COL,
            ELECTRODE_1_COL, ELECTRODE_2_COL, ELECTRODE_3_COL, ELECTRODE_4_COL)

        err_count = 0
        for index, line in enumerate(input_parser.eeg_data.raw_eeg):
            try:
                server_timestamp = line[0].server_timestamp
                device_timestamp = line[0].timetag_ntp
                e1 = line[1][0]
                e2 = line[1][1]
                e3 = line[1][2]
                e4 = line[1][3]
                c.execute(INSERT_STM, (ex_id, server_timestamp, device_timestamp, e1, e2, e3, e4,))
            except Exception as e:
                logger.error(
                    'failed to parse line:' + str(line) + " at index:" + str(index) + " with message:" + e.message)
                err_count += 1
                continue

        conn.commit()
        conn.close()

        # We can also close the connection if we are done with it.
        # Just be sure any changes have been committed or they will be lost.

    def test(self):
        conn = self.getConnection()
        c = conn.cursor()
        for row in c.execute("select * from " + RAW_EEG_TABLE):
            print row
