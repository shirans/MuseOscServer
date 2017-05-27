import argparse
from pythonosc import dispatcher
from pythonosc import osc_server
from stream_info import get_outlet


# def eeg_handler(unused_addr, args, ch1, ch2, ch3, ch4, ch5):
#     single_data = [ch1, ch2, ch3, ch4, ch5]
#     outlet.push_sample(single_data)
#     print("EEG (uV) per channel: ", ch1, ch2, ch3, ch4, ch5)
#     print("args: ", args)
#     print("unused_addr ", unused_addr)

def eeg_handler(unused_addr, args, osc_time, ch1, ch2, ch3, ch4, ch5):
    single_data = [ch1, ch2, ch3, ch4, ch5]
    outlet.push_sample( single_data, osc_time)


unique_id = 'Muse1234567890'
outlet = get_outlet(unique_id)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--ip",
                        default="10.0.0.5",
                        help="The ip to listen on")
    parser.add_argument("--port",
                        type=int,
                        default=5001,
                        help="The port to listen on")
    args = parser.parse_args()
    print("unique id:" + unique_id)

    dispatcher = dispatcher.Dispatcher()
    # dispatcher.map("/debug", print)
    dispatcher.map("/muse/eeg", eeg_handler, "EEG")
    # dispatcher.map("/muse/elements/alpha_absolute", print)
    # dispatcher.map("/muse/elements/beta_absolute", print)
    # dispatcher.map("/muse/elements/gamma_absolute", print)
    # dispatcher.map("/muse/elements/delta_absolute", print)
    # dispatcher.map("/muse/elements/theta_absolute", print)
    server = osc_server.ThreadingOSCUDPServer(
        (args.ip, args.port), dispatcher)
    print("Serving on {}".format(server.server_address))
    server.serve_forever()
